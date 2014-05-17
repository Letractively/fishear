package net.fishear.data.tree.services;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.services.GenericService;
import net.fishear.data.tree.entities.TreeEntityI;
import net.fishear.data.tree.entities.TreeEntityTreeI;
import net.fishear.exceptions.AppException;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Exceptions;

/** CRUD methods and main "tree manipulation" methods to service modify tree elements simultaneously in main tree table and in "support" table too.
 * The "support" table keeps the master-detail pairs - for every leaf exists records pointing to each it's parent 
 * at each tree level (e.g. to direct parent, to upper parent ... etc ... to root).
 * @author terber
 *
 * @param <K>
 * @param <X>
 */
public abstract class
	GenericTreeService<K extends TreeEntityI<Z>, X extends TreeEntityTreeI<Z>, Z>
extends
	GenericService<K>
implements
	TreeServiceI<K>
{

	private Class<X> treeType;
	private final GenericDaoI<X> treeDao;
	private String parentPropertyName;
	private String parentId;
	
    protected GenericTreeService() {
    	super();
    	treeDao = createTreeDao();
    	parentPropertyName = getParentPropertyName();
    	parentId = parentPropertyName.concat(".id");
    }

    /** subclass informs parent about name of entitie's property, in which the parent object resides.
     */
    protected abstract String getParentPropertyName();
    
	@SuppressWarnings("unchecked")
	private <Y extends GenericDaoI<X>> GenericDaoI<X> createTreeDao() {
		try {

			Object[] oa = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
			if(oa == null || oa.length < 2) {
				throw new AppException("Subclass does not parametrize generic superclasses <GenericDaoI, EntityI>.");
			}

//			Class<?> primaryDao = (Class<?>)oa[0];
//
//			String treeDaoName = primaryDao.getPackage().getName() + "." + Classes.getShortClassName(treeType) + "Dao";		// creates DAO class name depends on entity name with additional "Dao" suffix
//
//			@SuppressWarnings("unused")
//			Class<Y> daoType = (Class<Y>) primaryDao.getClassLoader().loadClass(treeDaoName);

			Class<X> treeType = (Class<X>)oa[1];
			this.treeType = treeType;

			return (GenericDaoI<X>) DaoSourceManager.createDao(treeType, getClass());

		} catch (Exception ex) {
			throw Exceptions.runtime(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<K> getParents(K entity) {
		List<K> list = new ArrayList<K>();
		K parent = (K) entity.getParent();
		while(parent != null) {
			list.add(parent);
			parent = (K) parent.getParent();
		}
		return list;
	}

	public void delete(K entity) {

		deleteTreeMasterSlave(entity);
		deleteEntireTree(entity);

	}
	
	public Object save(K entity) {
		if(entity.isNew()) {
			try {
				Long id = (Long) super.save(entity);
				addParentsTree(entity);
				return id;
			} catch (Exception ex) {
				throw Exceptions.runtime(ex);
			}
		} else {
			K ori = super.read(entity.getId());
			super.save(entity);
			if(EntityUtils.equalsId(entity.getParent(), ori.getParent())) {
				deleteTreeMasterSlave(ori);
				try {
					createTreeMasterSlave(entity);
				} catch(Exception ex) {
					Exceptions.runtime(ex);
				}
			}
			return entity.getId();
		}
	}

	/** adds all parents for 'entity' to the master-slave table
	 */
	@SuppressWarnings("unchecked")
	private void addParentsTree(K entity) throws InstantiationException, IllegalAccessException {
		K parent = (K) entity.getParent();
		List<X> xList = new ArrayList<X>();

		X link = treeType.newInstance();
		link.setParentId(entity.getId());
		link.setChildId(entity.getId());
		xList.add(link);							// add lint to oneself

		while(parent != null) {						// while root entity is found, I add it to all parents
			X link2 = treeType.newInstance();
			link2.setParentId(parent.getId());
			link2.setChildId(entity.getId());
			parent = (K) parent.getParent();
			xList.add(link2);
		}
		for (int i = xList.size() - 1; i >= 0; i--) {
			X link3 = xList.get(i);
			link3.setTreeLevel(i);
			treeDao.save(link3);
		}
	}

	/** delete all tree informations about 'entity' and all sub-entities from master-slave table
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private void deleteTreeMasterSlave(K entity) {
		List<K> list = getChildren_(entity);
		for (K k : list) {
			deleteTreeMasterSlave(k);
		}
		QueryConstraints qc = QueryFactory.create(Restrictions.or(
				Restrictions.equal("parentId", entity.getId()),
				Restrictions.equal("childId", entity.getId())
		));
		List<X> listX = (List<X>) treeDao.query(qc);
		for (X x : listX) {
			treeDao.delete(x);
		}
	}

	/** create all tree informations about 'entity' and all sub-entities in master-slave table.
	 * This method re-creates all master-slave info. If any link already exists, the method treats it as success and DOES NOT throw exception.
	 * @param entity
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void createTreeMasterSlave(K entity) throws InstantiationException, IllegalAccessException {
		List<K> list = getChildren_(entity);
		for (K k : list) {
			createTreeMasterSlave(k);
		}
		addParentsTree(entity);
	}

	/** delete element 'entity' and all its children (at all sub-levels).
	 * All links in master-slave table must be deleted before this call (see {@link #deleteTreeMasterSlave(Be2TreeEntity)}).
	 */
	private void deleteEntireTree(K entity) {
		List<K> list = getChildren_(entity);
		for (K k : list) {
			deleteEntireTree(k);
		}
		super.delete(entity);
	}

	private List<K> getChildren_(K entity) {
		List<K> list = getChildren(entity);
		if(list == null) {
			throw new IllegalStateException("Children list must not be null. In case the entity is the leaf, 'getChildren' method must return empty list.");
		}
		return list;
	}

	@Override
	public void move(K what, K where) {
		deleteTreeMasterSlave(what);
		what.setParent(where);
		try {
			createTreeMasterSlave(what);
		} catch (Exception ex) {
			throw Exceptions.runtime(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getRelation(K parent, K child) {

		List<X> list = (List<X>) treeDao.query(QueryFactory.andEquals("parentId", parent.getId(), "childId", child.getId()));
		boolean negate = false;
		if(list.size() == 0) {
			negate = true;
			list = (List<X>) treeDao.query(QueryFactory.andEquals("childId", parent.getId(), "parentId", child.getId()));
		}
		if(list.size() == 0) {
			return null;
		} else if(list.size() > 1) {
			throw new AppException("There is more than one tree link record for two entities. This is potentially the application error.");
		}
		X x = list.get(0);
		return negate ? (-x.getTreeLevel()) : x.getTreeLevel();
	}

	@Override
	public boolean isAncestor(K parent, K child) {
		Integer rel = getRelation(parent, child);
		return rel == null ? false : rel.intValue() > 0;
	}

	@Override
	public boolean isChild(K parent, K child) {
		Integer rel = getRelation(parent, child);
		return rel == null ? false : rel.intValue() < 0;
	}

	@Override
	public boolean hasChildren(K parent) {
		return getChildren_(parent).size() > 0;
	}
	
	/** designed for test purposes - should not be called from application.
	 */
	public GenericDaoI<X> getTreeDao() {
		return treeDao;
	}
	
	@Override
	public List<K> getChildren(K cat) {
		QueryConstraints qc;
		if(cat == null) {
			qc = QueryFactory.defaults();
			qc.add(Restrictions.isNull(parentId));
		} else {
			qc = QueryFactory.equals(parentId, cat.getId());
		}
		return super.list(qc);
	}

	public int getChildrenCount(K parent, int maxDepth) {
		QueryConstraints qc = QueryFactory.createDefault();
		qc.add(Restrictions.and(
			Restrictions.equal("parentId", parent.getId()),
			Restrictions.lessThan("treeLevel", maxDepth == Integer.MAX_VALUE ? Integer.MAX_VALUE : maxDepth + 1),
			Restrictions.greaterThan("treeLevel", 0)
		));
        qc.results().addRowCount();
        List<?> list = treeDao.query(qc);
        return (Integer)list.get(0);
	}
	
}

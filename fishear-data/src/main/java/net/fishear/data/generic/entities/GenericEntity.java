package net.fishear.data.generic.entities;


import java.lang.reflect.ParameterizedType;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import net.fishear.data.generic.services.GenericService;
import net.fishear.exceptions.AppException;
import net.fishear.utils.Classes;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Globals;
import net.fishear.utils.ListFilter;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic entity is superclass for all entity classes. It implements necessary
 * "equals" and "hashCode" methods by general algorithm (they can be
 * reimplemented of course).
 * 
 * @author terber
 * @see net.fishear.utils.EntityUtils#equals(EntityI, Object)
 * @see net.fishear.utils.EntityUtils#hashCode(EntityI)
 */
@MappedSuperclass
public abstract class 
	GenericEntity<T>
implements 
	EntityI<T>,
	InitialStateI,
	Cloneable
{

	private static final Logger log = Globals.getLogger();
	
	private T id;
	
	private Class<T> idType;
	
	private EntityI<?> initialState;

	public GenericEntity() {
		idType = findType();
	}
	
	@Transient
	public T getId() {
		return id;
	}
	
	@Transient
	public String getIdString() {
		T id = getId();
		String idStr;
		if(id != null && (idStr = id.toString()) != null && idStr.length() > 0) {
			return idStr;
		} else {
			return null;
		}
	}
	
	public void setIdString(String idStr) {
		if(idStr == null || idStr.length() == 0) {
			setId(null);
		}
		setId(EntityUtils.convertType(idStr, idType));
	}

	@SuppressWarnings("unchecked")
	public void setId(Object id) {
		// as ion Oracle DB - empty string is treated as null
		if (id != null && id instanceof CharSequence && ((CharSequence)id).length() == 0) {
			this.id = null;
		} else {
			this.id = (T) id;
		}
	}

	@Override
	public boolean equals(Object o) {
        return super.equals(o);
//		return EntityUtilsBase.equals(this, o);
	}

	@Override
	public int hashCode() {
        return super.hashCode();
//		return EntityUtilsBase.hashCode(this);
	}
	
	/** returns filter, which filters all entities with the same id as this entity has.
	 */
	@Transient
	public ListFilter<GenericEntity<T>> getFilterToThisId() {
		return new ListFilter<GenericEntity<T>>() {
			@Override
			public boolean addToResult(GenericEntity<T> ae) {
				return id == ae.getId();
			}
		};
	}

	@Transient
	public boolean isNew() {
		Object id = getId();
		if(id == null) {
			return true;
		}
		if(id instanceof Number) {
			Long lid = ((Number) id).longValue();
			return lid == 0;
		}
		return false;
	}

	/** null - safe comparison of ID with ID of other entity.
	 * @return as the CompareTo() method of {@link Comparable}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compareId(EntityI<?> o) {
		Object oid = o.getId();
		if(this.id != null) {
			return oid == null ? 0 : -1;
		} else if(oid == null) {
			return 1;
		} else {
			if(this.id instanceof Comparable<?> && oid instanceof Comparable<?>) {
				return ((Comparable)this.id).compareTo(oid);
			}
			throw new IllegalStateException("Both IDs must implement Comparable interface.");
		}
	}

    @Transient
	public String entityDescription() {
    	String desc = "Entity '"+ Classes.getShortClassName(this) + "', id=" + getId();
    	final String[] knMets = {"getFullName", "getCode", "getName", "getTitle"};
    	for (int i = 0; i < knMets.length; i++) {
    		Object val = EntityUtils.callMethod(this, "getName", null);
    		if(val == null) {
    			continue;
    		}
    		desc = "" + knMets[i].substring(4) + val + " (id="+getIdString()+")";
		}
		return desc;
	}

    /** returns value of given attribute using it's getter.
     * Dot notation is allowed for introspection.
     * @throws net.fishear.exceptions.AppException as wrapped exception, if any occurs.
     */
    @Transient
	public Object getAttributeValue(String name) {
		try {
			return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(this, name);
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}
	
    /** returrns value if given attribute. 
     * In case exception or null, the 'dftVal' is returned. Exception is never thrown.
     * @param name attribute name (may be "dotted" reference to internal objects)
     * @param dftVal default value
     * @return attribute's value or 'dftVal'
     */
    @SuppressWarnings({ "hiding", "unchecked" })
	@Transient
	public <T> T get(String name, T dftVal) {
    	String metn = name.substring(0, 1).toLowerCase().concat(name.substring(1));
		try {
			T ret = (T) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(this, metn);
			if(ret != null) {
				return ret;
			}
		} catch (Exception ex) {
			log.debug("", ex);
		}
		return dftVal;
    }

    @Transient
    public void beforeSave() {
    	// the default - empty - implementation
    }

    @Transient
    public void beforeDelete() {
    	// the default - empty - implementation
    }

	@SuppressWarnings("unchecked")
	private Class<T> findType() {
		Class<?> clazz = this.getClass();
		while(clazz != Object.class) {
			Object gscl = clazz.getGenericSuperclass();
			if(ParameterizedType.class.isAssignableFrom(gscl.getClass())) {
				ParameterizedType pt = (ParameterizedType)gscl;
				Object[] oa = pt.getActualTypeArguments();
				if(oa != null && oa.length > 0) {
					return (Class<T>)oa[0];
				}
			}
			clazz = clazz.getSuperclass();
		}
		throw new AppException("Subclass does not parametrize generic superclass.");
	}

	@Transient
	public Class<T> getIdType() {
		return idType;
	}
	
	@Transient
	public void saveInitialState() {
		try {
			initialState = (EntityI<?>) this.clone();
		} catch (CloneNotSupportedException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	/**
	 * @return state of the entity after it is loaded 
	 */
	@Transient
	public EntityI<?> getInitialState() {
		return initialState;
	}
}

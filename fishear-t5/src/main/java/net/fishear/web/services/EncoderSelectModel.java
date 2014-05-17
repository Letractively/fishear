package net.fishear.web.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.order.SortDirection;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.EntityUtils;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * model for lists derived from entity (implementation of {@link EntityI}.
 * 
 * It allows to get data directly from underlying service (implementation of {@link ServiceI}.
 * Additionally allows set ordering by value or label ascending or descending. 
 * 
 * @author ffyxrr
 *
 * @param <T>
 */
public class EncoderSelectModel<T extends EntityI<?>> implements ValueEncoder<T>//, MapEncoder<T>
{

	public enum SortType {
		VALUE,
		LABEL
	}
	
	private ServiceI<T> service;

	/**
	 * list of entitie's attribute manes that will act in labels. Constants can be enclosed in apostrophes.
	 */
	private String[] attrNames;
	
	private QueryConstraints constraints;

	private SortType sortType = SortType.LABEL;
	
	private SortDirection sortDirection;

	/**
	 * @param service service acting as source of data
	 * @param attrNames list of entitie's attribute manes that will act in labels. Constants can be enclosed in apostrophes.
	 */
	public EncoderSelectModel(ServiceI<T> service, String... attrNames) {
		this.service = service;
		this.attrNames = attrNames;
	}

	public EncoderSelectModel() {
		
	}
	
	@Override
	public String toClient(T entity) {
		return entity.getIdString();
	}

	@Override
	public T toValue(String idString) {
		return service.read(EntityUtils.convertType(idString, service.getEntityType()));
	}

	/**
	 * @return the service
	 */
	public ServiceI<T> getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(ServiceI<T> service) {
		this.service = service;
	}

	public SelectModel getModel() {

		DataModel model = new DataModel();
		return model;
	}

	/**
	 * @return current constraints
	 */
	public QueryConstraints getConstraints() {
		return constraints;
	}

	/**
	 * @param constraints additional constraints to set. If not ser, all data are returned.
	 */
	public void setConstraints(QueryConstraints constraints) {
		this.constraints = constraints;
	}

	/**
	 * @return the attrNames
	 */
	public String[] getAttrNames() {
		return attrNames;
	}

	/**
	 * @param attrNames the attrNames to set
	 */
	public void setAttrNames(String... attrNames) {
		this.attrNames = attrNames;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int cmp(Comparable o1, Comparable o2) {
		if(o1 == null || o2 == null) {
			return 0;
		}
		if(sortDirection == SortDirection.DESCENDING) {
			return (- o1.compareTo(o2));
		} else {
			return o1.compareTo(o2);
		}
	}

	private class ValueComparator implements Comparator<OptionModel> {

		@SuppressWarnings("rawtypes")
		@Override
		public int compare(OptionModel o1, OptionModel o2) {
			return cmp((Comparable)o1.getValue(), (Comparable)o2.getValue());
		}
	}

	private class LabelComparator implements Comparator<OptionModel> {

		@Override
		public int compare(OptionModel o1, OptionModel o2) {
			return cmp(lower(o1.getLabel()), lower(o2.getLabel()));
		}

		private String lower(String label) {
			return label == null ? null : label.toLowerCase();
		}
		
	}
	
	public class DataModel extends AbstractSelectModel implements SelectModel {

		@Override
		public List<OptionGroupModel> getOptionGroups() {
			return null;
		}

		@Override
		public List<OptionModel> getOptions() {
			List<OptionModel> rlist = new ArrayList<OptionModel>();
			
			List<T> list = service.list(constraints);

			for (T t :list) {
				Option opt = new Option(t);
				rlist.add(opt);
			}
			if(sortType != null && sortDirection != null) {
				if(sortType == SortType.VALUE) {
					Collections.sort(rlist, new ValueComparator());
				} else if (sortType == SortType.LABEL) {
					Collections.sort(rlist, new LabelComparator());
				} else {
					throw new IllegalStateException(String.format("Unsupported sort type: '%s'", sortType));
				}
			}
			return rlist;
		}
	}

	public class Option implements OptionModel {

		private EntityI<?> entity;
		
		Option(EntityI<?> entity) {
			this.entity = entity;
		}
		
		@Override
		public boolean isDisabled() {
			return false;
		}

		@Override
		public Object getValue() {
			return entity.getIdString();
		}

		@Override
		public String getLabel() {
			return EntityUtils.toString(entity, attrNames);
		}

		@Override
		public Map<String, String> getAttributes() {
			return null;
		}

		/**
		 * @return the entity
		 */
		public EntityI<?> getEntity() {
			return entity;
		}

		/**
		 * @param entity the entity to set
		 */
		public void setEntity(EntityI<?> entity) {
			this.entity = entity;
		}
		
	}

	@SuppressWarnings("rawtypes") 
	public static EncoderSelectModel<EntityI<?>> create(ServiceI service, QueryConstraints qc, String... attrNames) {
		EncoderSelectModel<EntityI<?>> em = create(service, attrNames);
		em.constraints = qc;
		return em;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static EncoderSelectModel<EntityI<?>> create(ServiceI service, String... attrNames) {
		try {
			EncoderSelectModel<EntityI<?>> em = new EncoderSelectModel<EntityI<?>>();
			em.setService(service);
			em.setAttrNames(attrNames);
			return em;
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	/**
	 * @return the sortType
	 */
	public SortType getSortType() {
		return sortType;
	}

	/**
	 * @param sortType the sortType to set. Must not be null. Default is {@link SortType#LABEL}.
	 */
	public void setSortType(SortType sortType) {
		if(sortType == null) {
			throw new IllegalArgumentException("'sortType' parameter must not be null");
		}
		this.sortType = sortType;
	}

	/**
	 * @return the sortDirection
	 */
	public SortDirection getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection direction for to set. If set, data will be sorted using given type (either value or label). If null, no special sorting is performed (returned in order of SQL data).
	 * @see #getSortType()
	 */
	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}
}
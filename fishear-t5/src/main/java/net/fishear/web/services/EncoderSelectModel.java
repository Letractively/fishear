package net.fishear.web.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.EntityUtils;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.util.AbstractSelectModel;

public class EncoderSelectModel<T extends EntityI<?>> implements ValueEncoder<T>//, MapEncoder<T>
{

	private ServiceI<T> service;

	private String[] attrNames;

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

	public class DataModel extends AbstractSelectModel implements SelectModel {

		@Override
		public List<OptionGroupModel> getOptionGroups() {
			return null;
		}

		@Override
		public List<OptionModel> getOptions() {
			List<OptionModel> list = new ArrayList<OptionModel>();
			for (T t : service.list(null)) {
				Option opt = new Option(t);
				list.add(opt);
			}
			return list;
		}
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

	public static EncoderSelectModel<EntityI<?>> create(ServiceI<EntityI<?>> service, String... attrNames) {
		try {
			EncoderSelectModel<EntityI<?>> em = new EncoderSelectModel<EntityI<?>>();
			em.setService(service);
			em.setAttrNames(attrNames);
			return em;
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
}
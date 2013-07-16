package net.fishear.web.t5commons.components;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ListDataHolder;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.Coercions;
import net.fishear.utils.Texts;
import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.annotations.Inject;

@SupportsInformalParameters
public class 
	Combobox<T> 
extends 
	ComponentBase 
{

	@Parameter(principal = true, autoconnect = true, required = true, allowNull = true)
	private EntityI<T> value;

	@Persist
	private T key;

	private String[] columns;

	@Parameter(required=true, allowNull=false)
	private ServiceI<?> service;

//	@Persist
//	private ServiceI<?> service_;
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL) 
	@Property
	String onchange;

	@SetupRender
	void setupRenser() {
//		if(service != null) {
//			service = service;
//		}
		String cols = crsc.getInformalParameter("columns", String.class);
		if(cols != null && cols.trim().length() > 0) {
			this.columns = Texts.trimAll(cols.split(","), ""); 
		}
		this.key = value == null ? null : value.getId();
	}

	@SuppressWarnings("unchecked")
	public Map<T, String> getDataMap() {
		ListDataHolder<EntityI<T>, T> holder = null;
		if(holder == null) {
			holder = new ListDataHolder<EntityI<T>, T>((ServiceI<EntityI<T>>) service, columns); 
		}
		return (Map<T, String>) holder.getIdValueMap();
	}

	/**
	 * @return the service
	 */
	public ServiceI<?> getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceI<?> service) {
		this.service = service;
	}

	public EntityI<T> getValue() {
		return value;
	}

	public void setValue(EntityI<T> entity) {
		value = entity;
		key = entity == null ? null : entity.getId();
	}

	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String... columns) {
		this.columns = columns;
	}

	/**
	 * @return the key
	 */
	public T getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	@SuppressWarnings("unchecked")
	public void setKey(T key) {
		setValue(key == null ? null : (EntityI<T>) service.read(Coercions.convertType(key, findType())));
	}

	private Class<?> findType() {
		Class<?> clazz = this.getClass();
		while(clazz != Object.class) {
			Object gscl = clazz.getGenericSuperclass();
			if(ParameterizedType.class.isAssignableFrom(gscl.getClass())) {
				ParameterizedType pt = (ParameterizedType)gscl;
				Object[] oa = pt.getActualTypeArguments();
				if(oa != null && oa.length > 0) {
					return (Class<?>)oa[0];
				}
			}
			clazz = clazz.getSuperclass();
		}
		return (Class<?>) Long.class;
	}
}

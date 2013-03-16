package net.fishear.data.audit.services;

import java.util.Hashtable;
import java.util.List;

import net.fishear.data.audit.entities.AuditEntity;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.GenericService;

public class 
	AuditedEntityService 
extends 
	GenericService<AuditEntity> 
{
	
	private static Hashtable<String, AuditEntity> CACHE = new Hashtable<String, AuditEntity>();

	public AuditEntity getOrCreate(Class<?> entityClass) {

		return getOrCreate(entityClass.getName());

	}

	/**
	 * gets id for given class name. If class does nor exist in database, store it.
	 * Classes are cached in memory.
	 * @param className
	 * @return
	 */
	private AuditEntity getOrCreate(String className) {

		AuditEntity ae;
		if((ae = CACHE.get(className)) != null) {
			return ae;
		}

		QueryConstraints qc = QueryFactory.equals("className", className);
		List<AuditEntity> list = list(qc);
		if(list.size() == 0) {
			ae = new AuditEntity();
			ae.setClassName(className);
			ae.setId(className.hashCode());
			save(ae);
			getDao().flush();
			CACHE.put(className, ae);
		} else if (list.size() > 1){
			throw new IllegalStateException("More records exist for class name. Possible database content error.");
		} else {
			ae = list.get(0);
			CACHE.put(className, ae);
		}
		return ae;
	}
}

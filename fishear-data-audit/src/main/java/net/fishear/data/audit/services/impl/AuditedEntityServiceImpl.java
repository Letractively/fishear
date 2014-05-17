package net.fishear.data.audit.services.impl;

import java.util.Hashtable;
import java.util.List;

import net.fishear.data.audit.entities.AuditedEntity;
import net.fishear.data.audit.services.AuditedEntityService;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.GenericService;

public class 
	AuditedEntityServiceImpl 
extends 
	GenericService<AuditedEntity>
implements
	AuditedEntityService
{
	
	private static Hashtable<String, AuditedEntity> CACHE = new Hashtable<String, AuditedEntity>();

	public AuditedEntity getOrCreate(Class<?> audEntityClass) {

		return getOrCreate(audEntityClass.getName());

	}

	/**
	 * gets id for given class name. If class does nor exist in database, store it.
	 * Classes are cached in memory.
	 * @param className
	 * @return
	 */
	public AuditedEntity getOrCreate(String className) {

		AuditedEntity ae;
		if((ae = CACHE.get(className)) != null) {
			return ae;
		}

		QueryConstraints qc = QueryFactory.equals("className", className);
		List<AuditedEntity> list = list(qc);
		if(list.size() == 0) {
			ae = new AuditedEntity();
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

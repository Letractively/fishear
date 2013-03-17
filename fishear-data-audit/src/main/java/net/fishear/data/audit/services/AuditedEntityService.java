package net.fishear.data.audit.services;

import net.fishear.data.audit.entities.AuditedEntity;
import net.fishear.data.generic.services.ServiceI;

public interface AuditedEntityService extends ServiceI<AuditedEntity> {

	AuditedEntity getOrCreate(Class<?> entityClass);

	AuditedEntity getOrCreate(String className);

}

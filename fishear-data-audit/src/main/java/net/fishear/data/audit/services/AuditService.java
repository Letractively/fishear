package net.fishear.data.audit.services;

import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;

public class AuditService {

	static class Change {
		String propertyName;
		Object value;
	}

	public void auditChanges(EntityI<?> entity, ServiceI<EntityI<?>> service) {
		List<Change> changeList;
	}
	
	
}

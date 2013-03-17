package net.fishear.data;

public class FishearDataConstants {

	
	/**
	 * classes that are present in different modules which may, but need not be presented on classpath.
	 * Presention of those classs is tested using reflection and nehavior is changed depending them presence.
	 * 
	 * @author ffyxrr
	 *
	 */
	public static class Classes {

		public static final String AUDIT_SERVICE       = "net.fishear.data.audit.services.AuditService";
		
		public static final String AUDIT_SERVICE_IMPL  = "net.fishear.data.audit.services.impl.AuditServiceImpl";
		
		public static final String AUDIT               = "net.fishear.data.audit.entities.Audit";
		
		public static final String AUDIT_CHANGE        = "net.fishear.data.audit.entities.AuditChange";
		
		public static final String AUDIT_ENTITY        = "net.fishear.data.audit.entities.AuditedEntity";
	}

}

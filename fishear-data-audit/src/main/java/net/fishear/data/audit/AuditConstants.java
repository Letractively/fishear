package net.fishear.data.audit;

import net.fishear.data.generic.DataConstants;

public class AuditConstants {

	
	public static final String AUDIT_HEADERS_TABLE_NAME = DataConstants.FISHEAR_OBJECTS_PREFIX + "AUDITS"; 
	
	public static final String AUDIT_CHANGES_TABLE_NAME = DataConstants.FISHEAR_OBJECTS_PREFIX + "AUDIT_CHANGES"; 

	public static final String AUDIT_ENTITYIES_TABLE_NAME = DataConstants.FISHEAR_OBJECTS_PREFIX + "AUDIT_ENTITIES"; 

	public static final String AUDIT_COLUMN_ACTION_USER = "ACTION_USER"; 

	public static final String AUDIT_COLUMN_ACTION_DATE = "ACTION_DATE"; 
}

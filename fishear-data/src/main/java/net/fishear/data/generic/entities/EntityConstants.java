package net.fishear.data.generic.entities;

public class EntityConstants
{

	public static final String IDGEN_NAME = "fishearIdGen";
	public static final String IDGEN_TABLE = "FE_SEQGEN";
	public static final String IDGEN_PK_NAME = "GEN_KEY";
	public static final String IDGEN_COLUMN = "GEN_VALUE";
	public static final String IDGEN_COL_VALUE = "ENTITY_ID";

	/**
	 * name of column with create user ID 
	 */
	public static final String STDCOL_CREATE_USER = "CREATE_USER";
	
	/**
	 * name of column with create date
	 */
	public static final String STDCOL_CREATE_DATE = "CREATE_DATE";

	/**
	 * name of column with update user ID 
	 */
	public static final String STDCOL_UPDATE_USER = "UPDATE_USER";
	
	/**
	 * name of column with validity update date
	 */
	public static final String STDCOL_UPDATE_DATE = "UPDATE_DATE";

	/**
	 * name of column with validity begin
	 */
	public static final String STDCOL_VALID_FROM = "VALID_FROM";
	
	/**
	 * name of column with validity end
	 */
	public static final String STDCOL_VALID_TO = "VALID_TO";

	/**
	 * size of database columns that keep uder ID
	 */
	public static final int USERID_LENGTH = 64;
	
}

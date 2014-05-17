package net.fishear.data.generic.query.results;

public class SqlProjectionItem extends ProjectionItem {

	private String sql;
	
	private String[] aliases;
	
	public SqlProjectionItem() {
		super(Type.SQL);
	}
	

	private ProjectionType[] types;
	
	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}


	/**
	 * @return the aliases
	 */
	public String[] getAliases() {
		return aliases;
	}


	/**
	 * @param aliases the aliases to set
	 */
	public void setAliases(String... aliases) {
		this.aliases = aliases;
	}

	/**
	 * @return the types
	 */
	public ProjectionType[] getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(ProjectionType... types) {
		this.types = types;
	}
	
	
}

package net.fishear.data.generic.query.results;

/**
 * One projection item.
 * 
 * class may be instantiate from package only.
 * 
 * @author terber
 *
 */
public class 
	ProjectionItem
implements
	Cloneable
{

	public enum Type {
		DISTINCT,
		PROPERTY,
		SQL,
		GROUP,
		MAX,
		MIN,
		SUM,
		COUNT,
		COUNTDISTINCT,
		AVG,
		ROWCOUNT
	}

	private String propertyName;
	
	private final Type type;
	
	private String alias;

	protected ProjectionItem(Type type) {
		this.type = type;
	}

	public static ProjectionItem distinct(String propertyName) {
		ProjectionItem pIt = new ProjectionItem(Type.DISTINCT);
		pIt.setPropertyName(propertyName);
		return pIt;
	}

	public static ProjectionItem create(String propertyName, Type type) {
		if(type == Type.SQL) {
			throw new IllegalArgumentException(String.format("Projection type %s cannot be created by this call. ", type));
		}
		ProjectionItem pIt = new ProjectionItem(type);
		pIt.setPropertyName(propertyName);
		return pIt;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Type getType() {
		return type;
	}

	public ProjectionItem as(String alias) {
		setAlias(alias);
		return this;
	}

	public String toString() {
		return type + " " + propertyName;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
}

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
	Projection
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

	protected Projection(Type type) {
		this.type = type;
	}

	public static Projection distinct(String propertyName) {
		Projection pIt = new Projection(Type.DISTINCT);
		pIt.setPropertyName(propertyName);
		return pIt;
	}

	public static Projection create(String propertyName, Type type) {
		if(type == Type.SQL) {
			throw new IllegalArgumentException(String.format("Projections type %s cannot be created by this call. ", type));
		}
		Projection pIt = new Projection(type);
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

	public Projection as(String alias) {
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

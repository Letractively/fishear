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
		GROUP
	}

	private String propertyName;
	
	private final Type type;

	protected ProjectionItem(Type type) {
		this.type = type;
	}

	public static ProjectionItem distinct(String propertyName) {
		ProjectionItem pIt = new ProjectionItem(Type.DISTINCT);
		pIt.setPropertyName(propertyName);
		return pIt;
	}

	static ProjectionItem create(String propertyName, Type type) {
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

	public String toString() {
		return type + " " + propertyName;
	}
}

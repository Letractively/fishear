package net.fishear.data.generic.query.results;

public class 
	ProjectionItem
implements
	Cloneable
{

	public enum Type {
		DISTINCT
	}

	private String propertyName;
	
	private final Type type;
	
	private ProjectionItem(Type type) {
		this.type = type;
	}

	public static ProjectionItem distinct(String propertyName) {
		ProjectionItem pIt = new ProjectionItem(Type.DISTINCT);
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

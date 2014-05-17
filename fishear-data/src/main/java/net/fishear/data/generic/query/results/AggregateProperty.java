package net.fishear.data.generic.query.results;

public class 
	AggregateProperty
implements
	Cloneable
{

	private Functions function;
	private String propertyName;

	public AggregateProperty(String propertyName, Functions function) {
		this.function = function;
		this.propertyName = propertyName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		AggregateProperty that = (AggregateProperty) o;

		if (function != that.function)
			return false;
		if (propertyName != null ? !propertyName.equals(that.propertyName)
				: that.propertyName != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (function != null ? function.hashCode() : 0);
		result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
		return result;
	}

	public Functions getFunction() {
		return function;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public String toString() {
		return function + "(" + propertyName + ")";
	}
}

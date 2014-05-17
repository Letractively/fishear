package net.fishear.data.generic.query.order;


public class SortedProperty {

    private String propertyName;
    private SortDirection sortDirection;

    public SortedProperty() {
    }

    public SortedProperty(String propName) {
    	setPropertyName(propName);
    }

    public SortedProperty(String propName, SortDirection sortDirection) {
    	setPropertyName(propName);
    	setSortDirection(sortDirection);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortedProperty that = (SortedProperty) o;

        if (propertyName != null ?
                !propertyName.equals(that.propertyName) :
                that.propertyName != null)
            return false;
        if (sortDirection != that.sortDirection) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        return result;
    }

    /**
     * Return property name.
     *
     * @return
     */
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    /**
     * Return sorting direction.
     *
     * @return
     */
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
    
    public String toString() {
    	return propertyName + " " + (sortDirection == null ? "" : sortDirection.toString());
    }
}

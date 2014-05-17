package net.fishear.data.generic.query.order;

import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.utils.Defender;



public class 
	OrderBy 
extends 
	AbstractQueryPart 
implements
	Cloneable
{

    /**
     * todo: this class belongs under Results class or no?
     */

    private List<SortedProperty> sortedProperties;

    public OrderBy() {
        sortedProperties = new ArrayList<SortedProperty>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderBy orderBy = (OrderBy) o;

        if (!sortedProperties.equals(orderBy.sortedProperties)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return sortedProperties.hashCode();
    }

    /**
     * Add sorting by property value in given direction.
     *
     * @param propertyName  Sorted property name.
     * @param direction     Sorting direction.
     * @return <code>this</code>
     * @throws IllegalArgumentException When property name is null of empty.
     */
    public OrderBy add(String propertyName, SortDirection direction) {
        Defender.notNullOrEmpty(propertyName, "propertyName");

        SortedProperty sortedProperty = new SortedProperty();
        sortedProperty.setPropertyName(propertyName);
        sortedProperty.setSortDirection(direction);

        return add(sortedProperty);
    }

    /**
     * Add property name and sorting direction pair.
     *
     * @param sortedProperty AppProperty name and sorting direction pair.
     * @return <code>this</code>
     */
    public OrderBy add(SortedProperty sortedProperty) {
        sortedProperties.add(sortedProperty);
        return this;
    }

    /**
     * List of all sorted properties and sorting directions.
     */
    public List<SortedProperty> getSortedProperties() {
        return sortedProperties;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (SortedProperty sp : sortedProperties) {
    		if(sb.length() > 0) { sb.append(", "); }
			sb.append(sp.toString());
		}
    	return sb.toString();
    }
}

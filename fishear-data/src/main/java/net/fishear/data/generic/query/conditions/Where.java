package net.fishear.data.generic.query.conditions;

import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.data.generic.query.restrictions.Restrictions;

public class 
	Where 
extends 
	AbstractQueryPart 
implements
	Cloneable
{

    private Conditions conditions;

    public Where() {

    }

    public Where(Conditions conditions) {
        setConditions(conditions);
    }

    /** sets the condition part of Where clause.
     * @param conditions must not be null
     * @throws IllegalArgumentException if 'conditions' is null
     */
    public void setConditions(Conditions conditions) {
    	if(conditions == null) {
    		throw new IllegalArgumentException("Argument 'conditions' must not be null");
    	}
    	this.conditions = conditions;
    }

    /** returns the condition part of Where clause.
     * Method is synonym to {@link #conditions()} method and it is provided to JavaBean compatibility.
     */
    public Conditions getConditions() {
    	if(conditions == null) {
    		conditions = new Conditions();
    	}
    	return this.conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return eq(conditions, ((Where) o).conditions);
	}

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (conditions == null ? 0 : conditions.hashCode());
        return result;
    }

    public boolean isConditionSet() {
    	return conditions != null;
    }

    /**
     * Return object representing conditions in query.
     */
    public Conditions conditions() {
    	if(conditions == null) {
    		conditions = new Conditions();
    	}
    	return conditions;
    }

    /** returns restriction of where condition, if it is set. 
     * Returns null if no restriction.
     */
	public Restrictions getRootRestriction() {
		if(conditions != null) {
			return conditions.getRootRestriction();
		}
		return null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if((conditions != null && conditions.getRootRestriction() != null) ||(conditions != null && conditions.getNestedRestrictions() != null)) {
			sb.append(sb.length() > 0 ? " AND " : "");
			sb.append(conditions.toString());
		}
		return sb.toString();
	}
}

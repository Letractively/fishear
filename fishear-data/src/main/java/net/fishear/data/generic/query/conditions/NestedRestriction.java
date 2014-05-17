package net.fishear.data.generic.query.conditions;

import net.fishear.data.generic.query.restrictions.Restrictions;

public class 
	NestedRestriction 
implements
	Cloneable
{
    private String propertyName;
    private Restrictions restrictions;

    public NestedRestriction(String propertyName, Restrictions restrictions) {
        this.propertyName = propertyName;
        this.restrictions = restrictions;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("Nested: ").append(propertyName).append(" ").append(restrictions.toString());
    	
    	return sb.toString();
    }
    
}

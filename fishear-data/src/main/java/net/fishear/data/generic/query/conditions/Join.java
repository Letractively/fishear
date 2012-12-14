package net.fishear.data.generic.query.conditions;

import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.data.generic.query.restrictions.Restrictions;

public class 
	Join 
extends 
	AbstractQueryPart 
implements
	Cloneable
{

    private String propertyName;
    private JoinType joinType;

    private Restrictions restrictions;
	private String alias;

    public Join(JoinType joinType, String propertyName, Restrictions restrictions) {
        this.restrictions = restrictions == null ? Restrictions.TRUE : restrictions;
        this.joinType = joinType;
        this.propertyName = propertyName.trim();
        if(this.propertyName.length() == 0) {
        	throw new IllegalStateException("'propertyName' argument is mandatory");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Join join = (Join) o;

        if (joinType != join.joinType) return false;
        if (!propertyName.equals(join.propertyName)) return false;
        if (!restrictions.equals(join.restrictions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = propertyName.hashCode();
        result = 31 * result + joinType.hashCode();
        result = 31 * result + restrictions.hashCode();
        return result;
    }

    /**
     * Join type.
     *
     * @see JoinTypes
     * @return Join type.
     */
    public JoinType getJoinType() {
        return joinType;
    }

    /**
     * Joined property.
     *
     * @return AppProperty name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Joined restrictions.
     *
     * @see Restrictions
     * @return Restrictions
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
    	if(restrictions == null) {
    		throw new NullPointerException("'restrictions' must not be null");
    	}
        this.restrictions = restrictions;
    }

    public String toString() {
    	return joinType + " JOIN " + propertyName + (alias == null ? "" : " ".concat(alias)) + " ON " + restrictions.toString(); 
    }

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public enum JoinType {
		OUTER,
		INNER,
		FULL
	}
	
}

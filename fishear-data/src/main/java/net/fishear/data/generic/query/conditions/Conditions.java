package net.fishear.data.generic.query.conditions;


import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.data.generic.query.conditions.Join.JoinType;
import net.fishear.data.generic.query.restrictions.Restrictions;

import org.apache.commons.lang.ObjectUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class 
	Conditions 
extends 
	AbstractQueryPart 
implements
	Cloneable
{

	/**
	 * Restrictions has composite structure and this is root restriction.
	 */
	private Restrictions rootRestriction;

	/**
	 * List of joined selections and contraints.
	 */
	private List<Join> joins;

    private List<NestedRestriction> nestedRestrictions;

	public Conditions() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Conditions that = (Conditions) o;
		if (!ObjectUtils.equals(joins, that.joins)) {
			return false;
		}
		if (rootRestriction != null ? !rootRestriction.equals(that.rootRestriction) : that.rootRestriction != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (rootRestriction != null ? rootRestriction.hashCode() : 0);
		result = 31 * result + ObjectUtils.hashCode(joins);
		return result;
	}

    public Conditions add(NestedRestriction nestedRestriction) {
        if (nestedRestrictions == null) {
			nestedRestrictions = new ArrayList<NestedRestriction>();
        }
        nestedRestrictions.add(nestedRestriction);
		return this;
    }

	/**
	 * Add restriction to database query.
	 * 
	 * @param restriction
	 * @return Return this for method chaining.
	 */
	public Conditions add(Restrictions restriction) {
		if (rootRestriction != null) {
			rootRestriction = Restrictions.and(rootRestriction, restriction);
		} else {
			rootRestriction = restriction;
		}
		return this;
	}

	/**
	 * Add joined restrictions for given property.
	 * 
	 * @param joinPropertyName
	 *            Name of join property.
	 * @param restriction restrictions. May be null = no restriction is expected.
	 * @return Return this for method chaining.
	 */
	public Conditions join(String joinPropertyName, Restrictions restriction) {

		if(joinExists(joinPropertyName)) {
			return this;
		}

		Join join = new Join(JoinType.OUTER, joinPropertyName, restriction);
		getJoins().add(join);
		return this;
	}
	
	
	public boolean joinExists(String joinPropertyName) {
		if(joinPropertyName == null || joinPropertyName.trim().length() == 0 || joins == null || joins.size() == 0) {
			return false;
		}
		for(Join j : joins) {
			if(joinPropertyName.equals(j.getPropertyName())) {
				return true;
			}
		}
		return false;
	}

	public List<Join> getJoins(String joinPropertyName) {
		ArrayList<Join> list = new ArrayList<Join>();
		if(joinPropertyName == null || joinPropertyName.trim().length() == 0 || joins == null || joins.size() == 0) {
			return list;
		}
		for(Join j : joins) {
			if(joinPropertyName.equals(j.getPropertyName())) {
				list.add(j);
			}
		}
		return list;
	}

	/**
	 * Add joined restrictions for given property.
	 * 
	 * @param joinProperty
	 *            Name of join property.
	 * @param restriction restrictions. May be null = no restriction is expected.
	 * @param joinType the type of join
	 * @return Return this for method chaining.
	 */
	public Conditions join(String joinProperty, JoinType joinType, Restrictions restriction) {

		Join join = new Join(joinType, joinProperty, restriction);
		getJoins().add(join);
		return this;
	}

	/**
	 * Add joined restrictions for given property.
	 * 
	 * @param joinProperty
	 *            Name of join property.
	 * @param alias The alias to assign to the joined association (for later reference).
	 * @param restriction restrictions. May be null = no restriction is expected.
	 * @return Return this for method chaining.
	 */
	public Conditions join(String joinProperty, String alias, Restrictions restriction) {

		Join join = new Join(JoinType.OUTER, joinProperty, restriction);
		join.setAlias(alias);
		getJoins().add(join);
		return this;
	}

	/**
	 * Add joined restrictions for given property.
	 * 
	 * @param joinProperty
	 *            Name of join property.
	 * @param alias The alias to assign to the joined association (for later reference).
	 * @param restriction restrictions. May be null = no restriction is expected.
	 * @param joinType type of the join
	 * @return Return this for method chaining.
	 */
	public Conditions join(String joinProperty, JoinType joinType, String alias, Restrictions restriction) {

		Join join = new Join(joinType, joinProperty, restriction);
		join.setAlias(alias);
		getJoins().add(join);
		return this;
	}

	/**
	 * Return root restriction.
	 * 
	 * @return Return root restriction. If is not set, return null!
	 */
	public Restrictions getRootRestriction() {
		return rootRestriction;
	}

	public List<Join> getJoins() {
		if (joins == null) {
			joins = new ArrayList<Join>();
		}
		return joins;
	}
//
//	/**
//	 * Return all joined restrictions.
//	 * 
//	 * @return
//	 */
//	public List<Join> getOuterJoins() {
//		return getJoins();
//	}

	/**
	 * adds AND condition to previously added conditions, but only if 'value' is
	 * not null nor empty string.
	 * 
	 * @param fldName
	 *            name of attribute in this class
	 * @param value
	 *            value - it is tested and in case it is not empty it is added.
	 * @return true if condition was added, false in case condition was not
	 *         changed.
	 */
	public boolean addEqualsNotEmpty(String fldName, String value) {
		if (value != null && value.length() > 0) {
			add(Restrictions.like(fldName, value));
			return true;
		}
		return false;
	}

	/**
	 * adds AND condition to previously added conditions (if any), but only if
	 * 'value' is not null nor empty string.
	 * 
	 * @param fldName
	 *            name of attribute in this class
	 * @param value
	 *            value - it is tested and in case it is not empty it is added.
	 * @return true if condition was added, false in case condition was not
	 *         changed.
	 */
	public boolean addLikeNotEmpty(String fldName, String value) {
		if (value != null && value.length() > 0) {
			add(Restrictions.like(fldName, "%" + value + "%"));
			return true;
		}
		return false;
	}

	/**
	 * adds AND condition to previously added conditions, but only if 'value' is
	 * not zero.
	 * 
	 * @param fldName
	 *            name of attribute in this class
	 * @param value
	 *            value - it is tested and in case it is not empty it is added.
	 * @return true if condition was added, false in case condition was not
	 *         changed.
	 */
	public boolean addNotZero(String fldName, long value) {
		if (value != 0) {
			add(Restrictions.equal(fldName, new Long(value)));
			return true;
		}
		return false;
	}

	/**
	 * adds AND condition to previously added conditions, but only if 'value' is
	 * not zero.
	 * 
	 * @param fldName
	 *            name of attribute in this class
	 * @param value
	 *            value - it is tested and in case it is not empty it is added.
	 * @return true if condition was added, false in case condition was not
	 *         changed.
	 */
	public boolean addNan(String fldName, double value) {
		if (!Double.isNaN(value)) {
			add(Restrictions.equal(fldName, new Double(value)));
			return true;
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		String cln = getClass().getName();

		if(joins != null && joins.size() > 0) {
			for (Join j : joins) {
				sb.append(j.toString());
			}
		}

		if(nestedRestrictions != null && nestedRestrictions.size() > 0) {
			for (NestedRestriction nr : nestedRestrictions) {
				sb.append(nr.toString());
			}
		}

		sb.append(cln.substring(cln.lastIndexOf('.') + 1)).append(": ");
		sb.append(rootRestriction == null ? "(no root restriction)" : rootRestriction.toString());

		return sb.toString();
	}

    public List<NestedRestriction> getNestedRestrictions() {
        if (nestedRestrictions == null) {
            return Collections.emptyList();
        }
        return nestedRestrictions;
    }

	/** shortcut to add equals restriction.
	 * Checks if the 'value' is null. If so, creates 'isNull' restriction, otherwise equals restriction. 
	 * @param fldName
	 * @param value
	 * @return 
	 */
	public Conditions addEquals(String fldName, Object value) {
		if(value == null) {
			return add(Restrictions.isNull(fldName));
		} else {
			return add(Restrictions.equal(fldName, value));
		}
	}

	/** adds new join if such does not exist, or adds restriction to existing join if already exists.
	 * @param propertyName the property that is joined to
	 * @param rootRestriction the restrixction
	 */
	public Conditions addJoin(String propertyName, Restrictions joiningRestriction) {
		List<Join> list = getJoins(propertyName);
		if(list.size() > 0) {
			list.get(0).setRestrictions(Restrictions.and(list.get(0).getRestrictions(), joiningRestriction));
		} else {
			join(propertyName, joiningRestriction);
		}
		return this;
	}
	
	/**
	 * @return true in case no join, no restriction ... etc is set = SQL equivalent is WHERE (1=1).
	 * If returns false, any of element is set.
	 */
	public boolean isEmpty() {
		return rootRestriction == null && (nestedRestrictions == null || nestedRestrictions.size() == 0) && (joins == null || joins.size() == 0);
	}
	
}

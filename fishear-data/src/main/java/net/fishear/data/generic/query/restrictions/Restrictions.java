package net.fishear.data.generic.query.restrictions;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.utils.Texts;

public abstract class 
	Restrictions 
extends 
	AbstractQueryPart 
implements
	Cloneable
{
	
	/**
	 * the "virtual" restriction (translated to SQLM part 1=1) suit sor situations where restriction is required, but not really need.
	 */
	public static final Restrictions TRUE = Restrictions.sql("1=1");

	/**
	 * the "virtual" restriction (translated to SQLM part 1=1) suit sor situations where restriction is required, but not really need.
	 */
	public static final Restrictions FALSE = Restrictions.sql("1=2");

	public Restrictions() {

	}

    public static Restrictions greaterThan(String propertyName, Object value) {
        return new Expression(ExpressionTypes.GREATER, propertyName, value);
    }

    public static Restrictions greaterOrEqualThan(String propertyName, Object value) {
        return new Expression(ExpressionTypes.GREATER_OR_EQUAL, propertyName, value);
    }

    public static Restrictions lessThan(String propertyName, Object value) {
        return new Expression(ExpressionTypes.LESS, propertyName, value);
    }

    public static Restrictions lessOrEqualThan(String propertyName, Object value) {
        return new Expression(ExpressionTypes.LESS_OR_EQUAL, propertyName, value);
    }

    public static Restrictions like(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE, propertyName, expression);
    }

    /** 
     * adds like that is open towards to the end.
     * 
     * @param propertyName the property name
     * @param expression liked string
     * @return
     */
    public static Restrictions likeEnd(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_END, propertyName, expression);
    }

    /** 
     * adds like that is open towards to the begin.
     * 
     * @param propertyName the property name
     * @param expression liked string
     * @return
     */
    public static Restrictions likeStart(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_START, propertyName, expression);
    }

    /** 
     * adds like that is open axactly as typed.
     * 
     * @param propertyName the property name
     * @param expression liked string
     * @return
     */
    public static Restrictions likeExact(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_EXACT, propertyName, expression);
    }

    private static Restrictions toLike(String propertyName, String s) {
    	if(s.length() > 2) {
	    	if(s.startsWith("!") && s.endsWith("!")) {
	    		return likeExact(propertyName, s.substring(1, s.length() - 1));
	    	} else if(s.startsWith("!")) {
	    		return likeStart(propertyName, s.substring(1));
	    	} else if (s.endsWith("!")) {
	    		return likeEnd(propertyName, s.substring(0, s.length() - 1));
	    	}
    	}
		return Restrictions.like(propertyName, s);
	}

	/**
     * merges all restrictions in list by "OR".
     * 
     * @param rlist the list 
     * @return the restriction joined by OR function
     */
    private static Restrictions toOr(List<Restrictions> rlist) {
    	if(rlist == null || rlist.size() == 0) {
    		return Restrictions.TRUE;
    	}
		if(rlist.size() > 1) {
			return Restrictions.or(rlist.get(0), rlist.subList(1, rlist.size()).toArray(new Restrictions[rlist.size()]));
		} else {
			return rlist.get(0);
		}
    }
    
    /**
     * merges all restrictions in list by "OR".
     * 
     * @param rlist the list 
     * @return the restriction joined by OR function
     */
    private static Restrictions toAnd(List<Restrictions> rlist) {
    	if(rlist == null || rlist.size() == 0) {
    		return Restrictions.TRUE;
    	}
		if(rlist.size() > 1) {
			return Restrictions.and(rlist.get(0), rlist.subList(1, rlist.size()).toArray(new Restrictions[rlist.size()]));
		} else {
			return rlist.get(0);
		}
    }

    /**
     * generates and returns likeAll for more entity properties.
     * All particular restrictions (generated for every property) are connected by OR.
     * 
     * @param searchedString the string that is searched for
     * @param propertyNames list of property names that the {@link #likeAll(String, String)} is constructed for
     * @return new restriction
     */
    public static Restrictions searchAll(String searchedString, String... propertyNames) {
		List<Restrictions> rlist = new ArrayList<Restrictions>();
    	for(String prop : propertyNames) {
    		rlist.add(likeAll(prop, searchedString));
    	}
    	return toOr(rlist);
    }
    
    /**
     * generates and returns likeAny for more properties.
     * All particular restrictions (generated for every property) are connected by OR.
     * 
     * @param searchedString the string that is searched for
     * @param propertyNames list of property names that the {@link #likeAny(String, String)} is constructed for
     * @return new restriction
     */
    public static Restrictions searchAny(String searchedString, String... propertyNames) {
		List<Restrictions> rlist = new ArrayList<Restrictions>();
    	for(String prop : propertyNames) {
    		rlist.add(likeAny(prop, searchedString));
    	}
    	return toOr(rlist);
    }
    
    /** returns LIKE restrictions with OR parts in case the 'str' consist of more than one word (words are separated by whitespaces).
     * Result must match at least one those restriction.
     * In case 'str' is one word only, behaves like the {@link #like(String, String)} method.
     */
    public static Restrictions likeAny(String propertyName, String str) {
    	if(str != null && str.trim().equals(".")) {
    		return Restrictions.TRUE;
    	}
		List<Restrictions> rlist = new ArrayList<Restrictions>();
		String[] as = Texts.normalizeWhitespaces(str).split(" ");
		for (int i = 0; i < as.length; i++) {
			rlist.add(toLike(propertyName, as[i]));
		}
		return toOr(rlist);
    }

    /** returns combined LIKE restriction with AND parts case the 'str' consist of more than one word (words are separated by whitespaces).
     * Result must match all those restrictions.
     * In case 'str' is one word only, behaves like the {@link #like(String, String)} method.
     */
    public static Restrictions likeAll(String propertyName, String str) {
    	if(str != null && str.trim().equals(".")) {
    		return Restrictions.TRUE;
    	}
		List<Restrictions> rlist = new ArrayList<Restrictions>();
		String[] as = Texts.normalizeWhitespaces(str).split(" ");
		for (int i = 0; i < as.length; i++) {
			rlist.add(toLike(propertyName, as[i]));
		}
		return toAnd(rlist);
    }

    public static Restrictions equal(String propertyName, Object value) {
        return new Expression(ExpressionTypes.EQUAL, propertyName, value);
    }

    public static Restrictions notEqual(String propertyName, Object value) {
        return new Expression(ExpressionTypes.NOT_EQUAL, propertyName, value);
    }

    public static Restrictions equalProperty(String propertyName1, String propertyName2) {
        return new Expression(ExpressionTypes.EQUAL_PROPERTY, propertyName1, propertyName2);
    }

    public static Restrictions greaterThanProperty(String propertyName1, String propertyName2) {
        return new Expression(ExpressionTypes.GREATER_PROPERTY, propertyName1, propertyName2);
    }

    public static Restrictions greaterOrEqualThanProperty(String propertyName1, String propertyName2) {
        return new Expression(ExpressionTypes.GREATER_EQUAL_PROPERTY, propertyName1, propertyName2);
    }

    public static Restrictions lessThanProperty(String propertyName1, String propertyName2) {
        return new Expression(ExpressionTypes.LESS_PROPERTY, propertyName1, propertyName2);
    }

    public static Restrictions lessOrEqualThanProperty(String propertyName1, String propertyName2) {
        return new Expression(ExpressionTypes.LESS_EQUAL_PROPERTY, propertyName1, propertyName2);
    }

    public static Restrictions in(String propertyName, Object... value) {
        return new Expression(ExpressionTypes.IN, propertyName, value);
    }

    public static Restrictions in(String propertyName, Collection<?> value) {
        return new Expression(ExpressionTypes.IN, propertyName, value);
    }

    public static Restrictions isNull(String propertyName) {
        return new Expression(ExpressionTypes.IS_NULL, propertyName, null);
    }

	public static Restrictions isNotNull(String propertyName) {
		return new Expression(ExpressionTypes.IS_NOT_NULL, propertyName, null);
	}

	public static SubqueryExpression exists(String entityName, Restrictions subquery) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.EXISTS, entityName, subquery);
		return sqex;
	}

	public static SubqueryExpression exists(Class<?> entityClass, Restrictions subquery) {
		return exists(entityClass.getName(), subquery);
	}

	public static Restrictions exists(String entityName, String alias, Restrictions subquery) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.EXISTS, entityName, subquery);
		sqex.setAlias(alias);
		return sqex;
	}

	public static Restrictions exists(Class<?> entityClass, String alias, Restrictions subquery) {
		return exists(entityClass.getName(), alias, subquery);
	}

	public static SubqueryExpression exists(String entityName, Where where) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.EXISTS, entityName, where);
		return sqex;
	}

	public static SubqueryExpression exists(Class<?> entity, Where where) {
		return exists(entity.getName(), where);
	}

	public static Restrictions exists(Class<?> entityClass, String alias, Where where) {
		return exists(entityClass.getName(), alias, where);
	}

	public static Restrictions exists(String entityName, String alias, Where where) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.EXISTS, entityName, where);
		sqex.setAlias(alias);
		return sqex;
	}

	public static Restrictions notExists(String entityName, Restrictions subquery) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.NOT_EXISTS, entityName, subquery);
		return sqex;
	}

	public static Restrictions notExists(String entityName, String alias, Restrictions subquery) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.NOT_EXISTS, entityName, subquery);
		sqex.setAlias(alias);
		return sqex;
	}

	public static Restrictions notExists(Class<?> entityClass, String alias, Restrictions subquery) {
		return notExists(entityClass.getName(), alias, subquery);
	}

	public static Restrictions notExists(Class<?> entityClass, Restrictions subquery) {
		return notExists(entityClass.getName(), subquery);
	}

	public static Restrictions notExists(String entityName, Where where) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.NOT_EXISTS, entityName, where);
		return sqex;
	}

	public static Restrictions notExists(String entityName, String alias, Where where) {
		SubqueryExpression sqex = new SubqueryExpression(ExpressionTypes.NOT_EXISTS, entityName, where);
		sqex.setAlias(alias);
		return sqex;
	}

	public static Restrictions notExists(Class<?> entityClass, String alias, Where where) {
		return notExists(entityClass.getName(), alias, where);
	}

	public static Restrictions notExists(Class<?> entityClass, Where where) {
		return notExists(entityClass.getName(), where);
	}

	/**********************************/
    /***** Composite restrictions *****/
    /**********************************/

    /** creates between as (value GREAT lo AND value LESS hi).
     * If both 'lo' and 'hi' implement {@link Comparable}, compares values and in case lo > hi, swap values.
     * If any of 'lo' or 'hi' does not implement {@link Comparable}, leave values unchecked.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Restrictions betweenCheck(
            String propertyName, Object lo, Object hi) {
    	if(lo instanceof Comparable && hi instanceof Comparable) {
    		if(((Comparable)hi).compareTo((Comparable)lo) < 0) {
    			Object o = lo;
    			lo = hi;
    			hi = o;
    		};
    	}
    	return between(propertyName, lo, hi);
    }
    

    /**
     * if both 'lo' and 'hi' are passed, creates {@link #between(String, Object, Object)}. 
     * If 'hi' is null, creates value >= 'lo', if 'lo' is null, creates value <= 'hi'.
     * If both are null, do nothing (returns {@link #TRUE} ).
     * 
     * @param propertyName
     * @param lo lower value (may be null)
     * @param hi higher value (may be null)
     * @return restriction
     */
	public static Restrictions interval(String propertyName, Object lo, Object hi) {
    	if(lo == null && hi == null) {
    		return Restrictions.TRUE;
    	}
    	if(hi == null) {
    		return Restrictions.greaterOrEqualThan(propertyName, lo);
    	} else if (lo == null) {
    		return Restrictions.lessOrEqualThan(propertyName, hi);
    	} else {
	    	return between(propertyName, lo, hi);
    	}
    }

    /**
     * if both 'lo' and 'hi' are passed, creates {@link #between(String, Object, Object)}. 
     * If 'hi' is null, creates value >= 'lo', if 'lo' is null, creates value <= 'hi'.
     * If both are null, do nothing (returns {@link #TRUE} ).
     * 
     * @param property1Name
     * @param lo lower value (may be null)
     * @param hi higher value (may be null)
     * @return restriction
     */
	public static Restrictions overlap(String property1Name, String property2Name, Object lo, Object hi) {
    	if(lo == null && hi == null) {
    		return Restrictions.TRUE;
    	}
    	if(hi == null) {
    		return Restrictions.or(Restrictions.isNull(property1Name), Restrictions.greaterOrEqualThan(property1Name, lo));
    	} else if (lo == null) {
    		return Restrictions.or(Restrictions.isNull(property2Name), Restrictions.lessOrEqualThan(property2Name, hi));
    	} else {
    		return Restrictions.and(
   				Restrictions.or(Restrictions.isNull(property1Name), Restrictions.greaterOrEqualThan(property1Name, lo)),
   				Restrictions.or(Restrictions.isNull(property2Name), Restrictions.lessOrEqualThan(property2Name, hi))
   			);
    	}
    }

    public static Restrictions between(String propertyName, Object lo, Object hi) {
        return new Expression(ExpressionTypes.BETWEEN, propertyName, new Object[] {lo, hi});    	
//        Restrictions result = Restrictions.and(
//                Restrictions.greaterThan(propertyName, lo),
//                Restrictions.lessThan(propertyName, hi));
//        return result;
    }

    public static Restrictions and(Restrictions left, Restrictions... right) {
        return new Conjunction(ConjunctionTypes.AND, left, right);
    }

    public static Restrictions or(Restrictions left, Restrictions... right) {
        return new Conjunction(ConjunctionTypes.OR, left, right);
    }

    public static Restrictions not(Restrictions restriction) {
        return new Conjunction(ConjunctionTypes.NOT,restriction);
    }

    public static Restrictions sql(String sql) {
        return new Expression(ExpressionTypes.SQL_RESTICTION, null, sql);
    }

	protected String val_(Object val) {
		if(val instanceof CharSequence) {
			return "'"+val.toString()+"'";
		}
		return val == null ? "NULL" : val.toString();
	}

    public abstract String toString();

}

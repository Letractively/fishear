package net.fishear.data.generic.query.restrictions;


import java.util.ArrayList;
import java.util.Collection;
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

    public static Restrictions likeEnd(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_END, propertyName, expression);
    }

    public static Restrictions likeStart(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_START, propertyName, expression);
    }

    public static Restrictions likeExact(String propertyName, String expression) {
        return new Expression(ExpressionTypes.LIKE_EXACT, propertyName, expression);
    }

    /** returns combined LIKE restriction with AND parts case the 'str' consist of more than one word (words are separated by whitespaces).
     * Result must match all those restrictions.
     * In case 'str' is one word only, behaves like the {@link #like(String, String)} method.
     */
    public static Restrictions likeAll(String propertyName, String str) {
		str = Texts.normalizeWhitespaces(str);
		List<Restrictions> rlist = new ArrayList<Restrictions>();
		String[] as = Texts.normalizeWhitespaces(str).split(" ");
		for (int i = 0; i < as.length; i++) {
			rlist.add(Restrictions.like(propertyName, as[i]));
		}
		Restrictions left = rlist.get(0);
		rlist.remove(0);
		if(rlist.size() > 0) {
			return Restrictions.and(left, rlist.toArray(new Restrictions[rlist.size()]));
		} else {
			return left;
		}
    }
    
    /** returns LIKE restrictions with OR parts in case the 'str' consist of more than one word (words are separated by whitespaces).
     * Result must match at least one those restriction.
     * In case 'str' is one word only, behaves like the {@link #like(String, String)} method.
     */
    public static Restrictions likeAny(String propertyName, String str) {
		str = Texts.normalizeWhitespaces(str);
		List<Restrictions> rlist = new ArrayList<Restrictions>();
		String[] as = Texts.normalizeWhitespaces(str).split(" ");
		for (int i = 0; i < as.length; i++) {
			rlist.add(Restrictions.like(propertyName, as[i]));
		}
		Restrictions left = rlist.get(0);
		rlist.remove(0);
		if(rlist.size() > 0) {
			return Restrictions.or(left, rlist.toArray(new Restrictions[rlist.size()]));
		} else {
			return left;
		}
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

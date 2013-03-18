package net.fishear.data.hibernate.query;


import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.data.generic.query.restrictions.Conjunction;
import net.fishear.data.generic.query.restrictions.ConjunctionTypes;
import net.fishear.data.generic.query.restrictions.Expression;
import net.fishear.data.generic.query.restrictions.ExpressionTypes;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.query.restrictions.SubqueryExpression;
import net.fishear.utils.Globals;
import net.fishear.utils.Texts;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class 
	RestrictionsParser
extends 
	AbstractQueryParser<Restrictions, Criteria> 
{

    private static final Logger LOG = Globals.getLogger();

    public void parse(Restrictions sourceObject, Criteria output) {
        if (sourceObject != null) {
            Criterion criterion = parseRestriction(sourceObject);
            output.add(criterion);
        }
    }

    /**
     * This method is recursively called and compose result
     * <code>Criterion</code> for hibernate query.
     *
     * @param currentRes
     * @return
     *
     * @see Restrictions
     */
    private Criterion parseRestriction(Restrictions currentRes) {

    	log.trace("parsing restriction {}", currentRes);
        if (currentRes instanceof Conjunction) { // inner node (AND, OR, ...)
            Conjunction conjunction = (Conjunction) currentRes;
            return parseConjuction(conjunction);
        } else if (currentRes instanceof Expression) { // leaf node
            Expression expression = (Expression) currentRes;
            return parseExpression(expression);
        } else {
       		log.error("Cannot parse leaf query class {}", currentRes);
            throw new IllegalStateException(String.format("Restrictions must be instance of 'Expression' or 'Conjunction', but %s is not", currentRes.getClass().getName()));
        }
    }

    private Criterion parseConjuction(Conjunction conjunction) {

        Criterion leftRestricitions = parseRestriction(conjunction.getLeft());
        List<Criterion> criterias = new ArrayList<Criterion>(conjunction.getRight().length);
        for(Restrictions r : conjunction.getRight()) {
        	if(r != null) {
        		criterias.add(parseRestriction(r));
        	}
        }

        Criterion crit;
        switch (conjunction.getType()) {
            case AND:
                crit = leftRestricitions;
                for(Criterion cr : criterias) {
                    crit = org.hibernate.criterion.Restrictions.and(crit, cr);
                }
                return crit;
            case OR:
                crit = leftRestricitions;
                for(Criterion cr : criterias) {
                    crit = org.hibernate.criterion.Restrictions.and(crit, cr);
                }
                return crit;
            case NOT:
                return org.hibernate.criterion.Restrictions.not(leftRestricitions);
            default:
                ConjunctionTypes type = conjunction.getType();
                if (LOG.isErrorEnabled()) {
                    LOG.error("Query conjuction " + type.name() + " cannot be recognized!");
                }
                throw new IllegalStateException("Unknown conjunction type: " + type.name());
        }
    }

    private Criterion parseExpression(Expression expression) {

        ExpressionTypes type = expression.getType();
        String propertyName = expression.getTargetPropertyName();
        Object value = expression.getValue();
        Class<?> clazz;

        if(log.isTraceEnabled()) {
        	log.trace(String.format("Parsing expression of type '%s' for propety '%s', value=%s", type.name(), propertyName, value));
        }

        switch (type) {
            case EQUAL:
                return org.hibernate.criterion.Restrictions.eq(propertyName, value);
            case GREATER:
                return org.hibernate.criterion.Restrictions.gt(propertyName, value);
            case GREATER_OR_EQUAL:
                return org.hibernate.criterion.Restrictions.ge(propertyName, value);
            case LESS:
                return org.hibernate.criterion.Restrictions.lt(propertyName, value);
            case LESS_OR_EQUAL:
                return org.hibernate.criterion.Restrictions.le(propertyName, value);
            case LIKE:
            	String sval = (String)value;
            	if(sval.contains("%") || sval.contains("_")) {
                    return org.hibernate.criterion.Restrictions.ilike(propertyName, sval);
            	} else {
                    return org.hibernate.criterion.Restrictions.ilike(propertyName, sval, MatchMode.ANYWHERE);
            	}
            case LIKE_END:
                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.END);
            case LIKE_START:
                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.START);
            case LIKE_EXACT:
                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.EXACT);
            case BETWEEN:
                return org.hibernate.criterion.Restrictions.between(propertyName, ((Object[])value)[0], ((Object[])value)[1]);
            case IN:
            	clazz = value.getClass();
            	if(clazz.isArray()) {
                    return org.hibernate.criterion.Restrictions.in(propertyName, (Object[])value);
            	} else if(Collection.class.isAssignableFrom(clazz)) {
                    return org.hibernate.criterion.Restrictions.in(propertyName, (Collection<?>)value);
            	} else {
            		throw new IllegalStateException(
            			"Object for 'IN' criteria must be array or collection, but it is " +
            			(clazz == null ? "(null)" : "'"+clazz.getName()+"'")
            		);
            	}
            case IS_NULL:
                return org.hibernate.criterion.Restrictions.isNull(propertyName);
            case IS_NOT_NULL:
                return org.hibernate.criterion.Restrictions.isNotNull(propertyName);
            case NOT_EQUAL:
                return org.hibernate.criterion.Restrictions.ne(propertyName, value);
            case EXISTS:
            	return Subqueries.exists(subquery((SubqueryExpression) expression));
            case NOT_EXISTS:
            	return Subqueries.notExists(subquery((SubqueryExpression) expression));
            case EQUAL_PROPERTY:
            	if(!(value instanceof CharSequence)) {
            		throw new IllegalStateException(
        				"Value for 'EQUAL_PROPERTY' criteria must be CharSequence, but it is "
        				+(value == null ? "(null)" : "'"+value.getClass().getName()+"'")
        			);
            	}
        		return org.hibernate.criterion.Restrictions.eqProperty(propertyName, value.toString());
            case GREATER_PROPERTY:
        		return org.hibernate.criterion.Restrictions.gtProperty(propertyName, value.toString());
            case LESS_PROPERTY:
        		return org.hibernate.criterion.Restrictions.ltProperty(propertyName, value.toString());
            case GREATER_EQUAL_PROPERTY:
        		return org.hibernate.criterion.Restrictions.geProperty(propertyName, value.toString());
            case LESS_EQUAL_PROPERTY:
        		return org.hibernate.criterion.Restrictions.leProperty(propertyName, value.toString());
            case SQL_RESTICTION:
        		return org.hibernate.criterion.Restrictions.sqlRestriction(value.toString());
            default:
            	throw new IllegalStateException(String.format("Unknown query expression '%s'", type.name()));
        }
    }

    private DetachedCriteria subquery(SubqueryExpression sqex) {

    	String entityName = sqex.getTargetPropertyName();
    	String alias = Texts.tos(sqex.getAlias(), null);
    	
    	DetachedCriteria dtc;
    	if(alias != null) {
    		dtc = DetachedCriteria.forEntityName(entityName, alias);
    	} else {
    		dtc = DetachedCriteria.forEntityName(entityName);
    	}
    	if(log.isTraceEnabled()) {
    		log.trace(String.format("Creating detached criteria for entity %s of type %s as %s", entityName, sqex.getType(), sqex.getValue()));
    	}

    	if(sqex.getValue() instanceof Restrictions) {
        	Restrictions restr = (Restrictions) sqex.getValue();
        	dtc.setProjection(Projections.id());
        	dtc.add(parseRestriction(restr));
    	} else if (sqex.getValue() instanceof Where) {
        	dtc.setProjection(Projections.id());
        	DetachedWhereParser whp = new DetachedWhereParser();
        	whp.parse((Where) sqex.getValue(), dtc);
    	} else {
    		throw new IllegalStateException(String.format("Subquery must be instance of restrictions or Where, but is %s", sqex.getValue().getClass().getName()));
    	}

    	return dtc;
    }
}

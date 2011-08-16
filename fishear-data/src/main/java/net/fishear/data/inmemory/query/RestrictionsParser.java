package net.fishear.data.inmemory.query;


import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.restrictions.Conjunction;
import net.fishear.data.generic.query.restrictions.Expression;
import net.fishear.data.generic.query.restrictions.ExpressionTypes;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.inmemory.InMemoryCriteria;

class 
	RestrictionsParser
extends 
	AbstractQueryParser<Restrictions, InMemoryCriteria> 
{

	public void parse(Restrictions sourceObject, InMemoryCriteria output) {
        if (sourceObject != null) {
            parseRestriction(sourceObject, output);
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
    private void parseRestriction(Restrictions currentRes, InMemoryCriteria output) {
		throw new IllegalArgumentException("Conditions are not supported");

//        if (currentRes instanceof Conjunction) {
//            //if is composite inner node like AND, OR etc.
//            Conjunction conjunction = (Conjunction) currentRes;
//            return parseConjuction(conjunction);
//        }
//        else if (currentRes instanceof Expression) {
//            //if is composite leaf node
//            Expression expression = (Expression) currentRes;
//            return parseExpression(expression);
//        }
//        else if (LOG.isErrorEnabled()) {
//            LOG.error("Cannot parse leaf query class " + currentRes);
//        }
//        return null;
    }

    private InMemoryCriteria parseConjuction(Conjunction conjunction) {
    	return null;
//        Criterion leftRestricitions = parseRestriction(conjunction.getLeft());
//        Restrictions[] restrictions = conjunction.getRight();
//        Criterion[] criterias = new Criterion[restrictions.length];
//        for (int i = 0; i < criterias.length; i++) {
//            criterias[i] = parseRestriction(restrictions[i]);
//		}
//
//        Criterion crit;
//        switch (conjunction.getType()) {
//            case AND:
//                crit = leftRestricitions;
//                for (int i = 0; i < criterias.length; i++) {
//                    crit = org.hibernate.criterion.Restrictions.and(crit, criterias[i]);
//				}
//                return crit;
//            case OR:
//                crit = leftRestricitions;
//                for (int i = 0; i < criterias.length; i++) {
//                    crit = org.hibernate.criterion.Restrictions.or(crit, criterias[i]);
//				}
//                return crit;
//            case NOT:
//                return org.hibernate.criterion.Restrictions.not(leftRestricitions);
//            default:
//                ConjunctionTypes type = conjunction.getType();
//                if (LOG.isErrorEnabled()) {
//                    LOG.error("Query conjuction " + type.name() + " cannot be recognized!");
//                }
//                throw new IllegalStateException("Unknown conjunction type: " + type.name());
//        }
    }

    private InMemoryCriteria parseExpression(Expression expression) {

        ExpressionTypes type = expression.getType();
        String propertyName = expression.getTargetPropertyName();
        Object value = expression.getValue();
        Class<?> clazz;
return null;
//        switch (type) {
//            case EQUAL:
//                return org.hibernate.criterion.Restrictions.eq(propertyName, value);
//            case GREATER:
//                return org.hibernate.criterion.Restrictions.gt(propertyName, value);
//            case GREATER_OR_EQUAL:
//                return org.hibernate.criterion.Restrictions.ge(propertyName, value);
//            case LESS:
//                return org.hibernate.criterion.Restrictions.lt(propertyName, value);
//            case LESS_OR_EQUAL:
//                return org.hibernate.criterion.Restrictions.le(propertyName, value);
//            case LIKE:
//            	String sval = (String)value;
//            	if(sval.contains("%")) {
//                    return org.hibernate.criterion.Restrictions.ilike(propertyName, sval);
//            	} else {
//                    return org.hibernate.criterion.Restrictions.ilike(propertyName, sval, MatchMode.ANYWHERE);
//            	}
//            case LIKE_END:
//                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.END);
//            case LIKE_START:
//                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.START);
//            case LIKE_EXACT:
//                return org.hibernate.criterion.Restrictions.ilike(propertyName, (String)value, MatchMode.EXACT);
//            case BETWEEN:
//                return org.hibernate.criterion.Restrictions.between(propertyName, ((Object[])value)[0], ((Object[])value)[1]);
//            case IN:
//            	clazz = value.getClass();
//            	if(clazz.isArray()) {
//                    return org.hibernate.criterion.Restrictions.in(propertyName, (Object[])value);
//            	} else if(Collection.class.isAssignableFrom(clazz)) {
//                    return org.hibernate.criterion.Restrictions.in(propertyName, (Collection<?>)value);
//            	} else {
//            		throw new IllegalStateException(
//            			"Object for 'IN' criteria must be array or collection, but it is " +
//            			(clazz == null ? "(null)" : "'"+clazz.getName()+"'")
//            		);
//            	}
//            case IS_NULL:
//                return org.hibernate.criterion.Restrictions.isNull(propertyName);
//            case IS_NOT_NULL:
//                return org.hibernate.criterion.Restrictions.isNotNull(propertyName);
//            case NOT_EQUAL:
//                return org.hibernate.criterion.Restrictions.ne(propertyName, value);
//            case EXISTS:
//            	return Subqueries.exists(subquery((SubqueryExpression) expression));
//            case NOT_EXISTS:
//            	return Subqueries.notExists(subquery((SubqueryExpression) expression));
//            case EQUAL_PROPERTY:
//            	if(!(value instanceof CharSequence)) {
//            		throw new IllegalStateException(
//        				"Value for 'EQUAL_PROPERTY' criteria must be CharSequence, but it is "
//        				+(value == null ? "(null)" : "'"+value.getClass().getName()+"'")
//        			);
//            	}
//        		return org.hibernate.criterion.Restrictions.eqProperty(propertyName, value.toString());
//            case GREATER_PROPERTY:
//        		return org.hibernate.criterion.Restrictions.gtProperty(propertyName, value.toString());
//            case LESS_PROPERTY:
//        		return org.hibernate.criterion.Restrictions.ltProperty(propertyName, value.toString());
//            case GREATER_EQUAL_PROPERTY:
//        		return org.hibernate.criterion.Restrictions.geProperty(propertyName, value.toString());
//            case LESS_EQUAL_PROPERTY:
//        		return org.hibernate.criterion.Restrictions.leProperty(propertyName, value.toString());
//            case SQL_RESTICTION:
//        		return org.hibernate.criterion.Restrictions.sqlRestriction(value.toString());
//            default:
//            	throw new IllegalStateException("Query unary expression '" + type.name() + "' cannot be recognized!");
//        }
    }

//    private DetachedCriteria subquery(SubqueryExpression sqex) {
//    	String entityName = sqex.getTargetPropertyName();
//    	String alias = Texts.tos(sqex.getAlias(), null);
//    	Restrictions restr = (Restrictions) sqex.getValue();
//    	DetachedCriteria dtc;
//    	if(alias != null) {
//    		dtc = DetachedCriteria.forEntityName(entityName, alias);
//    	} else {
//    		dtc = DetachedCriteria.forEntityName(entityName);
//    	}
//    	dtc.setProjection(Projections.id());
//    	dtc.add(parseRestriction(restr));
//    	return dtc;
//    }
}

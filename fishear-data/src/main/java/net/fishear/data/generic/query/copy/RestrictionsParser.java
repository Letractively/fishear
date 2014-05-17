package net.fishear.data.generic.query.copy;

import net.fishear.data.generic.query.restrictions.Conjunction;
import net.fishear.data.generic.query.restrictions.Expression;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.query.restrictions.SubqueryExpression;
import net.fishear.utils.Globals;

import org.slf4j.Logger;



class RestrictionsParser {

    private static final Logger LOG = Globals.getLogger();

    public Restrictions parse(Restrictions sourceObject) {
        if (sourceObject == null) {
            return null;
        }
        return copyRestrictions(sourceObject);
    }

    private Restrictions copyRestrictions(Restrictions restriction) {
        if (restriction instanceof Conjunction) {
        	return new Conjunction((Conjunction) restriction);
        } else if (restriction instanceof SubqueryExpression) {					// !! must be tested before 'Expression', because extends it
            return new SubqueryExpression((SubqueryExpression)restriction);
        } else if (restriction instanceof Expression) {
        	return new Expression((Expression)restriction);
        } else if (LOG.isErrorEnabled()) {
            LOG.error("Restriction class " + restriction.getClass().getName() + " cannot be copied!");
        }
        return null;
    }
}

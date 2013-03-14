package net.fishear.data.hibernate.query;


import java.util.List;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.conditions.Join;
import net.fishear.data.generic.query.conditions.Join.JoinType;
import net.fishear.data.generic.query.conditions.NestedRestriction;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.utils.Texts;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;

class DetachedWhereParser extends AbstractQueryParser<Where, DetachedCriteria> {

    private DetachedRestrictionsParser restrictionsParser;

    public DetachedWhereParser() {
        this.restrictionsParser = new DetachedRestrictionsParser();
    }

	public void parse(Where sourceObject, DetachedCriteria output) {
		parseConditions(sourceObject.getConditions(), output);
    }

    private void parseConditions(Conditions conditions, DetachedCriteria output) {
    	if(conditions != null) {
	        parseRootConditions(conditions, output);
	        parseJoinConditions(conditions, output);
	        parseNestedRestrictions(conditions, output);
    	}
    }

    private void parseNestedRestrictions(Conditions conditions, DetachedCriteria output) {
        for (NestedRestriction nestedRestriction : conditions.getNestedRestrictions()) {
        	DetachedCriteria c = output;
            String property = nestedRestriction.getPropertyName();
            if (nestedRestriction.getPropertyName().indexOf(".") != -1) {
                String[] splittedName = nestedRestriction.getPropertyName().split("\\.");
                for (int i=0;i<splittedName.length;i++) {

                    c = c.createCriteria(splittedName[i]);
                    
                }

            } else {
                c = output.createCriteria(property);
            }
            restrictionsParser.parse(nestedRestriction.getRestrictions(), c);
        }
    }

    private void parseRootConditions(Conditions conditions, DetachedCriteria output) {
        Restrictions mainRestrictions = conditions.getRootRestriction();
        restrictionsParser.parse(mainRestrictions, output);
    }

    private void parseJoinConditions(Conditions conditions, DetachedCriteria output) {
        List<Join> joinsList = conditions.getJoins();
        if (joinsList != null) {
            for (Join j : joinsList) {
            	
            	DetachedCriteria c;
            	String alias = Texts.tos(j.getAlias(), null);
            	if(alias == null) {
            		c = output.createCriteria(j.getPropertyName(), joinType(j.getJoinType()));
            	} else {
            		c = output.createCriteria(j.getPropertyName(), alias, joinType(j.getJoinType()));
            	}

                Restrictions joinRestrictions = j.getRestrictions();
                restrictionsParser.parse(joinRestrictions, c);
            }
        }
    }

	private int joinType(JoinType joinType) {
		if(joinType == null) {
			return CriteriaSpecification.INNER_JOIN;
		}
		switch(joinType) {
		case INNER:
			return CriteriaSpecification.INNER_JOIN;
		case OUTER:
			return CriteriaSpecification.LEFT_JOIN;
		case FULL:
			return CriteriaSpecification.FULL_JOIN;
		}
		throw new IllegalStateException(String.format("Unknown join type: %s", joinType));
	}
}

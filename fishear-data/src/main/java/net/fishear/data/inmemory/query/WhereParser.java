package net.fishear.data.inmemory.query;


import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.conditions.*;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.inmemory.InMemoryCriteria;

class WhereParser extends AbstractQueryParser<Where, InMemoryCriteria> {

    private RestrictionsParser restrictionsParser;

    public WhereParser() {
        this.restrictionsParser = new RestrictionsParser();
    }

    public void parse(Where sourceObject, InMemoryCriteria output) {
    	if(sourceObject != null) {
    		parseConditions(sourceObject.getConditions(), output);
    	}
    }

    private void parseConditions(Conditions conditions, InMemoryCriteria output) {
    	if(conditions != null) {

        	parseRootConditions(conditions, output);
        	parseJoinConditions(conditions, output);
        	parseNestedRestrictions(conditions, output);
    	}
    }

    private void parseNestedRestrictions(Conditions conditions, InMemoryCriteria output) {
//        for (NestedRestriction nestedRestriction : conditions.getNestedRestrictions()) {
//            Criteria c = output;
//            String property = nestedRestriction.getPropertyName();
//            if (nestedRestriction.getPropertyName().indexOf(".") != -1) {
//                String[] splittedName = nestedRestriction.getPropertyName().split("\\.");
//                for (int i=0;i<splittedName.length;i++) {
//
//                    c = c.createCriteria(splittedName[i]);
//                    
//                }
//
//            } else {
//                c = output.createCriteria(property);
//            }
//            restrictionsParser.parse(nestedRestriction.getRestrictions(), c);
//        }
    }

    private void parseRootConditions(Conditions conditions, InMemoryCriteria output) {
        Restrictions mainRestrictions = conditions.getRootRestriction();
        restrictionsParser.parse(mainRestrictions, output);
    }

    private void parseJoinConditions(Conditions conditions, InMemoryCriteria output) {
//        List<Join> joinsList = conditions.getOuterJoins();
//        if (joinsList != null) {
//            for (Join j : joinsList) {
//            	
//            	Criteria c;
//            	String alias = Texts.tos(j.getAlias(), null);
//            	if(alias == null) {
//            		c = output.createCriteria(j.getPropertyName());
//            	} else {
//            		c = output.createCriteria(j.getPropertyName(), alias);
//            	}
//
//                Restrictions joinRestrictions = j.getRestrictions();
//                restrictionsParser.parse(joinRestrictions, c);
//            }
//        }
    }
}

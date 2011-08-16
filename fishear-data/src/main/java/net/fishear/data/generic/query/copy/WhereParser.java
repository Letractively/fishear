package net.fishear.data.generic.query.copy;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.conditions.Join;
import net.fishear.data.generic.query.conditions.NestedRestriction;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.data.generic.query.restrictions.Restrictions;



class WhereParser extends AbstractQueryParser<Where, QueryConstraints> {

	private RestrictionsParser restrictionsParser;

    public WhereParser() {
        this.restrictionsParser = new RestrictionsParser();
    }

    public void parse(Where sourceObject, QueryConstraints output) {
        parseConstrains(sourceObject.getConditions(), output.where().conditions());
    }

    private void parseConstrains(Conditions sourceObject, Conditions output) {
    	if(sourceObject != null) {
	        copyJoins(sourceObject, output);
	        copyRestrictions(sourceObject, output);
	        copyNestedRestrictions(sourceObject, output);
    	}
    }

    private void copyNestedRestrictions(Conditions sourceObject, Conditions output) {
        for (NestedRestriction nr: sourceObject.getNestedRestrictions()) {
            output.add(nr);
        }
    }

    private void copyRestrictions(Conditions sourceObject, Conditions output) {
        Restrictions restrictions =
                restrictionsParser.parse(sourceObject.getRootRestriction());
        output.add(restrictions);
    }

    private void copyJoins(Conditions sourceObject, Conditions output) {

        for (Join j : sourceObject.getOuterJoins()) {
            Restrictions restrictions =
                    restrictionsParser.parse(j.getRestrictions());
            output.join(j.getPropertyName(), restrictions);
        }
    }
}

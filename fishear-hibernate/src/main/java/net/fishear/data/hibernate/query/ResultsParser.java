package net.fishear.data.hibernate.query;


import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.results.AggregateProperty;
import net.fishear.data.generic.query.results.Functions;
import net.fishear.data.generic.query.results.Results;
import net.fishear.utils.Globals;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;


/**
 * @author liso
 */
class ResultsParser extends AbstractQueryParser<Results, Criteria> {

    private static Logger LOG = Globals.getLogger();

    @Override
    public void parse(Results sourceObject, Criteria output) {
    	if(sourceObject != null) {
	        if (sourceObject.getResultsPerPage() != Results.ALL_RESULTS) {
	            output.setFirstResult(sourceObject.getFirstResultIndex());
	            output.setMaxResults(sourceObject.getResultsPerPage());
	        }
	        parseResultFunctions(sourceObject, output);
    	}
    }

    private void parseResultFunctions(Results resultObject, Criteria output) {
        ProjectionList projectionList = Projections.projectionList();
        for (AggregateProperty ap : resultObject.getFunctionsList()) {

            String propertyName = ap.getPropertyName();
            Functions func = ap.getFunction();

            switch (func) {
                case COUNT:
                    projectionList.add(Projections.count(propertyName));
                    break;
                case MAX:
                    projectionList.add(Projections.max(propertyName));
                    break;
                case AVG:
                    projectionList.add(Projections.avg(propertyName));
                    break;
                case MIN:
                    projectionList.add(Projections.min(propertyName));
                    break;
                case SUM:
                    projectionList.add(Projections.sum(propertyName));
                    break;
                default:
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Query result function " + func.name() +
                                " cannot be recognized!");
                    }
            }
        }

        if (projectionList.getLength() != 0) {
            output.setProjection(projectionList);
        }
    }
}
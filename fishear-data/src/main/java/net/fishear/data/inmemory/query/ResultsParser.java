package net.fishear.data.inmemory.query;


import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.results.AggregateProperty;
import net.fishear.data.generic.query.results.Functions;
import net.fishear.data.generic.query.results.Results;
import net.fishear.data.inmemory.InMemoryCriteria;
import net.fishear.utils.Globals;

import org.slf4j.Logger;


/**
 * @author liso
 */
class ResultsParser extends AbstractQueryParser<Results, InMemoryCriteria> {

    private static Logger LOG = Globals.getLogger();

    @Override
    public void parse(Results sourceObject, InMemoryCriteria output) {

        if (sourceObject.getResultsPerPage() != Results.ALL_RESULTS) {
        	int from = sourceObject.getFirstResultIndex();
        	int pp = sourceObject.getResultsPerPage();
        	if(pp <= 0) {
        		throw new IllegalArgumentException("Per page count must be great then 0, current value is: " + pp);
        	}
        	if(from < 0) {
        		throw new IllegalArgumentException("Begin index must not be less then 0, current value is: " + pp);
        	}
            output.setResults(sourceObject.getFirstResultIndex(), sourceObject.getResultsPerPage());
            parseResultFunctions(sourceObject, output);
        } else {
        	output.setResults(0, Integer.MAX_VALUE);
        }
    }

    private void parseResultFunctions(Results sourceObject, InMemoryCriteria output) {

        for (AggregateProperty ap : sourceObject.getFunctionsList()) {

            String propertyName = ap.getPropertyName();
            Functions func = ap.getFunction();
            
            throw new IllegalStateException("Functions are not implemented.");
//
//            switch (func) {
//                case COUNT:
//                    projectionList.add(Projections.count(propertyName));
//                    break;
//                case MAX:
//                    projectionList.add(Projections.max(propertyName));
//                    break;
//                case AVG:
//                    projectionList.add(Projections.avg(propertyName));
//                    break;
//                case MIN:
//                    projectionList.add(Projections.min(propertyName));
//                    break;
//                default:
//                    if (LOG.isErrorEnabled()) {
//                        LOG.error("Query result function " + func.name() +
//                                " cannot be recognized!");
//                    }
//            }
        }

//        if (projectionList.getLength() != 0) {
//            output.setProjection(projectionList);
//        }
    }
}
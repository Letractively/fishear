package net.fishear.data.generic.query.copy;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.results.AggregateProperty;
import net.fishear.data.generic.query.results.Results;

class ResultsParser
        extends AbstractQueryParser<Results, QueryConstraints> {

    public void parse(Results sourceObject, QueryConstraints output) {
        copyFunctions(sourceObject, output);

        output.results().setFirstResultIndex(sourceObject.getFirstResultIndex());
        output.results().setResultsPerPage(sourceObject.getResultsPerPage());
    }

    private void copyFunctions(Results sourceObject, QueryConstraints output) {
        for (AggregateProperty pfp : sourceObject.getFunctionsList()) {
            output.results().add(pfp.getPropertyName(), pfp.getFunction());
        }
    }
}

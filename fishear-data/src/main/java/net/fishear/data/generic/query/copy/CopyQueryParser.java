package net.fishear.data.generic.query.copy;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;

public class 
	CopyQueryParser 
extends 
	AbstractQueryParser<QueryConstraints, QueryConstraints> 
{

    private WhereParser whereParser = new WhereParser();
    private ResultsParser resultsParser = new ResultsParser();
    private OrderByParser orderByParser = new OrderByParser();

    @Override
    public void parse(QueryConstraints sourceObject, QueryConstraints output) {

        whereParser.parse(sourceObject.where(), output);
        resultsParser.parse(sourceObject.results(), output);
        orderByParser.parse(sourceObject.orderBy(), output);
    }
}

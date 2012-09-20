package net.fishear.data.generic.query.results;


import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.query.AbstractQueryPart;
import net.fishear.utils.Defender;

public class 
	Results 
extends 
	AbstractQueryPart
implements
	Cloneable
{

    public static final int DEFAULT_RESULTS_PER_PAGE = 20;
    public static final int ALL_RESULTS = -1;

    private ResultType resultType;
    
    /**
     * Maximum of returned entities.
     */
    private int resultsPerPage;
    /**
     * Index of first returned entity.
     */
    private int firstResultIndex;

    /**
     * List of property name and applied aggregation function.
     */
    private List<AggregateProperty> functionsList;

    public Results() {
        this.resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
        this.firstResultIndex = 0;
        functionsList = new ArrayList<AggregateProperty>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Results that = (Results) o;

        if (firstResultIndex != that.firstResultIndex) {
            return false;
        }
        if (resultsPerPage != that.resultsPerPage) {
            return false;
        }
        if (!functionsList.equals(that.functionsList)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = resultsPerPage;
        result = 31 * result + firstResultIndex;
        result = 31 * result + functionsList.hashCode();
        return result;
    }

    /**
     * Adds into results total count of entities returned as query result.
     *
     * @return <code>this</code>.
     */
    public Results addRowCount() {
        return this.add("id", Functions.COUNT);
    }

    /**
     * Adds into results aggregation function applied on property with given name.
     *
     * @param propertyName  Entity property name on that is aggregation function applied.
     * @param func          Aggregation function applied to property name.
     * @return              <code>this</code>.
     * @throws IllegalArgumentException When property name is null or empty.
     */
    public Results add(String propertyName, Functions func) {
        Defender.notNullOrEmpty(propertyName, "propertyName");

        AggregateProperty aggregateProperty = new AggregateProperty(propertyName, func);
        return this.add(aggregateProperty);
    }

    /**
     * Adds into results aggregation function and property name pair.
     *
     * @param aggregateProperty
     * @return <code>this</code>
     */
    public Results add(AggregateProperty aggregateProperty) {
        Defender.notNull(aggregateProperty, "aggregateProperty");
        functionsList.add(aggregateProperty);
        return this;
    }

    /**
     * Set maximum of results per one page. Default is set to
     * {@link #DEFAULT_RESULTS_PER_PAGE} value.
     *
     * @param resultsPerPage required results per page, or -1 to return all records.
     * @return <code>this</code>.
     */
    public Results setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
        return this;
    }

    /**
     * Set index of first returned result. Default value is 0.
     *
     * @param firstResultIndex
     * @return <code>this</code>.
     */
    public Results setFirstResultIndex(int firstResultIndex) {

    	Defender.greatThanOrZero(firstResultIndex, "firstResultIndex");
        this.firstResultIndex = firstResultIndex;
        return this;
    }

    /**
     * List of set query results.
     */
    public List<AggregateProperty> getFunctionsList() {
        return functionsList;
    }

    /**
     * Set maximum of results per one page. Default is set to
     * {@link #DEFAULT_RESULTS_PER_PAGE} value.
     *
     * @return Value greater or equal than zero.
     */
    public int getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * Set index of first returned result. Default value is 0.
     *
     * @return Value greater or equal than zero.
     */
    public int getFirstResultIndex() {
        return firstResultIndex;
    }

    public String toString() {
    	StringBuilder sb = new StringBuilder();
		long first = getFirstResultIndex();
		long ppage = getResultsPerPage();
		sb.append("\n    ");
		sb.append(first).append(" - ").append(first + ppage).append(" (").append(ppage).append(" results per page)");
		List<AggregateProperty> fl = functionsList;
		if(fl != null && fl.size() > 0) {
			StringBuilder sb1 = new StringBuilder();
			for (AggregateProperty agp : fl) {
				if(sb1.length() > 0) { sb1.append(", "); }
				sb1.append(agp.toString());
			}
			sb1.insert(0, "\n    "+"FUNCTIONS: ");
			sb1.append("\n  ");
			sb.append(sb1);
		}
		return sb.toString();
    }

	/**
	 * @return the resultType
	 */
	public ResultType getResultType() {
		return resultType;
	}

	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}
}

package net.fishear.data.generic.query;



public abstract class 
	AbstractQueryParser<T extends AbstractQueryPart, E> 
implements 
	QueryParser<T, E>
{

    /**
     * Parse query constraints with all constaints, filters etc.
     *
     * @param output
     * @param constraints
     */
    public abstract void parse(T sourceObject, E output);
}

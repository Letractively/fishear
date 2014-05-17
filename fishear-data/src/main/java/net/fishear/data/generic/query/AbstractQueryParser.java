package net.fishear.data.generic.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class 
	AbstractQueryParser<T extends AbstractQueryPart, E> 
implements 
	QueryParser<T, E>
{

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
    /**
     * Parse query constraints with all constaints, filters etc.
     *
     * @param output
     * @param constraints
     */
    public abstract void parse(T sourceObject, E output);
}

package net.fishear.data.generic.query;

public interface QueryParser<T extends AbstractQueryPart, E> {
    /**
     * Parse query constraints with all constaints, filters etc.
     *
     * @param output
     * @param constraints
     */
    public void parse(T constraints, E output);
}

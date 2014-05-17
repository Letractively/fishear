package net.fishear.data.generic.query.restrictions;

/**
 * Types of binary operators for query expressions.
 *
 * @see net.fishear.data.generic.query.restrictions.Restrictions
 *
 */
public enum ExpressionTypes {
    EQUAL,
    GREATER,
    GREATER_OR_EQUAL,
    LESS,
    LESS_OR_EQUAL,
    LIKE,
    IN,
    BETWEEN,
    IS_NULL,
    IS_NOT_NULL, 
    NOT_EQUAL, 
    EXISTS,
    NOT_EXISTS,
    EQUAL_PROPERTY, 
    GREATER_PROPERTY, 
    LESS_PROPERTY, 
    GREATER_EQUAL_PROPERTY, 
    LESS_EQUAL_PROPERTY, 
    LIKE_IGNORE_CASE, 
    LIKE_END, 
    LIKE_START, 
    LIKE_EXACT, 
    SQL_RESTICTION
}

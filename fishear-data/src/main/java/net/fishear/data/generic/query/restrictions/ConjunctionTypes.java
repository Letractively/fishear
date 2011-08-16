package net.fishear.data.generic.query.restrictions;

public enum ConjunctionTypes {

    AND,

    OR,

    NOT(true)
    
    ;
    
    public final boolean unary;
    
    ConjunctionTypes() {
    	this.unary = false;
    }

    ConjunctionTypes(boolean unary) {
    	this.unary = unary;
    }
}

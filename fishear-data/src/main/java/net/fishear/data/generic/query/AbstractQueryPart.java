package net.fishear.data.generic.query;

public abstract class AbstractQueryPart {

    protected static boolean eq(Object o1, Object o2) {
    	if(o1 == null) {
    		return o2 == null;
    	}
    	return o2 != null && o1.equals(o2);
    }


}

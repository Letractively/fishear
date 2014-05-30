package net.fishear.data.generic.query.restrictions;

import org.slf4j.Logger;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.utils.Globals;

public class Expression extends Restrictions {

	private static Logger log = Globals.getLogger();
	
    private final String targetPropertyName;
    private final ExpressionTypes type;
    private final Object value;
    
    private String comment;

    /**
     * if set to true, entities in transient state are added to the code normal way (even if likely exception will be thrown by ORM tool).
     * false (default) means in case transient (not saved) entity (e.g. implementation of {@link EntityI} ) is added, the resulting expressions is "FALSE" sql expression.
     * 
     * Since this is static variable, it is applied to all queries created after it is changed.
     */
    public static boolean IGNORE_TRANSIENT_ENTITIES = false;
    
    
    public Expression(ExpressionTypes type, String targetPropertyName, Object value) {
    	if(transientEnabled(type) && value != null && value instanceof EntityI<?> && ((EntityI<?>)value).isNew()) {
    		comment = String.format("Original: %s %s %s, replaced by 'SQL FALSE condition'", targetPropertyName, type, value);
    		log.debug("Transient entity ignored, replaced by SQL (1=2) condition. {}", comment);
            this.targetPropertyName = null;
            this.type = ExpressionTypes.SQL_RESTICTION;
            this.value = "1=2";
    	} else {
            this.targetPropertyName = targetPropertyName;
            this.type = type;
            this.value = value;
    	}
    }

    private boolean transientEnabled(ExpressionTypes type) {
    	if(!IGNORE_TRANSIENT_ENTITIES) {
    		// probably no type check is required
    		return true;
    	}
		return false;
	}

	public Expression(Expression expr) {
    	this.type = expr.type;
    	this.targetPropertyName = expr.targetPropertyName;
    	this.value = expr.value;
	}
    

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expression that = (Expression) o;

        if (!targetPropertyName.equals(that.targetPropertyName)) return false;
        if (type != that.type) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = targetPropertyName.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    public String getTargetPropertyName() {
        return targetPropertyName;
    }

    public ExpressionTypes getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
    
    protected String val_(Object val) {
    	if(type == ExpressionTypes.LIKE) {
    		String s = val.toString();
    		return super.val_(s.contains("%") ? val : "%"+s+"%");
    	} else if(type == ExpressionTypes.LIKE_END){
    		return super.val_(val+"%");
    	} else if(type == ExpressionTypes.LIKE_START){
    		return super.val_("%"+val);
    	} else if(type == ExpressionTypes.LIKE_EXACT){
    		return super.val_("%"+val+"%");
    	} else {
    		return super.val_(val);
    	}
    }

    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if(type == ExpressionTypes.IN) {
    		Object[] oa = (Object[])value;
    		sb.append(targetPropertyName).append(" ").append(type).append(" (");
    		for (int i = 0; i < oa.length; i++) {
				sb.append(i > 0 ? ", " : "").append(val_(oa[i]));
			}
    		sb.append(")");
    	} else if(type == ExpressionTypes.IS_NOT_NULL) {
    		sb.append(targetPropertyName).append(" ").append("IS NOT NULL");
    	} else if(type == ExpressionTypes.IS_NULL) {
    		sb.append(targetPropertyName).append(" ").append("IS NULL");
    	} else {
    		sb.append(targetPropertyName).append(" ").append(type).append(" ").append(val_(value));
    	}
    	if(comment != null) {
    		sb.append("[").append(comment).append("]");
    	}
    	return sb.toString(); 
    }

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}

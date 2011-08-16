package net.fishear.data.generic.query.restrictions;

public class Expression extends Restrictions {

    private final String targetPropertyName;
    private final ExpressionTypes type;
    private final Object value;

    public Expression(ExpressionTypes type, String targetPropertyName, Object value) {
        this.targetPropertyName = targetPropertyName;
        this.type = type;
        this.value = value;
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
    	return sb.toString(); 
    }
}

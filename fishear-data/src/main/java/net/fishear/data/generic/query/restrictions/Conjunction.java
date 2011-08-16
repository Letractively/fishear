package net.fishear.data.generic.query.restrictions;

import net.fishear.utils.Defender;

public class Conjunction extends Restrictions {

    private final ConjunctionTypes type;

    private final Restrictions left;
    private final Restrictions[] right;

    public Conjunction( ConjunctionTypes type, Restrictions left, Restrictions... right) {
    	Defender.notNull(type, "type");
    	Defender.notNull(left, "left");
    	if(type.unary) {
    		if(right != null && right.length > 0 && right[0] != null) {
    			throw new IllegalArgumentException(type.name() + " is unary oprerator, therefore does not allow to have right expression");
    		}
    	} else {
        	Defender.notNull(right, "right");
        	for (int i = 0; i < right.length; i++) {
        		if(right[i] == null) {
        			String expdesc = i == 0 ? "first expression" : i == 1 ? "second expression" : i == 2 ? "third expression" : "expression at index " + i;
        			throw new IllegalArgumentException("Right expression(s) is not allowed to be null, but " + expdesc + " is null");
        		}
			}
    	}
        this.type = type;
        this.left = left;
        this.right = right == null ? new Restrictions[0] : right;
    }

    public Conjunction(Conjunction conjunction) {
        this.type = conjunction.type;
        this.left = conjunction.left;
        this.right = conjunction.right;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunction that = (Conjunction) o;

        if (!left.equals(that.left)) return false;
        if(right.length != that.right.length) {
        	return false;
        }
        for (int i = 0; i < right.length; i++) {
            if (!right[i].equals(that.right[i])) return false;
		}
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = type.hashCode();
        result = 31 * result + left.hashCode();
        for (int i = 0; i < right.length; i++) {
            result = 31 * result + right[i].hashCode();
		}
        return result;
    }

    /**
     * Type of conjuctions operator.
     *
     * @return
     */
    public ConjunctionTypes getType() {
        return type;
    }

    /**
     * Left subtree root node.
     *
     * @return
     */
    public Restrictions getLeft() {
        return left;
    }

    /**
     * Returns rigth subtree root nodes. 
     * In case no subtree is defined, returns empty array. Never returns null.
     *
     * @return
     */
    public Restrictions[] getRight() {
        return right;
    }
    
    public String toString() {
    	if(type.unary) {
        	return type  + " " + left.toString(); 
    	} else {
    		StringBuilder sb = new StringBuilder();
    		sb.append(left);
    		for (int i = 0; i < right.length; i++) {
        		sb.append(" ").append(type).append(" ").append(right[i]);
			}
    		return sb.toString();
    	}
    }
}

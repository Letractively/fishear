package net.fishear.data.generic.query.restrictions;

import net.fishear.utils.Texts;

public class SubqueryExpression extends Expression
{
	private String alias;

	private static ExpressionTypes[] types = {
			ExpressionTypes.EXISTS,
			ExpressionTypes.NOT_EXISTS,
		};

	private static ExpressionTypes checkType(ExpressionTypes type) {
		for (int i = 0; i < types.length; i++) {
			if(type.equals(types[i])) {
				return type;
			}
		}
		throw new IllegalArgumentException("Only "+Texts.tos(types, " ")+" is allowed as type for subquery");
	}
	
	public SubqueryExpression(ExpressionTypes type, String forEntity, Restrictions subquery) {
		super(checkType(type), forEntity, subquery);
	}

	public SubqueryExpression(SubqueryExpression expression) {

		super(expression);
		this.alias = expression.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
	
	public String toString() {
		String alias = Texts.tos(this.alias, null);
		return getType() + " (FROM " + getTargetPropertyName() + (alias != null ? " " + alias : "") + " WHERE " + getValue() + ")";
	}

}

package net.fishear.data.generic.query;

import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.copy.CopyQueryParser;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.utils.Defender;



public class QueryFactory
{

    private static QueryParser<QueryConstraints, QueryConstraints> copyParser = (QueryParser<QueryConstraints, QueryConstraints>) new CopyQueryParser();

	private QueryFactory() {

	}

	/**
	 * Make new instance with default values. This instance has first returned
	 * row set to 0, row count set to default value (by default it id 20 rows)
	 * and no WHERE or ORDEFR BY clause.
	 * 
	 * @return Simply call default constructor.
	 */
	public static QueryConstraints createDefault() {
		return new QueryConstraints();
	}

	public static QueryConstraints fullResult(Restrictions restriction) {
		QueryConstraints qc = fullResult();
		qc.where().conditions().add(restriction);
		return qc;
	}

	/**
	 * synonym for {@link #createDefault()} (it is too long for typping)
	 */
	public static QueryConstraints create() {
		return new QueryConstraints();
	}

	/**
	 * shortcut to {@link #createDefault()}
	 * 
	 * @return
	 */
	public static QueryConstraints defaults() {
		return new QueryConstraints();
	}

	/**
	 * Make new instance with default values. This instance has number of
	 * returned rows set to {@link Integer#MAX_VALUE}, all others parameters are
	 * set as in method {@link #createDefault()}.
	 * 
	 * @return Simply call default constructor.
	 */
	public static QueryConstraints fullResult() {
		QueryConstraints qc = new QueryConstraints();
		qc.results().setResultsPerPage(Integer.MAX_VALUE);
		return qc;
	}

	/**
	 * create copy of existing constraints or new constraints.
	 * 
	 * @param constraints
	 *            if null, crates and returns new (empty) instance of
	 *            constraints, otherwise make copy of this parameter.
	 */
	public static QueryConstraints copyOrCreate(QueryConstraints constraints) {
		return constraints == null ? createDefault() : createCopy(constraints);
	}

	/**
	 * Make duplicate <tt>QueryConstrains</tt> object.
	 * <p/>
	 * NOTE:Copied object includes possibly dangerous data like id`s, passwords
	 * and so on in example objects or expression values!!!
	 * 
	 * @param constraints
	 *            Copied query object.
	 * @return Deep copy of query object.
	 * @throws IllegalArgumentException
	 *             When copied query is null.
	 */
	public static QueryConstraints createCopy(QueryConstraints constraints) {
		Defender.notNull(constraints, "query constaints");

        QueryConstraints result = createDefault();
        copyParser.parse(constraints, result);
        return result;
	}

	/**
	 * creates constraint with one where "equals" condition.
	 */
	public static QueryConstraints equals(String propertyName, Object value) {
		QueryConstraints qc = createDefault();
		qc.where().conditions().add(Restrictions.equal(propertyName, value));
		return qc;
	}

	/**
	 * creates constraint with two "equals" condition connected by "and"
	 * operator.
	 */
	public static QueryConstraints andEquals(String prop1Name, Object value1,
			String prop2Name, Object value2) {
		QueryConstraints qc = createDefault();
		qc.where().conditions().add(
				Restrictions.and(Restrictions.equal(prop1Name, value1),
						Restrictions.equal(prop2Name, value2)));
		return qc;
	}

	public static boolean equalsWhere(QueryConstraints cstr1,
			QueryConstraints cstr2) {
		if (cstr1 == cstr2) {
			return true;
		}
		if (cstr1 == null) {
			return cstr2 == null;
		}
		return cstr1.where().equals(cstr2.where());
	}

	/**
	 * creates and returns constraints depends on given restrictions.
	 * Restriction can be null = no restriction is set.
	 */
	public static QueryConstraints create(Restrictions restriction) {
		QueryConstraints cstr = createDefault();
		if(restriction != null) {
			cstr.where().conditions().add(restriction);
		}
		return cstr;
	}

	public static QueryConstraints like(String propertyName, String expression) {
		QueryConstraints qc = createDefault();
		qc.where().conditions().add(Restrictions.like(propertyName, expression));
		return qc;
	}

	 /** creates new query constraint instance with given condition.
	 * @param cond if it is null, no condition will be set. Otherwise, the condition is set.
	 */
	public static QueryConstraints create(Conditions cond) {
		QueryConstraints cstr = createDefault();
		if(cond != null) {
			cstr.where().setConditions(cond);
		}
		return cstr;
	}

	/**
	 * Here you can add you own factory method for query constrains where you
	 * can predefine you own values.
	 * 
	 * Add method only in wide used cases!
	 */
}

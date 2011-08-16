package net.fishear.data.generic.query;

import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.query.results.Projection;
import net.fishear.data.generic.query.results.Results;
import net.fishear.exceptions.AppException;



public class 
	QueryConstraints
extends
	AbstractQueryPart
implements 
	Cloneable
{

	private OrderBy orderBy;
	private Where where;
	private Projection projection;
	private Results results;

	/**
	 * The constructor.

	 * @see net.fishear.data.generic.query.QueryFactory
	 */
	QueryConstraints() {
		where = new Where();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		QueryConstraints that = (QueryConstraints) o;

		if (eq(orderBy, that.orderBy)) {
			return false;
		}
		if (eq(results, that.results)) {
			return false;
		}
		if (eq(where, that.where)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hc;
		hc = orderBy == null ? 0 : orderBy.hashCode();
		hc = 31 * hc + (where == null ? 0 : where.hashCode());
		hc = 31 * hc + (results == null ? 0 : results.hashCode());
		return hc;
	}

	/**
	 * adds new restriction to existing or creates new one (if it not exists
	 * yet). This is the shortcut to call
	 * <code>where().condition().add(...)</code>
	 */
	public Conditions add(Restrictions restriction) {
		return where().conditions().add(restriction);
	}

	/**
	 * Object for order by the results.
	 */
	public OrderBy orderBy() {
		if(orderBy == null) {
			orderBy = new OrderBy();
		}
		return orderBy;
	}

	/**
	 * Object for setting constraints and examples.
	 */
	public Where where() {
		return where;
	}

	/**
	 * Object for setting query results.
	 */
	public Results results() {

		if(results == null) {
			results = new Results();
		}
		return results;
	}

	/**
	 * returns data filter (e.g. where clause for SQL) currently set. Never
	 * returns null.
	 * 
	 * @return
	 */
	public Where getFilter() {
		return where;
	}

	/**
	 * Sets the filer to restrict data.
	 * 
	 * @param filter
	 *            the filter conditions. This must not be null
	 * @throws NullPointerException
	 *             when the 'filter' argument is null
	 */
	public void setFilter(Where filter) {
		if (filter == null) {
			throw new NullPointerException("'filter' argument must not be null");
		}
		this.where = filter;
	}

	/**
	 * returns current order by clause. Method is provided for JavaBean
	 * compatibility.
	 * 
	 * @return
	 */
	public OrderBy getOrderBy() {
		return this.orderBy;
	}

	/**
	 * Sets the sql ORDER BY clause or equivalent.
	 * 
	 * @param order
	 *            the order by clause. This must not be null
	 * @throws NullPointerException
	 *             when the 'order' argument is null
	 */
	public void setOrderBy(OrderBy order) {
		if (order == null) {
			throw new NullPointerException("'order' argument must not be null");
		}
		this.orderBy = order;
	}

	/**
	 * returns restriction of where condition, if it is set. Returns null if no
	 * restriction.
	 */
	public Restrictions getRootRestriction() {
		if (where != null) {
			return where.getRootRestriction();
		}
		return null;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	/**
	 * Returns the projection instance (if any exists). 
	 * Returns null if no projection is required.
	 */
	public Projection getProjection() {
		return projection;
	}

	/**
	 * Always returns the projection instance. 
	 * If the one does not exists, creates it.
	 */
	public Projection projection() {
		if (projection == null) {
			projection = new Projection();
		}
		return projection;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (where != null) {
			String whereStr = where.toString();
			sb.append(" WHERE ").append(whereStr.length() > 0 ? whereStr : "(no where)");
		}
		String s;
		if (orderBy != null && (s = orderBy.toString()) != null && s.length() > 0) {
			sb.append("\n");
			sb.append(" ORDER BY ").append(s);
		}
		if (results != null) {
			sb.append("\n");
			sb.append(" RESULTS: [").append(results.toString()).append("]");
		}
		if (projection != null && projection.size() > 0) {
			sb.append("\n");
			sb.append(" PROJECTION: [").append(projection.toString()).append("]");
		}
		return sb.toString();
	}

	public Object clone() {
		QueryConstraints qc;
		try {
			qc = (QueryConstraints) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new AppException(ex);
		}
		return qc;
	}

	public Where getWhere() {
		return where;
	}

	public Results getResults() {
		return results;
	}

}
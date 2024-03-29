package net.fishear.data.generic.query;

import java.util.Arrays;
import java.util.List;

import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.conditions.Join;
import net.fishear.data.generic.query.conditions.Where;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.order.SortDirection;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.query.results.Projections;
import net.fishear.data.generic.query.results.Results;
import net.fishear.exceptions.AppException;
import net.fishear.utils.Texts;



public class 
	QueryConstraints
extends
	AbstractQueryPart
implements 
	Cloneable
{

	private String alias;

	private OrderBy orderBy;
	private Where where;
	private Projections projections;
	private Results results;
	
	private List<String> eagerLoad;

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

		if (!eq(orderBy, that.orderBy)) {
			return false;
		}
		if (!eq(results, that.results)) {
			return false;
		}
		if (!eq(where, that.where)) {
			return false;
		}
		if (!eq(eagerLoad, that.eagerLoad)) {
			return false;
		}
		if (!eq(projections, that.projections)) {
			return false;
		}
		if (!eq(alias, that.alias)) {
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

	public void setProjections(Projections projections) {
		this.projections = projections;
	}

	/**
	 * Returns the projections instance (if any exists). 
	 * Returns null if no projections is required.
	 */
	public Projections getProjections() {
		return projections;
	}

	/**
	 * Always returns the projections instance. 
	 * If the one does not exists, creates it.
	 */
	public Projections projections() {
		if (projections == null) {
			projections = new Projections();
		}
		return projections;
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
		if (projections != null && projections.size() > 0) {
			sb.append("\n");
			sb.append(" PROJECTION: [").append(projections.toString()).append("]");
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

	/** adds ascending sorting by given property.
	 * @param property the property
	 * @return this
	 */
	public QueryConstraints orderBy(String property) {
		orderBy().add(property, SortDirection.ASCENDING);
		return this;
	}

	/** adds by given property with the given sorting direction.
	 * @param property the property
	 * @param direction the direction
	 * @return this
	 */
	public QueryConstraints orderBy(String property, SortDirection direction) {
		orderBy().add(property, direction);
		return this;
	}
	
	/** joins new entity to current query.
	 * @param joinProperty source's entity property name
	 * @param restrictions restriction that restricts joined data
	 * @return this
	 * @see Join
	 */
	public QueryConstraints join(String joinProperty, Restrictions restrictions) {
		where().getConditions().join(joinProperty, restrictions);
		return this;
	}

	/** adds new join if such does not exist, or adds restriction to existing join if already exists.
	 * @param joinProperty property name to join
	 * @param restrictions the restriction
	 * @return this instance
	 * @see Conditions#addJoin(String, Restrictions)
	 */
	public QueryConstraints addJoin(String joinProperty, Restrictions restrictions) {
		where().getConditions().addJoin(joinProperty, restrictions);
		return this;
	}
	
	/** joins new entity to current query.
	 * @param joinProperty source's entity property name
	 * @param restrictions restriction that restricts joined data
	 * @param alias alias that joined entity will appear under
	 * @return this
	 * @see Join
	 */
	public QueryConstraints join(String joinProperty, String alias, Restrictions restrictions) {
		getWhere().getConditions().join(joinProperty, restrictions);
		return this;
	}

	/**
	 * @return the alias that will be used in SQL query for main entity.
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public QueryConstraints setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	/**
	 * @return the eagerLoad
	 */
	public List<String> getEagerLoad() {
		return eagerLoad;
	}

	/**
	 * sets list of properties of basic entity that should be loaded after query is performed.
	 * 
	 * @param eagerLoad the eagerLoad to set
	 */
	public void setEagerLoad(List<String> eagerLoad) {
		this.eagerLoad = eagerLoad;
	}

	public void eager(String... eagerLoad) {
		if(eagerLoad != null) {
			String[] as = Texts.removeEmpty(eagerLoad);
			if(as.length > 0) {
				setEagerLoad(Arrays.asList(as));
			}
		}
	}
	


}
package net.fishear.web.t5.data;

import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.order.SortDirection;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.Globals;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.slf4j.Logger;


public class 
	PagingDataSource
implements 
	GridDataSource
{

	private static final Logger log = Globals.getLogger();
	
	private List<?> list;
	private int lastStartIndex;

	private SourceWrapperI sourceWrapperI;
	private QueryConstraints queryConstraints;

	private final Class<? extends EntityI<?>> rowType;

	public PagingDataSource(ServiceI<? extends EntityI<?>> listService) {
		this.rowType = listService.getEntityType();
		setQueryConstraint(null);
		setListService(listService);
	}

	public PagingDataSource(Class<? extends EntityI<?>> rowType) {
		this.rowType = rowType;
		setQueryConstraint(null);
	}

	/** sets constraints used for subsequent queries.
	 * @param queryConstraints
	 * @return this isnstance (to allow chaining)
	 */
	public PagingDataSource setQueryConstraint(QueryConstraints queryConstraints) {
		if(queryConstraints == null) {
			this.queryConstraints = QueryFactory.createDefault();
		} else {
			this.queryConstraints = queryConstraints;
		}
		return this;
	}

	public PagingDataSource setRestrictions(Restrictions restrictions) {
		if(restrictions != null) {
			queryConstraints = QueryFactory.create(restrictions);
		}
		return this;
	}

	public PagingDataSource setConditions(Conditions conditions) {
		if(conditions != null) {
			queryConstraints = QueryFactory.create(conditions);
		}
		return this;
	}

	public QueryConstraints getQueryConstraint() {
		return this.queryConstraints;
	}

	public void setSourceWrapper(SourceWrapperI wrapper) {
		if (wrapper == null) {
			throw new NullPointerException("Source wrapper must not be null");
		}
		this.sourceWrapperI = wrapper;
	}

	public SourceWrapperI getSourceWrapper() {
		return this.sourceWrapperI;
	}

	public int getAvailableRows() {
		log.debug("Getting record count using query constraints: {}", this.queryConstraints.where());
		int result = (int) sourceWrapperI.getItemsCount(this.queryConstraints);
		log.debug("Record count is {}", result);
		return result;
	}

	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		// save start index for iterating during rendering
		this.lastStartIndex = startIndex;
		QueryConstraints qc = setupConstraints(startIndex, endIndex, sortConstraints);
		if(log.isDebugEnabled()) {
			log.debug(String.format("Preparing data: startIndex=%s, endIndex=%s, QueryConstraints: %s",  startIndex, endIndex, qc));
		}
		this.list = sourceWrapperI.getItems(qc);
	}

	public Object getRowValue(int rowIndex) {
		int index = rowIndex - lastStartIndex;

		if (index < 0 || list == null || index >= list.size()) {
			return null;
		}
		return list.get(index);
	}

	public Class<? extends EntityI<?>> getRowType() {
		return rowType;
	}

	private QueryConstraints setupConstraints(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		QueryConstraints qc = QueryFactory.copyOrCreate(this.queryConstraints);
		setupPaging(startIndex, endIndex, qc);
		setupSorting(sortConstraints, qc);
		return qc;
	}

	private void setupSorting(List<SortConstraint> sortConstraints, QueryConstraints constraints) {

		OrderBy orderBy = constraints.orderBy();
		orderBy.getSortedProperties().clear();
		List<String> added = new ArrayList<String>();

		if (sortConstraints != null) {
			for (SortConstraint sc : sortConstraints) {

				String propertyName = sc.getPropertyModel().getPropertyName();
				if(propertyName.equals("idString")) {
					propertyName = "id";
				}
				if(added.contains(propertyName)) {
					continue;
				}
				added.add(propertyName);

				if (sc.getColumnSort() == ColumnSort.ASCENDING) {
					orderBy.add(propertyName, SortDirection.ASCENDING);
				} else {
					assert sc.getColumnSort() == ColumnSort.DESCENDING;
					orderBy.add(propertyName, SortDirection.DESCENDING);
				}
			}
		}
	}

	private void setupPaging(int startIndex, int endIndex, QueryConstraints constraints) {

		int rowCount = endIndex - startIndex + 1;
		constraints.results().setFirstResultIndex(startIndex);
		constraints.results().setResultsPerPage(rowCount);
	}

// *********************************************************************
	public void setListService(ServiceI<? extends EntityI<?>> listService) {
		setSourceWrapper(new DefaultSourceWrapper(listService));
	}

	public interface Transformation
	{
		public Object doTransform(Object o);
	}
}

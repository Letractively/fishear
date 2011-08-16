package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.Coercions;
import net.fishear.web.t5.base.AbstractComponent;
import net.fishear.web.t5.base.SearchFormI;
import net.fishear.web.t5.base.SearchableI;
import net.fishear.web.t5.base.ServiceSourceI;
import net.fishear.web.t5.data.PagingDataSource;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.grid.GridDataSource;

public abstract class 
	AbstractGrid<T extends EntityI<?>>
extends
	AbstractComponent
implements 
	ServiceSourceI<T>,
	SearchableI<T>,
	FormContainerI<T>
{

	private SearchFormI<T> searchComponent;

	private DetailFormI<T> detailForm;

	private T row;
	
	/** ethod to simplify grid script.
	 * It the "fe.table" has no T5 ID (= no attribute t:id is put into tml), then it has degault id "table".
	 * The data source is linked automatically via this ID, then it is not needed write "source=..." attribute. 
	 * In other case (if t:id=... is set), thr table's attribute "source=..." has to be set too.
	 * @return data source
	 */
	public GridDataSource getTable() {
		return getDataSource();
	}
	
	@Cached
	public GridDataSource getDataSource() {
		PagingDataSource dataSource = new PagingDataSource(getService());
		if(searchComponent != null) {
			dataSource.setQueryConstraint(QueryFactory.create(searchComponent.getSearchConstraints()));
		}
		return dataSource;
	}

	/** 
	 * @param id
	 * @return
	 */
	@OnEvent(component = "Edit")
	public Object onActionFromEdit(String id) {
		return onEdit(id);
	}
	
	/** event handler
	 * @param id
	 * @return
	 */
	@Override
	public Object onEdit(String id) {
		detailForm.load(coerce(id));
		return detailForm;
	}

	@OnEvent(component = "Delete")
	public Object onActionFromDelete(String id) {
		return onDelete(id);
	}

	@Override
	public Object onDelete(String idStr) {
		Object id = coerce(idStr);
		getService().getDao().transaction();
		if(getService().delete(id)) {
			setMessageText(getMessage("record-successfully-deleted-label"));
		} else {
			setMessageText(getMessage("record-record-does-not-exist-label"));
		}
		getService().getDao().commit();
		return this;
	}

	@Override
	public ServiceI<T> getService() {
		if(detailForm != null) {
			return detailForm.getService();
		}
		if(searchComponent != null) {
			return searchComponent.getService();
		}
		throw new IllegalStateException("Descendent of AbstractGrid has to implement 'getService' method to get service, or has to contain component implementing 'DetailFormI' or 'SearchFormI'");
	}

	private Object coerce(String idStr) {
		return Coercions.convertType(idStr, Long.class);
	}

	public DetailFormI<T> getDetailForm() {
		return detailForm;
	}

	public SearchFormI<T> getSearchComponent() {
		return searchComponent;
	}

	public void setSearchComponent(SearchFormI<T> searchComponent) {
		this.searchComponent = searchComponent;
	}

	@Override
	public void setDetailForm(AbstractForm<T> form) {
		this.detailForm = form;
	}

	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
	}
}

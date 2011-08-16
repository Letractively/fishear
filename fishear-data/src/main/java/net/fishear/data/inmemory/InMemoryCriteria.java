package net.fishear.data.inmemory;

import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.query.order.SortDirection;
import net.fishear.utils.Lists;

public class InMemoryCriteria
{

	private InMemoryDao<?> dao;

	private List<Sort> sortList = new ArrayList<Sort>();
	
	private int fromIndex;
	private int toIndex;

	public InMemoryCriteria(InMemoryDao<?> inMemoryDao) {
		this.dao = inMemoryDao;
	}

	public List<?> list() {

		List<?> resList = dao.dataList;
		return Lists.sublist(resList, fromIndex, toIndex);
	}

	public void setResults(int fromIndex, int rowsPerPage) {
		this.fromIndex = fromIndex;
		this.toIndex = fromIndex + rowsPerPage;
	}

	public void addOrder(String propertyName, SortDirection sortDirection) {
		sortList.add(new Sort(propertyName, sortDirection));
	}

	
	@SuppressWarnings("unused")
	private static class Sort {
		final String propertyName;
		final SortDirection sortDirection;
		public Sort(String propertyName, SortDirection sortDirection) {
			super();
			this.propertyName = propertyName;
			this.sortDirection = sortDirection;
		}
	}
}

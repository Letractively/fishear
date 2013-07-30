package net.fishear.web.t5.base;

import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.services.AbstractService;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.data.inmemory.InMemoryDaoSource;
import net.fishear.web.t5.components.AbstractSearch;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.testng.annotations.Test;

public class GenericMasterDetailComponentTest {

	
	static {
		DaoSourceManager.registerDaoSource(new InMemoryDaoSource(), "inmemory");
		DaoSourceManager.setDefaultDaoSource("inmemory");
	}

	@Test
	public void basicTest() {
		MDCImpl_1 md1 = new MDCImpl_1();
		Search_1 ss = new Search_1(md1);
		md1.service = new Service_1();
		md1.setSearchComponent(ss);

		md1.getDataSource();
	}
	
	@Test
	public void badEntityTestTest() {
		MDCImpl_1 md1 = new MDCImpl_1();
		AbstractSearch<?> ss = new Search_1(md1);
		md1.service = new Service_1();
		md1.setSearchComponent((SearchFormI<Entity_1>) ss);

		md1.getDataSource();
	}
	

/* ************************************************************************************** */
	
	public static class Entity_1 extends AbstractEntity {
		
	}
	
	public static class Entity_2 extends AbstractEntity {
		
	}

	public static class Service_1 extends AbstractService<Entity_1> {
	}

	public static class MDCImpl_1 extends AbstractGridDetailComponent<Entity_1> {
		Service_1 service;
		@Override
		public ServiceI<Entity_1> getService() { return service; }
		@Override
		protected void beforeSave(Entity_1 entity) { }
	}
	
	public static class MDCImpl_2 extends AbstractGridDetailComponent<Entity_2> {
		@Override
		public ServiceI<Entity_2> getService() { return null; }
		@Override
		protected void beforeSave(Entity_2 entity) { }
	}
	
	public static class Search_1 extends AbstractSearch<Entity_1> {

		public Search_1(MDCImpl_1 md1) {
			super.setSearchable(md1);
		}
//		@Override
//		public ServiceI<Entity_1> getService() { return null; }
	}
	
}

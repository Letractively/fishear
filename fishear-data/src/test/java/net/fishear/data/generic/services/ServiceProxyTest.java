package net.fishear.data.generic.services;

import java.util.List;

import net.fishear.data.generic.Utils;
import net.fishear.data.generic.data.TestEntity;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.inmemory.InMemoryDaoSource;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServiceProxyTest
{
	
	@BeforeClass
	void init() {
		
		Utils.clearMgr();
		
		InMemoryDaoSource.init();
		
	}
	
	@Test
	public void doTest() {
		
		ServiceI<TestEntity> svc = ServiceFactory.createService(TestEntity.class);

		TestEntity e1 = svc.newEntityInstance();
		e1.setName("E1");
		svc.save(e1);

		TestEntity ee = svc.read(e1.getId());
		assertEquals(e1, ee);
		
		TestEntity e2 = svc.newEntityInstance();
		e2.setName("E2");
		svc.save(e2);
		
		TestEntity e3 = svc.newEntityInstance();
		e3.setName("E3");
		svc.save(e3);
		
		TestEntity e4 = svc.newEntityInstance();
		e4.setName("E4");
		svc.save(e4);

		List<TestEntity> list = svc.list(null);
		assertEquals(list.size(), 4);

		QueryConstraints qc = QueryFactory.create();
		qc.results().setFirstResultIndex(1);
		qc.results().setResultsPerPage(2);
		List<TestEntity> list2 = svc.list(qc);
		
		assertEquals(list2.size(), 2);

		svc.delete(e2);
		
	}

}

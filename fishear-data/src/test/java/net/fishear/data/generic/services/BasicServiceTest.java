package net.fishear.data.generic.services;

import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.fishear.data.generic.Utils;
import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.BasicServiceTest.SampleEntity;
import net.fishear.data.inmemory.InMemoryDaoSource;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BasicServiceTest
{

	@BeforeClass
	void init() {
		
		Utils.clearMgr();
		
		InMemoryDaoSource.init();
		
	}
	
	@Test
	public void test() {

		GenericService<SampleEntity> svc = new GenericService<SampleEntity>() {
		};
		
		SampleEntity e1 = svc.newEntityInstance();
		e1.setName("N1");
		Long id1 = (Long) svc.save(e1);
		assertNotNull(id1);
		
		SampleEntity e2 = svc.newEntityInstance();
		e1.setName("N2");
		Long id2 = (Long) svc.save(e2);

		SampleEntity e3 = svc.newEntityInstance();
		e1.setName("N3");
		Long id3 = (Long) svc.save(e3);

		SampleEntity e4 = svc.newEntityInstance();
		e1.setName("N4");
		Long id4 = (Long) svc.save(e4);

		SampleEntity e5 = svc.newEntityInstance();
		e1.setName("N5");
		Long id5 = (Long) svc.save(e5);

		assertEquals(svc.list(null).size(), 5);
		
		SampleEntity ee = svc.read(id1);
		
		assertEquals(ee, e1);
		
		QueryConstraints qc = QueryFactory.create();
		qc.results().setFirstResultIndex(2);
		qc.results().setResultsPerPage(2);
		List<SampleEntity> list5 = svc.list(qc);
		assertEquals(list5.size(), 2);
		assertEquals(list5.get(0), e3);
	}
	
	
	public static class SampleEntity extends AbstractEntity {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}

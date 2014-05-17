package net.fishear.data.generic.dao;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;

import net.fishear.data.generic.Utils;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.services.GenericService;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings({"rawtypes", 	"unchecked"})
public class DaoTest
{

	private GenericDaoI emd1 = mock(GenericDaoI.class);
	private GenericDaoI emd2 = mock(GenericDaoI.class);

	@BeforeClass
	void init() {
		Utils.clearMgr();
		
		DaoSourceI dsi1 = mock(DaoSourceI.class);
		when(dsi1.createDao(SampleEntity.class)).thenReturn(emd1);
		DaoSourceManager.registerDaoSource(dsi1, "default");

		DaoSourceI dsi2 = mock(DaoSourceI.class);
		DaoSourceManager.registerDaoSource(dsi2, "dao2");
		when(dsi2.createDao(SampleEntity.class)).thenReturn(emd2);
	}
	
	@Test
	void normalDaoTest() {


		GenericDaoI<SampleEntity> dao0 = DaoSourceManager.createDao(SampleEntity.class);
		assertEquals(dao0.getClass(), emd1.getClass());

		GenericDaoI<SampleEntity> dao1 = DaoSourceManager.createDao(SampleEntity.class, SampleService2.class);
		assertEquals(dao1.getClass(), emd1.getClass());

		DaoSourceManager.createDao(SampleEntity.class, SampleService1.class);

		GenericDaoI<SampleEntity> dao2 = DaoSourceManager.createDao(SampleEntity.class, SampleService1.class);
		assertEquals(dao2.getClass(), emd2.getClass());

		GenericDaoI<SampleEntity> dao3 = DaoSourceManager.createDao(SampleEntity.class, SampleService3.class);
		assertEquals(dao3.getClass(), emd1.getClass());

	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void missingDaoTest() {
		DaoSourceManager.createDao(SampleEntity.class, SampleServiceX.class);
	}

	@PersistenceContext(unitName = "dao2")
	public static class SampleService1 extends GenericService<SampleEntity>
	{

	}

	@PersistenceContext(name = "aaaa")
	public static class SampleService2 extends GenericService<SampleEntity>
	{

	}

	@PersistenceContext(name = "default")
	public static class SampleService3 extends GenericService<SampleEntity>
	{

	}

	@PersistenceContext(unitName = "aaaa")
	public static class SampleServiceX extends GenericService<SampleEntity>
	{

	}

	@Entity
	public static class SampleEntity extends AbstractEntity
	{
	}
}

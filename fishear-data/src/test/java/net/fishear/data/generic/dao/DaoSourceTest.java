package net.fishear.data.generic.dao;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import net.fishear.data.generic.Utils;

import org.testng.annotations.Test;

public class DaoSourceTest
{

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void noNameTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource, "");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void duplNameTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource, "MyDao1");
		
		DaoSourceManager.registerDaoSource(daoSource, "mydao1");
	}
	
	@Test//(expectedExceptions = IllegalStateException.class)
	public void getByNameTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource, "MyDao1");

		assertEquals(DaoSourceManager.getDaoSource("mydao1"), daoSource);
		
		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource);
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void duplInstanceTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource, "MyDao1");
		
		DaoSourceManager.registerDaoSource(daoSource, "MyDaoX");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void unregisteredDefaultSourceTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource1 = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource1, "MyDao1");

		assertEquals(DaoSourceManager.getDaoSource("mydao1"), daoSource1);

		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource1);

		DaoSourceManager.setDefaultDaoSource("anyOtherSource");
	}
	
	@Test//(expectedExceptions = IllegalArgumentException.class)
	public void complexDaoSourceTest() {
		
		Utils.clearMgr();
		
		DaoSourceI daoSource1 = mock(DaoSourceI.class);
		DaoSourceI daoSource2 = mock(DaoSourceI.class);
		DaoSourceI daoSource3 = mock(DaoSourceI.class);

		DaoSourceManager.registerDaoSource(daoSource1, "MyDao1");

		assertEquals(DaoSourceManager.getDaoSource("mydao1"), daoSource1);
		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource1);
		
		DaoSourceManager.registerDaoSource(daoSource2, "MyDao2");
		assertEquals(DaoSourceManager.getDaoSource("mydao2"), daoSource2);
		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource1);
		
		DaoSourceManager.registerDaoSource(daoSource3, "MyDao3");
		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource1);

		DaoSourceManager.setDefaultDaoSource("mydao3");
		assertEquals(DaoSourceManager.getDefaultDaoSource(), daoSource3);
	}
	
}

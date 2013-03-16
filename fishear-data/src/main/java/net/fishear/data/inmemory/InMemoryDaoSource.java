package net.fishear.data.inmemory;

import net.fishear.data.generic.dao.ClassBasedDaoSource;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.entities.EntityI;

public class InMemoryDaoSource extends ClassBasedDaoSource
{
	
//	static {
//		if(DaoSourceManager.getDefaultDaoSource() == null) {
//			DaoSourceManager.setDefaultDaoSource(new InMemoryDaoSource());
//		}
//	}

	public InMemoryDaoSource() {
		super(InMemoryDao.class);
	}

	public static void init() {
		DaoSourceManager.registerDaoSource(new InMemoryDaoSource(), "inmemory");
	}

	@Override
	public boolean supportsAutoregistrationEntities() {
		return false;
	}

	@Override
	public void registerEntity(Class<EntityI<?>> entity) {
		
	}

	@Override
	public void registerPackage(String packageName) {

	}

	@Override
	public boolean supportsAutoregistrationPackages() {
		return false;
	}
}

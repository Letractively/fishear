package net.fishear.data.generic;

import java.lang.reflect.Field;
import java.util.Map;

import net.fishear.data.generic.dao.DaoSourceManager;

public class Utils
{
	public static void clearMgr() {
		
		try {
			Field fld1 = DaoSourceManager.class.getDeclaredField("daoSources");
			fld1.setAccessible(true);
			((Map<?, ?>)fld1.get(null)).clear();
			
			Field fld2 = DaoSourceManager.class.getDeclaredField("defaultDaoSource");
			fld2.setAccessible(true);
			fld2.set(null, null);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	

}

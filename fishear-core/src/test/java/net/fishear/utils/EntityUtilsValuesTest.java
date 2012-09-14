package net.fishear.utils;

import static org.testng.Assert.*;
import net.fishear.exceptions.AppException;

import org.testng.annotations.Test;

public class EntityUtilsValuesTest {

	@Test
	public void getValueTest() {
		
		Cl1 c1 = new Cl1();

		assertNull(EntityUtils.getAttribute("valStr", c1));

		c1.setValStr("SSss");

		assertEquals(EntityUtils.getAttribute("valStr", c1), "SSss");

	}

	@Test(expectedExceptions = AppException.class)
	public void noAttrExceptionTest() {
		
		Cl1 c1 = new Cl1();

		EntityUtils.getAttribute("valStrBBBB", c1);

	}
	
	
	@Test
	public void noExceptionTest() {
		
		Cl1 c1 = new Cl1();

		assertEquals(EntityUtils.getValue("valStrBBBB", c1, "1234"), "1234");

		assertNull(EntityUtils.getAttribute("valStr", c1));

		c1.setValStr("SSss");

		assertEquals(EntityUtils.getAttribute("valStr", c1), "SSss");
		
		
	}
	
	
	public static class Cl1 {
		
		private String valStr;

		/**
		 * @return the valStr
		 */
		public String getValStr() {
			return valStr;
		}

		/**
		 * @param valStr the valStr to set
		 */
		public void setValStr(String valStr) {
			this.valStr = valStr;
		}
		
		
	}
	
}

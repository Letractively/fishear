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

	public static class ParentEnt {
		
		private MyEntity myEntity = new MyEntity("MYENT 1");
		private MyEntity myEntity2 = new MyEntity("MYENT 2");

		public MyEntity getMyEntity() {
			return myEntity;
		}

		public void setMyEntity(MyEntity myEntity) {
			this.myEntity = myEntity;
		}

		public MyEntity getMyEntity2() {
			return myEntity2;
		}

		public void setMyEntity2(MyEntity myEntity2) {
			this.myEntity2 = myEntity2;
		}
	}

	@Test
	public void getSetValueTest() {
		
		ParentEnt pe = new ParentEnt();

		assertEquals(EntityUtils.getValue("myEntity.mystr", pe, null), "MYENT 1");
		assertEquals(EntityUtils.getValue("myEntity2.mystr", pe, null), "MYENT 2");
		
		EntityUtils.setValue(pe, "myEntity2.mystr", "ABCD");

		assertEquals(EntityUtils.getValue("myEntity2.mystr", pe, null), "ABCD");
		assertEquals(EntityUtils.getValue("myEntity.mystr", pe, null), "MYENT 1");
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

	public static class MyEntity {
		
		private String mystr = "My Str Value";

		private String myText = "My Str Value";
		
		private Cl1 cl1 = new Cl1();

		private Cl1 cl2 = new Cl1();

		public MyEntity(String string) {
			mystr = string;
		}

		/**
		 * @return the mystr
		 */
		public String getMystr() {
			return mystr;
		}

		/**
		 * @param mystr the mystr to set
		 */
		public void setMystr(String mystr) {
			this.mystr = mystr;
		}

		/**
		 * @return the myText
		 */
		public String getMyText() {
			return myText;
		}

		/**
		 * @param myText the myText to set
		 */
		public void setMyText(String myText) {
			this.myText = myText;
		}

		/**
		 * @return the cl1
		 */
		public Cl1 getCl1() {
			return cl1;
		}

		/**
		 * @param cl1 the cl1 to set
		 */
		public void setCl1(Cl1 cl1) {
			this.cl1 = cl1;
		}

		/**
		 * @return the cl2
		 */
		public Cl1 getCl2() {
			return cl2;
		}

		/**
		 * @param cl2 the cl2 to set
		 */
		public void setCl2(Cl1 cl2) {
			this.cl2 = cl2;
		}
		
	}

}

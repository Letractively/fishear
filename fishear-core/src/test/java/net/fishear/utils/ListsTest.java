package net.fishear.utils;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ListsTest {

	static class ListIt {
		
		private String val1;
		
		private Double val2;

		/**
		 * @return the val1
		 */
		public String getVal1() {
			return val1;
		}

		/**
		 * @param val1 the val1 to set
		 */
		public void setVal1(String val1) {
			this.val1 = val1;
		}

		/**
		 * @return the val2
		 */
		public Double getVal2() {
			return val2;
		}

		/**
		 * @param val2 the val2 to set
		 */
		public void setVal2(Double val2) {
			this.val2 = val2;
		}
	}
	
	List<ListIt> newist() {
	
		List<ListIt> list = new ArrayList<ListIt>();
		
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		list.add(new ListIt());
		
		return list;
	}
	
	@Test
	public void fillItemsTest() {
		List<ListIt> list = newist();
		
		Lists.set(list, "val2", -100D);
		
		for(ListIt it : list) {
			assertEquals(it.getVal2(), -100D);
		}
		
		Lists.set(list, "val2", 20D);
		
		for(ListIt it : list) {
			assertNull(it.getVal1());
			assertEquals(it.getVal2(), 20D);
		}
		Lists.set(list, "val1", "ABCD");
		for(ListIt it : list) {
			assertEquals(it.getVal1(), "ABCD");
			assertEquals(it.getVal2(), 20D);
		}
	}
	
	@Test
	public void fillErrorTest() {
		List<ListIt> list = newist();
		Lists.set(list, "val2", "22");
		for(ListIt it : list) {
			assertEquals(it.getVal2(), null);
		}
	}
	
	
}

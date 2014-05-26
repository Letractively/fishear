package net.fishear.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ListsTest {

	static class ListIt {
		
		private String val1;
		
		private Double val2;
		
		private int numInt; 
		
		private long numLong;
		
		private Integer numInt2;

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

		/**
		 * @return the numInt
		 */
		public int getNumInt() {
			return numInt;
		}

		/**
		 * @param numInt the numInt to set
		 */
		public void setNumInt(int numInt) {
			this.numInt = numInt;
		}

		/**
		 * @return the numLong
		 */
		public long getNumLong() {
			return numLong;
		}

		/**
		 * @param numLong the numLong to set
		 */
		public void setNumLong(long nulLong) {
			this.numLong = nulLong;
		}

		/**
		 * @return the numInt2
		 */
		public Integer getNumInt2() {
			return numInt2;
		}

		/**
		 * @param numInt2 the numInt2 to set
		 */
		public void setNumInt2(Integer numInt2) {
			this.numInt2 = numInt2;
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
	
	@Test
	public void sumTest() {
		List<ListIt> list = newist();
		list.get(0).setNumLong(10);
		list.get(2).setNumLong(20);
		list.get(4).setNumLong(30);
		assertEquals(Lists.sum(list, "numLong"), new BigDecimal(60));
		
		list.get(1).setNumInt2(20);
		list.get(3).setNumInt2(40);
		list.get(5).setNumInt2(60);
		assertEquals(Lists.sum(list, "numInt2"), new BigDecimal(120));

	}
	
	@Test
	public void toMapTest() {
		List<ListIt> list = newist();
		int cnt = 0;
		for(ListIt it : list) {
			it.setVal2(((double)cnt + 1) * 10);
			it.setVal1("THEKEY_"+cnt);
			cnt++;
		}
		
		Map<String, ListIt> map1 = Lists.toKeyEntityMap(list, "val1");
		Map<Double, ListIt> map2 = Lists.toKeyEntityMap(list, "val2");

		assertEquals(map1.get("THEKEY_4").getVal1(), "THEKEY_4");
		assertEquals(map2.get(20D).getVal1(), "THEKEY_1");
		
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void toMapNullKeyTest() {
		List<ListIt> list = newist();
		int cnt = 0;
		for(ListIt it : list) {
			it.setVal2(((double)cnt + 1) * 10);
			it.setVal1("THEKEY_"+cnt);
			cnt++;
			if(cnt > 2) {
				break;
			}
		}
		Lists.toKeyEntityMap(list, "val1");
		
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void toMapDuplityTest() {
		List<ListIt> list = newist();
		int cnt = 0;
		for(ListIt it : list) {
			it.setVal2(((double)cnt + 1) * 10);
			it.setVal1("THEKEY_"+cnt);
			if(cnt == 4) {
				it.setVal1("THEKEY_"+(cnt - 1));
			}
			cnt++;
		}

		Lists.toKeyEntityMap(list, "val1");

	}
	
	
}

package net.fishear.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ListsTest {

	static class ListKey {

		public ListKey(int id) {
			setId((long)id);
			setText("the-"+id+"text");
		}

		private Long id;
		
		private String text;

		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
		
	}
	
	static class ListIt {

		private String key;
		
		private String val1;
		
		private Double val2;
		
		private int numInt; 
		
		private long numLong;
		
		private Integer numInt2;

		
		private ListKey listKey;
		
		public ListIt() {
		}
		
		public ListIt(int num) {
			setKey("k-"+num);
			setVal1("VAL_"+num);
			setVal2((double)num);
		}
		
		public ListIt(String key, String val1) {
			
		}
		
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

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the listKey
		 */
		public ListKey getListKey() {
			return listKey;
		}

		/**
		 * @param listKey the listKey to set
		 */
		public void setListKey(ListKey listKey) {
			this.listKey = listKey;
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

	@Test
	public void groupListStringTest() {
		List<ListIt> list = newist();
		list.get(0).setKey("KEY1");
		list.get(1).setKey("KEY1");
		list.get(2).setKey("KEY2");
		list.get(3).setKey("KEY3");
		list.get(4).setKey("KEY3");
		list.get(5).setKey("KEY3");
		list.get(6).setKey(null);
		list.get(7).setKey(null);
		
		Map<String, List<ListIt>> map = Lists.group(list, "key");
		
		assertEquals(map.size(), 4);
		
		assertEquals(map.get(null).size(), 2);
		assertEquals(map.get("KEY3").size(), 3);
	}

	@Test
	public void groupListObjTest() {
		List<ListIt> list = newist();
		
		ListKey lk1 = new ListKey(1);
		list.get(0).setListKey(lk1);
		list.get(1).setListKey(lk1);
		list.get(2).setListKey(lk1);
		list.get(3).setListKey(lk1);
		
		ListKey lk2 = new ListKey(1);
		list.get(4).setListKey(lk2);
		list.get(5).setListKey(lk2);
		list.get(6).setListKey(lk2);
		list.get(7).setListKey(null);

		Map<String, List<ListIt>> map = Lists.group(list, "listKey");
		
		assertEquals(map.size(), 3);
		
		assertEquals(map.get(null).size(), 1);
		assertEquals(map.get(lk1).size(), 4);
		assertEquals(map.get(lk2).size(), 3);
	}
	
}

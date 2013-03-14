package net.fishear.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.fishear.utils.EntityUtils.DiffFlags;
import net.fishear.utils.EntityUtils.Property;
import net.fishear.utils.data.EqualsAndDiffEntity;
import static net.fishear.utils.data.EqualsAndDiffEntity.*;

import static org.testng.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;



public class EntityUtilsTestDiff {

	Logger log = LoggerFactory.getLogger(getClass());
	
	
	ArrayList<Property> fillDiffs() {
		ArrayList<Property> ll = new ArrayList<Property>();
		ll.add(new Property("str1", "AAAA", "aaaa"));
		ll.add(new Property("str2", "BBBB", "bbbb"));
		ll.add(new Property("intO", 111, 99111));
		ll.add(new Property("intP", 222, 99222));
		ll.add(new Property("longO", 1111L, 991111L));
		ll.add(new Property("longP", 2222L, 992222L));
		ll.add(new Property("dblO", 111.5D, 99111.5D));
		ll.add(new Property("dblP", 222.5D, 99222.5D));
		ll.add(new Property("date1", new Date(100000L), new Date(99100000L)));
		ll.add(new Property("date2", new Date(10000000L), new Date(9910000000L)));
		return ll;
	}

	@Test
	public void allDiffsTest() {
		
		EqualsAndDiffEntity[] te = fillEntities();
		
		List<Property> list = EntityUtils.listDifferencies(te[0], te[1]);

		ArrayList<Property> ll = fillDiffs();
		
		assertEquals(list.size(), 10);
		assertEquals(list, ll);
		
	}

	@Test
	public void allEquelTest() {

		EqualsAndDiffEntity[] ta1 = fillEntities();
		EqualsAndDiffEntity[] ta2 = fillEntities();
		List<Property> list = EntityUtils.listDifferencies(ta1[0], ta2[0]);
		assertEquals(list.size(), 0);
	}

	@Test
	public void allEquelButTransientTest() {

		EqualsAndDiffEntity[] ta1 = fillEntities();
		EqualsAndDiffEntity[] ta2 = fillEntities();
		
		ta1[0].setTransientField("XXXXYYYY");
		ta2[0].setTransientField("ABCDKOCKAPREDE");
		
		ta1[0].setTransientGetter("XXXXzzzz");
		ta2[0].setTransientField("NENENENE");
		
		List<Property> list = EntityUtils.listDifferencies(ta1[0], ta2[0]);
		assertEquals(list.size(), 0);
	}

	@Test
	public void transientDiffsTest() {

		EqualsAndDiffEntity[] ta = { fillEntities()[0], fillEntities()[0] };
		
		ta[0].setTransientField("XXXXYYYY");
		ta[1].setTransientField("ABCDKOCKAPREDE");
		
		ta[0].setTransientGetter("XXXXzzzz");
		ta[1].setTransientField("NENENENE");

		List<Property> list = EntityUtils.listDifferencies(ta[0], ta[1], DiffFlags.COMPARE_TRANSIENT);
		assertEquals(list.size(), 2);
	}

	@Test
	public void nullDiffsTest() {
		
		EqualsAndDiffEntity[] ta = { fillEntities()[0], fillEntities()[0] };

		ta[0].setStr1(null);
		ta[1].setStr2(null);

		List<Property> list = EntityUtils.listDifferencies(ta[0], ta[1]);

		ArrayList<Property> ll = new ArrayList<Property>();
		ll.add(new Property("str1", null, "AAAA"));
		ll.add(new Property("str2", "BBBB", null));
		
		assertEquals(list.size(), 2);
		assertEquals(list, ll);
		
	}

	@Test
	public void nullEqualTest() {
		
		EqualsAndDiffEntity[] ta = { fillEntities()[0], fillEntities()[0] };

		ta[0].setStr1(null);
		ta[0].setStr2(null);
		ta[1].setStr1(null);
		ta[1].setStr2(null);

		List<Property> list = EntityUtils.listDifferencies(ta[0], ta[1]);

		assertEquals(list.size(), 0);
		
	}

}

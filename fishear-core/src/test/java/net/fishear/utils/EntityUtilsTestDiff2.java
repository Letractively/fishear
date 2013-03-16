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



public class EntityUtilsTestDiff2 {

	Logger log = LoggerFactory.getLogger(getClass());
	
	
	ArrayList<Property> fillDiffs(boolean isTarget) {
		ArrayList<Property> ll = new ArrayList<Property>();
		if(isTarget) {
			ll.add(new Property("id", null, null));
			ll.add(new Property("str1", null, "aaaa"));
			ll.add(new Property("str2", null, "bbbb"));
			ll.add(new Property("intO", null, 99111));
			ll.add(new Property("intP", null, 99222));
			ll.add(new Property("longO", null, 991111L));
			ll.add(new Property("longP", null, 992222L));
			ll.add(new Property("dblO", null, 99111.5D));
			ll.add(new Property("dblP", null, 99222.5D));
			ll.add(new Property("date1", null, new Date(99100000L)));
			ll.add(new Property("date2", null, new Date(9910000000L)));
			ll.add(new Property("type", null, null));
		} else {
			ll.add(new Property("id", null, null));
			ll.add(new Property("str1", "AAAA", null));
			ll.add(new Property("str2", "BBBB", null));
			ll.add(new Property("intO", 111, null));
			ll.add(new Property("intP", 222, null));
			ll.add(new Property("longO", 1111L, null));
			ll.add(new Property("longP", 2222L, null));
			ll.add(new Property("dblO", 111.5D, null));
			ll.add(new Property("dblP", 222.5D, null));
			ll.add(new Property("date1", new Date(100000L), null));
			ll.add(new Property("date2", new Date(10000000L), null));
			ll.add(new Property("type", null, null));
		}

		return ll;
	}

	@Test
	public void allDiffsSourceTest() {
		
		EqualsAndDiffEntity[] te = fillEntities();

		List<Property> list = EntityUtils.fillDifferencies(te[0], false);

		ArrayList<Property> ll = fillDiffs(false);
		
		assertEquals(list.size(), 12);
		assertEquals(list, ll);
		
	}

	@Test
	public void allDiffsTargetTest() {
		
		EqualsAndDiffEntity[] te = fillEntities();

		List<Property> list = EntityUtils.fillDifferencies(te[1], true);

		ArrayList<Property> ll = fillDiffs(true);
		
		assertEquals(list.size(), 12);
		assertEquals(list, ll);
		
	}

	@Test
	public void allDiffsTargetTransientTest() {
		
		EqualsAndDiffEntity[] te = fillEntities();

		List<Property> list = EntityUtils.fillDifferencies(te[1], true, DiffFlags.COMPARE_TRANSIENT);

		assertEquals(list.size(), 14);
		
	}

}

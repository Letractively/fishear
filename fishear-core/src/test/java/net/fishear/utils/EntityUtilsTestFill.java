package net.fishear.utils;

import net.fishear.utils.data.TestEntity;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class EntityUtilsTestFill {

	
	@Test
	public void mainTest() {
		TestEntity te1 = new TestEntity();
		TestEntity te2 = new TestEntity();

		te1.setId("AAAA");
		te1.setNum1(111L);
		te1.setNum2(222L);
		te1.setVal1("V1V1V1");
		te1.setVal2("V3V3V3");

		te2.setVal1("1V1V1V");
		te2.setVal2("2V2V2V");
		
		EntityUtils.fillDestination(te1, te2, EntityUtils.FillFlags.FILL_ALL);
		assertFalse(EntityUtils.equals(te1, te2));
		assertNotEquals(te1.getId(), te2.getId());

		te1.setId(te2.getId());
		assertTrue(EntityUtils.equals(te1, te2));

		
	}

	@Test
	public void mainTest2() {
		TestEntity te1 = new TestEntity();
		TestEntity te2 = new TestEntity();

		te1.setId("AAAA");
		te1.setNum1(111L);
		te1.setNum2(222L);
		te1.setVal1("V1V1V1");
		te1.setVal2("V3V3V3");

		te2.setVal1("1V1V1V");
		te2.setVal2("2V2V2V");

		EntityUtils.fillDestination(te1, te2, EntityUtils.FillFlags.FILL_EMPTY_ONLY);

		assertNotEquals(te1.getId(), te2.getId());
		assertNotEquals(te1.getVal1(), te2.getVal1());
		assertNotEquals(te1.getVal2(), te2.getVal2());
		
		assertEquals(te1.getVal3(), te2.getVal3());
		assertEquals(te1.getNum1(), te2.getNum1());
		assertEquals(te1.getNum2(), te2.getNum2());

		
	}
	
	@Test
	public void mainTest3() {
		TestEntity te1 = new TestEntity();
		TestEntity te2 = new TestEntity();

		te1.setId("AAAA");
		te1.setNum1(111L);
		te1.setNum2(222L);
		te1.setVal1("V1V1V1");
		te1.setVal2("V3V3V3");

		te2.setVal1("1V1V1V");
		te2.setVal2("2V2V2V");

		EntityUtils.fillDestination(te1, te2);

		assertNotEquals(te1.getId(), te2.getId());
		assertEquals(te1.getVal1(), te2.getVal1());
		assertEquals(te1.getVal2(), te2.getVal2());
		
		assertEquals(te1.getVal3(), te2.getVal3());
		assertEquals(te1.getNum1(), te2.getNum1());
		assertEquals(te1.getNum2(), te2.getNum2());

	}

}

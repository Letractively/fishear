package net.fishear.utils;

import net.fishear.utils.data.TestEntity;
import net.fishear.utils.data.TestEntity2;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class EntityUtilsTestEquals {

	
	private TestEntity fillte1() {
		TestEntity te1 = new TestEntity();
		te1.setId("AAAA");
		te1.setNum1(111L);
		te1.setNum2(222L);
		te1.setVal1("V1V1V1");
		te1.setVal2("V3V3V3");
		return te1;
	}
	
	private TestEntity2 fillte2() {
		TestEntity2 te = new TestEntity2();
		te.setIdNum(2222L);
		te.setName("TE2Name");
		te.setCode("T2C");
		return te;
	}
	
	@Test
	public void simpleTest() {
		
		TestEntity te1 = fillte1();
		TestEntity te2 = fillte1();

		assertTrue(EntityUtils.equals(te1, te2));

		te2.setVal3("XX");
		assertFalse(EntityUtils.equals(te1, te2));
		
		te2.setVal3(null);
		assertTrue(EntityUtils.equals(te1, te2));
		
		te2.setVal1(null);
		assertFalse(EntityUtils.equals(te1, te2));
		
		te2.setVal1(te1.getVal1());
		assertTrue(EntityUtils.equals(te1, te2)); 
		
		te2.setNum3(9999L);
		assertTrue(EntityUtils.equals(te1, te2));	// transient field is ignored
		
		te2.setTestEntity2(fillte2());
		assertFalse(EntityUtils.equals(te1, te2));

		te1.setTestEntity2(fillte2());
		assertTrue(EntityUtils.equals(te1, te2));
		
		te2.getTestEntity2().setName("NENEMWMW");
		assertFalse(EntityUtils.equals(te1, te2));
		
		te1.getTestEntity2().setName("NENEMWMW");
		assertTrue(EntityUtils.equals(te1, te2));
		
	}
	
	
}

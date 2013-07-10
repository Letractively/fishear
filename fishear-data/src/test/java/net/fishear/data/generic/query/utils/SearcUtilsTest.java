package net.fishear.data.generic.query.utils;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import net.fishear.data.generic.annotations.Interval;
import net.fishear.data.generic.annotations.Intervals;
import net.fishear.data.generic.query.conditions.Conditions;

public class SearcUtilsTest {

	
	@Test
	public void intervalTest() {
		
		Entity1 e1 = new Entity1();

		Conditions cond1 = SearchUtils.createSearchConditions(e1, null);
		assertEquals(cond1.toString(), "Conditions: intStrt IS NULL OR intStrt GREATER_OR_EQUAL -100 AND intEnd IS NULL OR intEnd LESS_OR_EQUAL 100");

	}
	
	@Test
	public void intervalsTest1() {
		
		Entity3 e1 = new Entity3();

		Conditions cond1 = SearchUtils.createSearchConditions(e1, null);
		assertEquals(cond1.toString(), "Conditions: intStrt IS NULL OR intStrt GREATER_OR_EQUAL -100 AND intEnd IS NULL OR intEnd LESS_OR_EQUAL 100");
	}
	
	@Test
	public void intervalsTest2() {
		
		Entity4 e1 = new Entity4();

		Conditions cond1 = SearchUtils.createSearchConditions(e1, null);
		assertEquals(cond1.toString(), "Conditions: i1Strt IS NULL OR i1Strt GREATER_OR_EQUAL -100 AND i1End IS NULL OR i1End LESS_OR_EQUAL 100 AND i2Strt IS NULL OR i2Strt GREATER_OR_EQUAL -200 AND i2End IS NULL OR i2End LESS_OR_EQUAL 200");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void badIntervalTest1() {
		
		EntityB1 e1 = new EntityB1();

		Conditions cond = SearchUtils.createSearchConditions(e1, null);
		assertEquals(cond.toString(), "Conditions: intStrt IS NULL OR intStrt GREATER_OR_EQUAL -100 AND intEnd IS NULL OR intEnd LESS_OR_EQUAL 100");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void badIntervalTest2() {
		
		EntityB2 e1 = new EntityB2();

		Conditions cond = SearchUtils.createSearchConditions(e1, null);
		assertEquals(cond.toString(), "Conditions: intStrt IS NULL OR intStrt GREATER_OR_EQUAL -100 AND intEnd IS NULL OR intEnd LESS_OR_EQUAL 100");
	}
	
	
	@Interval(start="intStrt", end="intEnd")
	public static class Entity1 {
		
		private Integer intStrt = -100;
		private Integer intEnd = 100;

		public Integer getIntStrt() { return intStrt; }

		public void setIntStrt(Integer intStrt) { this.intStrt = intStrt; }

		public Integer getIntEnd() { return intEnd; }

		public void setIntEnd(Integer intEnd) { this.intEnd = intEnd; }
		
	}
	
	@Interval(start="intStrt", end="intEndXXX")
	public static class EntityB1 {
		
		private Integer intStrt = -100;

		public Integer getIntStrt() { return intStrt; }

		public void setIntStrt(Integer intStrt) { this.intStrt = intStrt; }
		
	}
	
	@Interval(start="intStrt", end="intEndXXX")
	@Intervals(@Interval(start="intStrt", end="intEnd"))
	public static class EntityB2 {
		
		private Integer intStrt = -100;
		private Integer intEnd = 100;

		public Integer getIntStrt() { return intStrt; }

		public void setIntStrt(Integer intStrt) { this.intStrt = intStrt; }

		public Integer getIntEnd() { return intEnd; }

		public void setIntEnd(Integer intEnd) { this.intEnd = intEnd; }
		
	}
	
	@Intervals(@Interval(start="intStrt", end="intEnd"))
	public static class Entity3 {
		
		private Integer intStrt = -100;
		private Integer intEnd = 100;

		public Integer getIntStrt() { return intStrt; }

		public void setIntStrt(Integer intStrt) { this.intStrt = intStrt; }

		public Integer getIntEnd() { return intEnd; }

		public void setIntEnd(Integer intEnd) { this.intEnd = intEnd; }
		
	}
	
	@Intervals({@Interval(start="i1Strt", end="i1End"), @Interval(start="i2Strt", end="i2End")})
	public static class Entity4 {
		
		private Integer i1Strt = -100;
		private Integer i1End = 100;

		private Integer i2Strt = -200;
		private Integer i2End = 200;

		public Integer getI1Strt() { return i1Strt; }

		public void setI1Strt(Integer i1Strt) { this.i1Strt = i1Strt; }

		public Integer getI1End() { return i1End; }

		public void setI1End(Integer i1End) { this.i1End = i1End; }

		public Integer getI2Strt() { return i2Strt; }

		public void setI2Strt(Integer i2Strt) { this.i2Strt = i2Strt; }

		public Integer getI2End() { return i2End; }

		public void setI2End(Integer i2End) { this.i2End = i2End; }

	}
	
	
}

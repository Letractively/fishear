package net.fishear.utils;

import java.util.Comparator;

import net.fishear.utils.data.TestEntity;
import net.fishear.utils.data.TestEntity2;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class EntityUtilsTestComparator {

	
	@Test
	public void basicComparatorTest() {

		EntityUtils.getComparator(TestEntity.class, "val1");

	}

	@Test
	public void comparatorPrimitiveTest() {

		Comparator<TestEntity> cmp = EntityUtils.getComparator(TestEntity.class, "bool2");

		TestEntity te1 = new TestEntity();
		TestEntity te2 = new TestEntity();
		assertEquals(cmp.compare(te1, te2), 0);

		te2.setBool2(true);
		assertEquals(cmp.compare(te1, te2), -1);

		te2.setBool2(false);
		assertEquals(cmp.compare(te1, te2), -1);
		
		te1.setBool2(false);
		assertEquals(cmp.compare(te1, te2), 0);

		te1.setBool2(true);
		assertEquals(cmp.compare(te1, te2), 1);
		
	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void ComparatorExceptionTest() {

		EntityUtils.getComparator(TestEntity.class, "testEntity2");
		
	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void comparatorPrimitiveExceptionTest() {

		EntityUtils.getComparator(TestEntity.class, "bool1");
		
	}

	@Test
	public void dottedComparatorTest() {

		EntityUtils.getComparator(TestEntity.class, "testEntity2.code");
		
	}

	@Test
	public void valComparatorTest() {
		TestEntity te1 = new TestEntity();
		TestEntity te2 = new TestEntity();
		
		Comparator<TestEntity> cmp = EntityUtils.getComparator(TestEntity.class, "testEntity2.code");
		
		assertEquals(cmp.compare(te1, te2), 0);
		
		te1.setTestEntity2(new TestEntity2());
		te1.getTestEntity2().setCode("AAA");
		
		assertEquals(cmp.compare(te1, te2), 1);

		te2.setTestEntity2(new TestEntity2());
		te2.getTestEntity2().setCode("AAA");

		assertEquals(cmp.compare(te1, te2), 0);

		te2.getTestEntity2().setCode("BBB");

		assertEquals(cmp.compare(te1, te2), -1);

		te1.getTestEntity2().setCode("CCC");

		assertEquals(cmp.compare(te1, te2), 1);
	}
}

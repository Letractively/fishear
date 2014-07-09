package net.fishear.utils;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NumbersTest {

	@Test
	public void toBdTest() {
		assertEquals(Numbers.tobd((long)10), new BigDecimal(10));
		assertEquals(Numbers.tobd((double)5.123456987014), BigDecimal.valueOf((double)5.123456987014));
		assertEquals(Numbers.tobd((float)11.1234569), BigDecimal.valueOf((float)11.1234569));
		assertEquals(Numbers.tobd((short)9987), BigDecimal.valueOf(9987));
		assertEquals(Numbers.tobd((byte)122), BigDecimal.valueOf(122));
	}
	
}

package net.fishear.utils;

import static org.testng.Assert.*;
import net.fishear.exceptions.CoercerNotFoundException;

import org.testng.annotations.Test;

public class CoercionsTest
{

	@Test(expectedExceptions = CoercerNotFoundException.class)
	public void notfoundTets() {
		Coercions.convertType("123456", Class.class);
	}

	@Test
	public void toLongTets() {
		
		Long sl1 = new Long(123456);
		Long long1 = Coercions.convertType(sl1.toString(), Long.class);
		assertEquals(long1, sl1);

		Long sl2 = new Long(998877665544332211L);
		Long long2 = Coercions.convertType(sl2.toString(), Long.class);

		assertEquals(long2, sl2);
	}
	
	@Test
	public void toIntTets() {

		Integer si1 = new Integer(123456);
		Integer int1 = Coercions.convertType(si1.toString(), Integer.class);
		assertEquals(int1, si1);
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void bigIntTets() {

		Long sl1 = new Long(998877665544332211L);
		Coercions.convertType(sl1.toString(), Integer.class);
	}
	
	@Test(expectedExceptions = NumberFormatException.class)
	public void toIntExcptTets() {

		Coercions.convertType("12AAA3456", Integer.class);
		
	}

	@Test
	public void customCoercerTets() {

		Coercer<Byte, String> coercer = new AbstractCoercer<Byte, String>() {

			@Override
			protected String convertImpl(Byte source) {
				return Byte.toString(source);
			}
			
		};
		Coercions.addCoercer(coercer);
		Coercions.convertType(new Byte((byte) 10), String.class);
	}
}

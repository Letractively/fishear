package net.fishear.data.generic.query.restrictions;

import java.util.Date;

import net.fishear.data.generic.data.EntityWithInterval;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class RestrictionIntervalTest {

	@Test
	public void overlapTest() {
		Restrictions ret1 = Restrictions.overlap("validFrom", "validTo", -100, 100);
		assertEquals(ret1.toString(), "validFrom IS NULL OR validFrom GREATER_OR_EQUAL -100 AND validTo IS NULL OR validTo LESS_OR_EQUAL 100");

		Restrictions ret2 = Restrictions.overlap("validFrom", "validTo", -100, null);
		assertEquals(ret2.toString(), "validFrom IS NULL OR validFrom GREATER_OR_EQUAL -100");

		Restrictions ret3 = Restrictions.overlap("validFrom", "validTo", null, 100);
		assertEquals(ret3.toString(), "validTo IS NULL OR validTo LESS_OR_EQUAL 100");
	}
	
	@Test
	public void intervalTest() {
		Restrictions ret1 = Restrictions.interval("theDate", -100, 100);
		assertTrue(ret1.toString().startsWith("theDate BETWEEN [Ljava.lang.Object;"));

		Restrictions ret2 = Restrictions.interval("theDate", null, 100);
		assertEquals(ret2.toString(), "theDate LESS_OR_EQUAL 100");

		Restrictions ret3 = Restrictions.interval("theDate", -100, null);
		assertEquals(ret3.toString(), "theDate GREATER_OR_EQUAL -100");
	}
	
	
}

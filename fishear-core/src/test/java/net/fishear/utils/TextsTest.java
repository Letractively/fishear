package net.fishear.utils;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class TextsTest {

	String basicArray[] = {
			" ",
			"ABC",
			" def",
			null,
			"",
			"GHI ",
			" \n KLM  ",
			"\r\n"
	};

	@Test
	public void trimAllTest() {
		String[] as = Texts.trimAll(basicArray, "QQQQ");
		String expected[] = {
				"",
				"ABC",
				"def",
				"QQQQ",
				"",
				"GHI",
				"KLM",
				""
		};
		assertEquals(as, expected);
	}
	
	@Test
	public void allEmptyTest() {
		String[] as = Texts.setAllEmpty(basicArray, "1XYZ2");
		String expected[] = {
				"1XYZ2",
				"ABC",
				"def",
				"1XYZ2",
				"1XYZ2",
				"GHI",
				"KLM",
				"1XYZ2"
		};
		assertEquals(as, expected);
	}
	
	@Test
	public void removeNullsTest() {
		String[] as = Texts.removeNulls(Texts.setAllEmpty(basicArray, null));
		String expected[] = {
				"ABC",
				"def",
				"GHI",
				"KLM"
		};
		assertEquals(as, expected);
	}
	
	@Test
	public void removeEmptyTest() {
		String[] as = Texts.removeEmpty(basicArray);
		String expected[] = {
				"ABC",
				"def",
				"GHI",
				"KLM"
		};
		assertEquals(as, expected);
	}
	
	@Test
	public void substringtest() {
		assertEquals(Texts.substring("[ABCD]", "[", "]"), "ABCD");
		assertEquals(Texts.substring("XX[ABCD]YY", "[", "]"), "ABCD");
		assertEquals(Texts.substring("XX[ABCD]YY", "XX[", "]YY"), "ABCD");
		assertEquals(Texts.substring("XX[ABCD]YY", "AA[", "]YY"), "XX[ABCD");
		assertEquals(Texts.substring("  [ABCD]  ", "[", "]"), "ABCD");
		assertEquals(Texts.substring("[ABCD", "[", "]"), "ABCD");
		assertEquals(Texts.substring("ABCD]", "[", "]"), "ABCD");
		assertEquals(Texts.substring("[ABCD]", null, "]"), "[ABCD");
		assertEquals(Texts.substring("[ABCD]", "[", null), "ABCD]");
		assertEquals(Texts.substring(null, "[", "blabla"), "");
	}
	
}

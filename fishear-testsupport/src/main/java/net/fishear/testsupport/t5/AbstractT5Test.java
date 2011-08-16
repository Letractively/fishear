package net.fishear.testsupport.t5;

import net.fishear.utils.Classes;

import org.apache.tapestry5.test.PageTester;
import org.mockito.MockitoAnnotations;
import org.mockito.cglib.core.ClassesKey;

import com.formos.tapestry.testify.core.TapestryTester;
import com.formos.tapestry.testify.testng.TapestryTest;


public abstract class 
	AbstractT5Test
extends 
	TapestryTest
{

	protected PageTester tester() {
		return tester;
	}

	public AbstractT5Test(Class<?> appModuleClass, String appBasePath) {
		super(new TapestryTester(Classes.getParentPackage(Classes.getPackage(appModuleClass)), "app", appBasePath));
	}

	protected void setUpForAllTestMethods() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
}

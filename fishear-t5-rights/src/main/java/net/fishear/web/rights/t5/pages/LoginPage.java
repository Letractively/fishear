package net.fishear.web.rights.t5.pages;

import net.fishear.web.rights.t5.components.LoginForm;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;

public class LoginPage
{

	@Component
	@Property
	private LoginForm loginForm;

	void onActionFromLogout() {
		loginForm.performLogout();
	}
	
	public String getPageTitle() {
		return "Login page";
	}
	
	public String getPageName() {
		return "Login page";
	}
	
}

package net.fishear.web.rights.t5.components;

import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Exceptions;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;
import net.fishear.web.services.EnvironmentService;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.internal.Constants;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;




//@IncludeJavaScriptLibrary({
//	""
//})
@Import(
		stylesheet = "LoginForm.css"
)
public class 
	LoginForm 
extends 
	ComponentBase
{

	@Inject
	private LoginLogoutService llSvc;
	
	@Inject
	private Request request;
	
	@Inject
	private JavaScriptSupport rsup;
	
	@SuppressWarnings("unused")
	@Inject
	private EnvironmentService s2env;
	
	@Component
	AlertManager alerts;
	
	@Inject
	private Cookies cookies;

	@Property
	private String username;

	@Property
	private String password;

	@Property
	private boolean rememberMe;

	@SuppressWarnings("unused")
	private String clientId;
	
	public Object onSuccess() { 
		try {
			llSvc.doLogin(username, password, rememberMe);
		} catch (Exception xex) {
			processException(xex);
			return this;
		}
		return llSvc.getRedirectPageAfterLogin();
	}
	
	private void processException(Exception xex) {
		Throwable ex = Exceptions.getRootCause(xex);
		if(ex instanceof ValidationException) {
			if(username == null || username.trim().length() == 0) {
				alerts.error(translate("missing-loginname"));
			} else {
				alerts.info(translate(ex.getMessage()));
			}
			request.setAttribute(Constants.REQUIRED_LOGIN_RQATR, Constants.LoginRequest.LOGIN_FORCED);
		} else {
			ex.printStackTrace();
			alerts.info(translate("error-while-login", ex.toString()));
			request.setAttribute(Constants.REQUIRED_LOGIN_RQATR, Constants.LoginRequest.LOGIN_FORCED);
		}
		String lastPars = cookies.readCookieValue(Constants.LAST_PAGE_COOKIE_CODE);
		if(lastPars != null && lastPars.trim().length() > 0) {
			cookies.writeCookieValue(Constants.LAST_PAGE_COOKIE_CODE, lastPars, "/");
		}
	}

	public Object onLogout() {
		llSvc.doLogout();
		return llSvc.getRedirectPageAfterLogout();
	}

	public String getLoginMessage() {
		if(isLoggedIn()) {
			return translate("log-in-as-user-with-access-rights");
		} else {
			return translate("please-log-in");
		}
	}
	
	public boolean isLoggedIn() {
		return llSvc.isLoggedIn();
	}
	
	public boolean isAdmin() {
		return llSvc.hasRole("admin");
	}
	
	public boolean getHasMoreRoles() {
		return false;
	}
	
    @AfterRender
    public void afterRender(MarkupWriter wr) {
 		boolean requireLogin = request.getAttribute(Constants.REQUIRED_LOGIN_RQATR) != null;
        if (requireLogin) {
            rsup.addScript("$jq(function() {");
            rsup.addScript("$jq(\"#loginFormDialog\").dialog(\"option\", \"modal\", false);");
            rsup.addScript("$jq(\"#loginFormDialog\").dialog(\"open\");");
            rsup.addScript("});");
        }

//        rsup.addScript(String.format("localize('%s');", threadLocale.getLocale().toString()));
    }
    
    public UserInfoI getUserInfo() {
    	return llSvc.getUserInfo();
    }
    
    public void performLogout() {
    	llSvc.doLogout();
    }
}

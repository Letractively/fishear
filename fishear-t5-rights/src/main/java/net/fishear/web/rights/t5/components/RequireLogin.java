package net.fishear.web.rights.t5.components;

import net.fishear.web.rights.services.LoginLogoutService;
import net.fishear.web.services.EnvironmentService;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.internal.Constants;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;




/** The layout provides "coverage" for components requiring one of specific roles, 
 * which user must be logged in to allow access to such component.
 * Role code(s) is passed as parameter "roleCode". If no role code is passed, it is assumed
 * the user has access in case hi is logged in.
 * @author terber
 */
@Import(library = "RequireLogin.js")
public class 
	RequireLogin
extends
	ComponentBase
{

	@SuppressWarnings("unused")
	@Inject
	private EnvironmentService s2Env;
	
	@SuppressWarnings("unused")
	@Inject
	private Cookies cookies;
	
	@Inject
	private LoginLogoutService llSvc;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String roleCode;
	
	@Parameter(value = "false")
	private boolean showDialog;
	
	@Inject
	private Request request;

	/** if it is set to false, this component is "inactive" (= it does not check neither roles or account is logged in).
	 *  If true (= default), the component does it's work. This parameter is provided to allow creating 
	 *  uniform parametrized superclasses in case any subclasses do not require login.
	 */
	@Parameter(value = "literal:true")
	private boolean loginRequired;

	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	private Block noLogin;
	
	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	private Block noAccess;

	public Block getNoLoginBlock() {
		return noLogin == null ? crsc.findBlock("nologinDft") : noLogin;
	}
	
	public Block getNoAccessBlock() {
		return noAccess == null ? crsc.findBlock("noAccessDft") : noAccess;
	}
	
	public void setupRender() {
//		llSvc.checkRememberMe(cookies); 
	}
	
	public boolean isLoggedIn() {
		if(!loginRequired) {
			return true;
		}
		return llSvc.isLoggedIn();
	}

	@Cached
	public String[] getRolesList() {
		return roleCode == null ? null : roleCode.replace(';', ',').split(",");
	}

	public boolean getHasRole() {
		if(!loginRequired) {
			return true;
		}
		String roleCode = this.roleCode;
		if(roleCode != null && (roleCode = roleCode.trim()).length() > 0) {
			return llSvc.hasRole(getRolesList());
		}
		return true;		// if no role is passed to, it is assumed to acces is allowed i case user is logged in
	}

	public String getRoleCodes() {
		String s = roleCode;
		if(s == null || (s = s.trim().toLowerCase()).length() == 0) {
			return "(none)";
		}
		return roleCode.replace(';', ',');
	}
	
	@SetupRender
	public void checkLoginRequired() {
		if((!getHasRole() || !isLoggedIn()) && showDialog) {
			request.setAttribute(Constants.REQUIRED_LOGIN_RQATR, Constants.LoginRequest.LOGIN_REQUIRED);
		}
	}

	public boolean isMoreRoles() {
		String[] as = getRolesList();
		return as != null && as.length > 1;
	}

}

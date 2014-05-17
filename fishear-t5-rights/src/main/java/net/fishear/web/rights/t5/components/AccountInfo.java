package net.fishear.web.rights.t5.components;

import net.fishear.utils.Texts;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;
import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;



public class 
	AccountInfo
extends
	ComponentBase
{

	@SuppressWarnings("unused")
	@Parameter @Property
	private String onclickAction;
	
	@Inject @Property
	private LoginLogoutService llSvc;
	
	
	public String getUserInfo() {
		if(!llSvc.isLoggedIn()) {
			return translate("not-logged-label");
		}
		UserInfoI ui = llSvc.getUserInfo();
		if(ui == null) {
			return translate("no-user-info-label");
		}
		String udata = Texts.tos((Texts.tos(ui.getFullUserName())).trim());
		if(udata.length() == 0) {
			udata = Texts.tos(ui.getLoginName());
		}
		return udata;
	}
	
	public void onActionFromLogout() {
		llSvc.doLogout();
	}
}
 
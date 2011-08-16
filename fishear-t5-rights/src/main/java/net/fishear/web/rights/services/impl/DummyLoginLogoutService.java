package net.fishear.web.rights.services.impl;

import net.fishear.utils.Lists;
import net.fishear.utils.Texts;
import net.fishear.web.rights.entities.UserInfo;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;


/**
 * returns true for any combination username / password.
 * 
 * @author terber
 *
 */
public class
	DummyLoginLogoutService
extends
	AbstractLoginLogoutService
implements
	LoginLogoutService
{

	@Override
	public UserInfoI doLoginImpl(String username, String password) throws Exception {
		
		String unLo = username.toLowerCase();
		
		UserInfo uinf = new UserInfo();

		String[] ui = {
			Texts.toName(username),
			Texts.toName(username),
		};

		uinf.setFirstName(ui[0]);
		uinf.setLastName(ui[1]);
		uinf.setLoginName(username);

		// "virtual" user's roles
		if(unLo.equals("admin")) {
			uinf.setRoles(Lists.toList("admin", "user"));
		} else if(unLo.contains("user")) {
			uinf.setRoles(Lists.toList("user"));
		} else {
			uinf.setRoles(Lists.toList("public"));
		}

		return uinf;
	}

	@Override
	public Object getRedirectPageAfterLogin() {
		return null;
	}

	@Override
	public Object getRedirectPageAfterLogout() {
		return null;
	}
}

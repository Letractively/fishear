package net.fishear.web.rights.services.impl;

import java.util.List;

import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Defender;
import net.fishear.utils.Texts;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;
import net.fishear.web.services.EnvironmentService;
import net.fishear.web.t5.base.Constants;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;



/**
 * This service is designed to testing acces rights.
 * For user name 'admin', it sets user's reoles 'admin' and 'user'.
 * For each username containing 'user' everywhere, it sets role 'user'.
 * In rest of cases it sets role 'public'.
 * 
 * @author terber
 *
 */
public abstract class 
	AbstractLoginLogoutService
implements
	LoginLogoutService
{

	private static final String REMENBER_ME_SEPARATOR = "\u0000";

	@Inject
	protected Cookies cookies;

	@Inject
	protected RequestGlobals rgl;

	@Inject
	protected EnvironmentService gwEnv;

	public abstract UserInfoI doLoginImpl(String username, String password) throws Exception;

	public AbstractLoginLogoutService() {

	}

	@Override
	public void checkRememberMe() {
		// checks the session first. If it cannot be created, do nothing.
		try {
			if(getSession() == null) { return ; }
		} catch (Exception ex) { return; }
		cont1:
		if(!isLoggedIn() && rmmeRequired()) {
			String rmMe = cookies.readCookieValue(Constants.REMEMBER_ME_COOKIE_CODE);
			if(rmMe != null && (rmMe = rmMe.trim()).length() > 0 && gwEnv.hasClientId(cookies)) {
				String unp = gwEnv.decode(rmMe, gwEnv.getClientId(cookies));
				String[] as = unp.split(REMENBER_ME_SEPARATOR);
				if(as.length == 2) {
					try {
						doLogin(as[0], as[1], true);
						break cont1;
					} catch (ValidationException ex) {

					}
				}
				clearCookies();
				rgl.getRequest().getSession(true).setAttribute("gweb!rememberme!disabled", true);
			}
		}
	}
	
	public final void doLogin(String username, String password) {
		clearCookies();

		Session ses = getSession();
		ses.setAttribute(gesAtrLoggedInKey(), null);

		if(username == null || username.trim().length() == 0) {
			throw new ValidationException("username-is-mandatory");
		}

		try {
			UserInfoI uinf = doLoginImpl(username, password);
			ses.setAttribute(gesAtrLoggedInKey(), uinf);
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ValidationException("error-while-login-user", ex.toString());
		}
	}

	private boolean rmmeRequired() {
		
		return rgl.getRequest().getSession(true).getAttribute("gweb!rememberme!disabled") == null;
	}

	@Override
	public void doLogin(String username, String password, boolean rememberMe) {
		doLogin(username, password);
		if(rememberMe) {
			rgl.getRequest().getSession(true).setAttribute("gweb!rememberme!disabled", null);
			String uncode = username + REMENBER_ME_SEPARATOR + password;
			String uncEnc = gwEnv.encode(uncode, gwEnv.getClientId(cookies));
			cookies.writeCookieValue(Constants.REMEMBER_ME_COOKIE_CODE, uncEnc, getRememberMeUriBase());
		}
	}

	/**
	 * @return string 
	 */
	protected String getRememberMeUriBase() {
		return gwEnv.getUriBase();
	}

	protected String readRmm() {
		String rmMe = cookies.readCookieValue(Constants.REMEMBER_ME_COOKIE_CODE);
		if(rmMe != null) {
			String unp = gwEnv.decode(rmMe, gwEnv.getClientId(cookies));
			String[] as = unp.split(REMENBER_ME_SEPARATOR);
			if(as != null && as.length > 1) {
				return as[0] + ", " + as[1];
			}
		}
		return "(not cookie)";
	}

	public void clearCookies() {
		cookies.removeCookieValue(Constants.REMEMBER_ME_COOKIE_CODE);
		rgl.getRequest().getSession(true).setAttribute("gweb!rememberme!disabled", null);
	}

	protected Session getSession() {
		Request rq = Defender.notNull(rgl.getRequest(), "The request is null");
		Session ses = Defender.notNull(rq.getSession(true), "Session is null and system cannot create it");
		return ses;
	}

	@Override
	public UserInfoI getUserInfo() {
		if(isLoggedIn()) {
			Session ses = getSession();
			UserInfoI uinf = (UserInfoI) ses.getAttribute(gesAtrLoggedInKey());
			return uinf;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasRole(String... roleCodes) {
		UserInfoI uinf = getUserInfo();
		if(uinf != null) {
			List<String> roles = uinf.getRoles();
			for (String string : roles) {
				for (int i = 0; i < roleCodes.length; i++) {
					String rqr = Texts.tos(roleCodes[i]);
					if(rqr.length() > 0 && (string = Texts.tos(string)).length() > 0 && string.equalsIgnoreCase(rqr)) {
						return true;
					}
				}
			}
		}
	
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		return getSession().getAttribute(gesAtrLoggedInKey()) != null;
	}

	@Override
	public void doLogout() {
		clearCookies();
		Session ses = getSession();
		ses.setAttribute(gesAtrLoggedInKey(), null);
	}

	/** returns key under which is account info put to session.
	 * In future, it will be appended to application specific key to store 
	 * login info for more applications.
	 */
	protected String gesAtrLoggedInKey() {
		return "net!fishear!t5!account!key";
	}
}

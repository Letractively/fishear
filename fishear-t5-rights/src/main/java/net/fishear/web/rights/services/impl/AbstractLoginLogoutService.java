package net.fishear.web.rights.services.impl;

import java.util.List;

import javax.persistence.Transient;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.services.CurrentStateI;
import net.fishear.data.generic.services.CurrentStateSourceI;
import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Defender;
import net.fishear.utils.Texts;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;
import net.fishear.web.services.EnvironmentService;
import net.fishear.web.t5.internal.Constants;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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
	LoginLogoutService,
	CurrentStateSourceI
{

	
	private static String DATA_HASH_CONSTANT = Math.random() + "~~" + System.currentTimeMillis();
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String REMENBER_ME_SEPARATOR = "\u0000";

	@Inject
	protected Cookies cookies;

	@Inject
	protected RequestGlobals rgl;

	@Inject
	protected EnvironmentService gwEnv;

	/**
	 * starts login proccess. If succeeded, returns info about user.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception thrown in case any error occurred
	 */
	public abstract UserInfoI doLoginImpl(String username, String password) throws Exception;
	
	/**
	 * verifies whether user with given ID exists. Returns its info.
	 * 
	 * @param username
	 * @return {@link UserInfoI} ot null if user does not exist, is disabled ... ets
	 */
	public abstract UserInfoI checkUserData(String username);

	private transient CurrentState currentState;

	
	public AbstractLoginLogoutService() {
		if(DaoSourceManager.getDefaultDaoSource() != null && DaoSourceManager.getDefaultDaoSource().getCurrentStateSource() == null) {
			log.info("Default 'currentStateSource' has not been set. Setting this instance as 'currentStateSource'");
			DaoSourceManager.getDefaultDaoSource().setCurrentStateSource(this);
		}
	}

	public CurrentStateI getCurrentState() {
		if(this.currentState == null) {
			synchronized(this) {
				if(this.currentState == null) {
					currentState = new CurrentState(this);
					if(DaoSourceManager.getDefaultDaoSource() != null && DaoSourceManager.getDefaultDaoSource().getCurrentStateSource() == null) {
						DaoSourceManager.getDefaultDaoSource().setCurrentStateSource(this);
					}
				}
			}
		}
		return this.currentState;
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
	
	public final void doLogin(String userId, String password) {
		clearCookies();

		Session ses = getSession();
		ses.setAttribute(gesAtrLoggedInKey(), null);

		if(userId == null || userId.trim().length() == 0) {
			throw new ValidationException("username-is-mandatory");
		}
		
		try {
			UserInfoI uinf;
Cont1:
			{ 
				if(rmmeRequired() && hashUserData(userId).equals(password)) {
					log.trace("Remeneber me constant for userId {} fits. Getting userInfo.", userId);
					uinf = checkUserData(userId);
					if(uinf != null) {
						break Cont1;
					}
					log.debug("User info is null for iserId {}. Continuing normal login.", userId);
				}
				uinf = doLoginImpl(userId, password);
			}

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
			String uncode = username + REMENBER_ME_SEPARATOR + hashUserData(username);
			String uncEnc = gwEnv.encode(uncode, gwEnv.getClientId(cookies));
			cookies.writeCookieValue(Constants.REMEMBER_ME_COOKIE_CODE, uncEnc, getRememberMeUriBase());
		}
	}

	private String hashUserData(String username) {
		return Integer.toString(("*" + username + "~" + getHashConstant(username) + "*").hashCode(), 36);
	}

	protected String getHashConstant(String userId) {
		return DATA_HASH_CONSTANT;
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
		Session ses = getSession();
		if(ses == null) {
			return null;
		}
		if(isLoggedIn()) {
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

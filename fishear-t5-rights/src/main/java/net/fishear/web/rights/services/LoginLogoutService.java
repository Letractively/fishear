package net.fishear.web.rights.services;

import net.fishear.web.rights.entities.UserInfoI;

/** Basic login / logout operations
 * 
 * @author terber
 *
 */
public interface LoginLogoutService
{

	void doLogin(String username, String password);

	/** logout account, if any is logged in.
	 * Do nothing if user is not logged.
	 */
	void doLogout();

	boolean isLoggedIn();

	/** checks if the "remember me" cookie is set. If so and user is not logged in, logs in it.
	 * To apply 'remember me', the client's ID cookie must be set as well.
	 */
	void checkRememberMe();

	/** calls doLogin to user log in.
	 * If the login successes and the 'rememberMe' param is set, set rememberMe cookie.
	 */
	void doLogin(String username, String password, boolean rememberMe);
	
	/** returns true if user is logged-in and has associated (and eventually active) any of roles with given code.
	 * Role codes are case insensitive, the method should always trim them.
	 */
	boolean hasRole(String... roleCodes);

	/** returns page which the user is redirect to after successfull login.
	 * Can return null = default page is used.
	 */
	Object getRedirectPageAfterLogin();

	/** returns page which the user is redirect to after logout.
	 * Can return null = default page is used.
	 */
	Object getRedirectPageAfterLogout();
	
	/** returns informations about user currently logged in, or null if no user is logged in
	 */
	UserInfoI getUserInfo();
}

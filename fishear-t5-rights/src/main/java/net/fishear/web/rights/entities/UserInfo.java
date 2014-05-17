package net.fishear.web.rights.entities;

import java.util.ArrayList;
import java.util.List;

import net.fishear.utils.Texts;

/** commonly used entity - across entire system.
 * @author terber
 *
 */
public class UserInfo implements UserInfoI
{

	private String loginName;
	
	private String firstName;
	
	private String lastName;

	private String titles;

	private List<String> roles;

	public UserInfo() {
		setRoles(null);
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setRoles(List<String> list) {
		this.roles = list == null ? new ArrayList<String>() : list;
	}

	/** Returns list of roles associated to user.
	 *  never return null - only empty array.
	 */
	public List<String> getRoles() {
		return roles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getTitles() {
		return titles;
	}

	@Override
	public String getFullUserName() {

		return Texts.tos(getFirstName()) + " " + getLastName();
	}
	
}

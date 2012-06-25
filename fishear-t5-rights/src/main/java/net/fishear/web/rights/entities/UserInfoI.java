package net.fishear.web.rights.entities;

import java.util.List;

public interface UserInfoI
{

	List<String> getRoles();

	String getLoginName();

	String getFullUserName();
	
	String getFirstName();
	
	String getLastName();

}

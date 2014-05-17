package net.fishear.web.t5.internal;

public class Constants
{

	public static final String JQUERY_FILE = "jquery.min.js";

	public static final String SCRIPTS_BASE = "context:asset";

	public static final String CSS_BASE = "context:asset/css";

	public static final String JQUERY_BASE = SCRIPTS_BASE+"/"+"jquery";

	public static final String JQUUI_BASE = JQUERY_BASE+"/jquery-ui";

	public static enum LoginRequest {
		LOGIN_REQUIRED, 
		LOGIN_FORCED
	}

	public static final String REMEMBER_ME_COOKIE_CODE = "!net!fishear!web!rmme!cookie!";

	public static final String REQUIRED_LOGIN_RQATR = "!net!fishear!web!rqlogin!rqattr!";

	public static final String CLNID_COOKIE_CODE = "!net!fishear!web!clnid!rqattr!";

	public static final String LAST_PAGE_COOKIE_CODE = "!net!fishear!web!last!page!";

}

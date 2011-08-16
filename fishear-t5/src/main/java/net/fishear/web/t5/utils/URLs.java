package net.fishear.web.t5.utils;

//import net.fishear.webapps.common.services.SettingBean;
import org.apache.tapestry5.services.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLs
{
	public static final UrlType NORMAL = UrlType.NORMAL;
	public static final UrlType SECURE = UrlType.SECURE;
	
	enum UrlType {
		NORMAL,
		SECURE
	}
	
	/** returns protocol, server and port of this application.
	 * Application prefix is NOT added in the end of URL.
	 * @param sbean setting bean
	 * @param type type of returned URL
	 * @return
	 */
//	public static String getUrlBase(SettingBean sbean, UrlType type) {
//		String base = sbean.getStringValue("be2.web.base.url");
//		String port;
//		switch(type) {
//		case NORMAL:
//			port = sbean.getStringValue("be2.web.unsecure.port", "").trim();
//			if(port.equals("80")) {
//				return "http://"+base;
//			} else {
//				return "http://"+base + ":" + port;
//			}
//		case SECURE:
//			port = sbean.getStringValue("be2.web.secure.port", "").trim();
//			if(port.equals("443")) {
//				return "https://"+base;
//			} else {
//				return "https://"+base + ":" + port;
//			}
//		}
//		throw new IllegalArgumentException("Unknown URL type: " + type);
//	}
	
	/** returns basic URL of application.
	 * This consists of protocol, server, port and base path of this application.
	 * Application prefix is NOT added in the end of URL.
	 * @param sbean setting bean
	 * @param type type of returned URL
	 */
//	public static String getApplicationUrl(SettingBean sbean, UrlType type) {
//		String s = getUrlBase(sbean, type);
//		String path = sbean.getStringValue("be2.web.applName", "").trim();
//		if(path.length() == 0) {
//			return s;
//		}
//		return s + "/" + path;
//	}

    public static String getBackUrl(Request request) {
        String link = (String) request.getAttribute("pageUrl");

        if (request.getParameterNames().size() > 0 || link.contains("regconfirm") || link.contains("resetpwd")) {
            link="default";
        }

        try {
            return URLEncoder.encode(link,"iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            //should never happen
            throw new RuntimeException(e);
        }
    }

	/** Removes protocol, server and port from URL (if any).
	 * Returns URL relative to server's root (e.g. URL is absolute on given server). 
	 * If URL does not contain those informations, leaves it unchanged.
	 * In case the 'url' is server only (no path informations) returns "/" (= root )
	 * If 'url' is null, returns empty string (never returns null).
	 */
	public static String stripServer(String url) {
		if(url == null || (url = url.trim()).length() == 0) {
			return "";
		}
		String cstr;
		if(
				url.regionMatches(true, 0, cstr = "http://", 0, 7) ||
				url.regionMatches(true, 0, cstr = "https://", 0, 8)
		) {
			// cstr was set in dependency to evaluated condition
			int ii = url.indexOf('/', cstr.length());
			if(ii < 0) {
				return "/";
			}
			return url.substring(ii);
		}
		return url;
	}
}

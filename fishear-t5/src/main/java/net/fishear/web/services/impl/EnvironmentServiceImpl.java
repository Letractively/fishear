package net.fishear.web.services.impl;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import net.fishear.utils.Globals;
import net.fishear.web.services.EnvironmentService;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;



public class 
	EnvironmentServiceImpl
implements 
	EnvironmentService
{

	private static final String DFT_PROP_FILE = "WEB-INF/fishear.properties";

	private static final String PROP_FILE_KEY = "fishear-property-file";
	
	private static Logger LOG = Globals.getLogger();
	
	private String uriBase = "";		// must not be null

	private Properties thisProps;

	private ApplicationGlobals aplGl;
	
	public EnvironmentServiceImpl(ApplicationGlobals aplGl) {
		if(aplGl == null) {
			throw new IllegalArgumentException("The 'aplGl' parameter must not be null.");
		}
		this.aplGl = aplGl;
	}

	@Override
	public String generateUniqueClientId() {
		String rand1 = StringUtils.leftPad(Integer.toString((int)(Math.random() * 1000)), 5, "-");
		String rand2 = StringUtils.leftPad(Integer.toString((int)(Math.random() * 1000)), 5, "A");
		return rand1 + Long.toString(System.currentTimeMillis() % 1000000000, 32) + rand2;
	}
	
	private String prepareKey(String key, int len) {
		String skk = "";
		while(skk.length() < len) { skk = skk.concat(key); }
		return skk.substring(0, len);
	}

	@Override
	public String decode(String encodedString, String key) {
		// TODO: implement better algorythm
		try {
			String rets = new String(Base64.decodeBase64(encodedString.replace('*', '=').getBytes("utf-8")), "utf-8");
			String skk = prepareKey(key, rets.length());
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < rets.length(); i++) {
				sb.append((char)(rets.charAt(i) ^ skk.charAt(i)));
			}
			return sb.toString();
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String encode(String plainString, String key) {
		// TODO: implement better algorythm
		String skk = prepareKey(key, plainString.length());
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < plainString.length(); i++) {
			sb.append((char)(plainString.charAt(i) ^ skk.charAt(i)));
		}
		try {
			String rets = new String(Base64.encodeBase64(sb.toString().getBytes("utf-8")), "utf-8").replace('=', '*');
			return rets;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getClientId(Cookies cookies) {
		String cid = cookies.readCookieValue(net.fishear.web.t5.internal.Constants.CLNID_COOKIE_CODE);
		if(StringUtils.isEmpty(cid)) {
			cid = generateUniqueClientId();
			cookies.writeCookieValue(net.fishear.web.t5.internal.Constants.CLNID_COOKIE_CODE, cid, getUriBase());
		}
		return cid;
	}

	@Override
	public boolean hasClientId(Cookies cookies) {
		String cid = cookies.readCookieValue(net.fishear.web.t5.internal.Constants.CLNID_COOKIE_CODE);
		return !StringUtils.isEmpty(cid);
	}

	@Override
	public String getUriBase() {
		return uriBase;
	}
	
	private Properties gprops() {
		if(thisProps == null) synchronized(this) {
			if(thisProps == null) {
				String propFileName = aplGl.getServletContext().getInitParameter(PROP_FILE_KEY);
				if(propFileName == null || (propFileName = propFileName.trim()).length() == 0) {
					LOG.warn("Propery file is NOT specifies as contex parameter '{}', Assuming default file '{}'", PROP_FILE_KEY, DFT_PROP_FILE);
					propFileName = DFT_PROP_FILE;
				}
				Properties pp = new Properties();
				try {
					FileReader rdr = new FileReader(aplGl.getServletContext().getRealPath(propFileName));
					pp.load(rdr);
					rdr.close();
				} catch (IOException ex) {
					LOG.error("Cannot load property file 'context:{}'. The cause: {}", propFileName, ex);
					this.thisProps = new Properties();
				}
				this.thisProps = pp;
			}
		}
		return this.thisProps;
	}

	@Override
	public String getProperty(String key, String dft) {

		return gprops().getProperty(key, dft);
	}

	@Override
	public String getPropertyRemeber(String key, String dft) {
		String val = gprops().getProperty(key);
		if(val == null) {
			LOG.warn("The property with key '{}' does not exist in system properties. Assume value '{}' and set it for futrher use.", key, dft);
			gprops().setProperty(key, dft == null ? "" : dft);
			val = dft;
		}
		return val;
	}
}

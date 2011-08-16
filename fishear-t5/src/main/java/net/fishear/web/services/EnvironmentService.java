package net.fishear.web.services;

import org.apache.tapestry5.services.Cookies;

/** the service provides basic environment parameters for the application.
 * Some values can be generated to be unique for given client session.
 * 
 * @author terber
 *
 */
public interface EnvironmentService
{

	/** generates and returns string, which contains unique string.
	 * This string may be used fortr example as ID of client browser etc...
	 */
	public String generateUniqueClientId();

	/** encodes any string using given key. 
	 * To decode string encoded by this method, call {@link #decode(String, String)}
	 */
	public String encode(String plainString, String key);
	
	/** decodes string previously encoded by {@link #encode(String, String)} using given key.
	 * The key must be the same as key passed to encode method.
	 * @return
	 */
	public String decode(String encodedString, String key);

	/** returns unique client ID depending on cookie value.
	 * If such does not exist, creates it and set as cookie.
	 */
	public String getClientId(Cookies cookies);

	/** returns true if client has generated it's ID.
	 * @see #getClientId(Cookies)
	 */
	public boolean hasClientId(Cookies cookies);

	/** returns URI base of this web. 
	 * It is uri relative to server root - the constant part after server (and eventually port). 
	 * The returned value always starts and ends with "/" (the slash) character. 
	 * In case application is in root of the web, returns "/".
	 */
	public String getUriBase();

	/** returns property with key "key" from application's properties, or "dft" if such property does not exist.
	 * @param key
	 * @param dft
	 */
	public String getProperty(String key, String dft);

	/** returns property with key "key" from application's properties.
	 * If such property does not exist, log it as warning, store the "dft" value and return it (Since this, the "dft" value will be returned always).
	 * @param key
	 * @param dft
	 */
	public String getPropertyRemeber(String key, String dft);
}

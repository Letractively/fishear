package net.fishear.utils;

import java.io.*;
import java.util.Properties;

public class PropertyUtils {

	private static final String SPECIAL_LINE_BEGIN = "#!";
	
	
	/** loads property file using charset specified in property file itself.
	 * If first line of property file begins with those two characters: '#!' (e.g. sharp and screamer), 
	 * those line is interpreted as "control line". It contains key-value pairs separated by colon (',') or semicolon (';') character.
	 * If those line contains keyword 'encoding=XXXX', the 'XXXX' is taken as encoding of rest of this file.
	 * Blank rows before are ignored.
	 * For example: if the file starts with line "<code>#! encoding=utf8, anyOtherKey=otherValue</code>", those file is supposed to be encoded in UTF-8 encoding.
 	 * @param file file to be read
 	 * @param p properties read from special line (if any) are stored into this properties
	 * @return read properties
	 * @throws IOException in case any error ecorred
	 */
	public static Properties loadProperties(File file, Properties p) throws IOException {
		InputStream is = new FileInputStream(file);
		try {
			return loadProperties(is, p);
		} finally {
			is.close();
		}
	}

	/** loads property from stream using charset specified in property stream itself.
	 * Stream is read from current position.
 	 * @param is stream to read from. It is not closed after read.
 	 * @param hdrProps properties read from special line (if any) are stored into this properties
	 * @return read properties
	 * @throws IOException in case any error ecorred
	 * @see #loadProperties(File, Properties)
	 */
	public static Properties loadProperties(InputStream is, Properties hdrProps) throws IOException {
		BufferedInputStream bis = new  BufferedInputStream(is);
		Properties props = new Properties();
		if(hdrProps == null) {
			hdrProps = new Properties();
		}
		bis.mark(4000);
		BufferedReader brd = new BufferedReader(new InputStreamReader(bis, "iso-8859-1"));	// one character == one byte
		String s;
		int readed = 0;
		String encoding = "";
		while((s = brd.readLine()) != null) {
			System.out.println("  " + s);
			readed += s.length() + 3;
			if(readed > 4000) {  // in case number of readed characters exceed buffer size
				break;
			}
			if((s = s.trim()).length() > 0) {
				if(s.startsWith(SPECIAL_LINE_BEGIN)) {
					hdrProps.load(new StringReader(s.substring(SPECIAL_LINE_BEGIN.length()).trim().replace(',', '\n').replace(';', '\n')));
					encoding = hdrProps.getProperty("encoding", "").trim();
					if(encoding.length() == 0) {
						encoding = hdrProps.getProperty("charset", "").trim();
					}
					break;
				} else if(!s.startsWith("#")) { // comments on file begin are skipped
					break;
				}
			}
		}
		bis.reset();
		BufferedReader brd2;
		if(encoding.length() > 0) {
			brd2 = new BufferedReader(new InputStreamReader(bis, encoding));
		} else {
			brd2 = new BufferedReader(new InputStreamReader(bis));
		}
		props.load(brd2);
		return props;
	}
}

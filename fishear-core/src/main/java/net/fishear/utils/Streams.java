package net.fishear.utils;


import java.io.*;

public class Streams
{

	public static String load(File path, String encoding) throws UnsupportedEncodingException, IOException {
		return load(new FileInputStream(path), encoding, true);
	}

	public static String load(InputStream inputStream, String encoding, boolean closeStream) throws UnsupportedEncodingException, IOException {

		Reader rdr = new InputStreamReader(inputStream, encoding);

		StringBuilder sb = new StringBuilder();
		char[] ac = new char[4096];
		int ii;

		while((ii = rdr.read(ac)) > 0) {
			sb.append(ac, 0, ii);
		}

		if(closeStream) {
			inputStream.close();
		}
		return sb.toString();
	}

}

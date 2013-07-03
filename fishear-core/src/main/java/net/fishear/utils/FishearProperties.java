package net.fishear.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class 
	FishearProperties
extends
	Properties
{
	
	private static final long serialVersionUID = 1L;

	private static final String PROPERTIES_FILE = "fishear.properties";

	private static FishearProperties instance;

	public static FishearProperties getInstance() {
		if(instance == null) {
			synchronized(FishearProperties.class) {
				if(instance == null) {
					instance = new FishearProperties();
					try {
						instance.loadFromClasspath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}

	public void loadFromClasspath() throws IOException {
		URL url = getClass().getClassLoader().getResource(PROPERTIES_FILE);
		super.load(url.openStream());
	}
	
}

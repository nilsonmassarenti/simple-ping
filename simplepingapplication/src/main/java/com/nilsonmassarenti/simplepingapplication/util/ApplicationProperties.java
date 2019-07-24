package com.nilsonmassarenti.simplepingapplication.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	
	public static Properties getProperties(){
		Properties properties = new Properties();
		try {
			ClassLoader classLoader = new ApplicationProperties().getClass().getClassLoader();
			File file = new File(classLoader.getResource("app.properties").getFile());
			InputStream in = new FileInputStream(file);
			properties.load(in);
			return properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

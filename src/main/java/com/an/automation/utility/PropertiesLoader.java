package com.an.automation.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.an.automation.report.Log;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PropertiesLoader {

	private static PropertiesLoader _instance;
	public Properties framework_configuration;
	public Properties selenium_configuration;
	public Properties environment_properties;

	public PropertiesLoader() {
		framework_configuration = readResourceProperties("/framework.properties");
		String selenium_location = framework_configuration.getProperty("selenium.location");

		// check test location and get properties
		if (selenium_location.equalsIgnoreCase("local")) {
			String configPath = framework_configuration.getProperty("selenium.local.config.path");
			selenium_configuration = readResourceProperties(configPath);
		} else if (selenium_location.equalsIgnoreCase("browserstack")) {
			String configPath = framework_configuration.getProperty("selenium.browserstack.config.path");
			selenium_configuration = readResourceProperties(configPath);
		}
	}


	public static PropertiesLoader getPropertiesLoader() {
		if (_instance == null) {
			_instance = new PropertiesLoader();
			return _instance;
		} else {
			return _instance;
		}
	}

	/**
	 * This method is used to read the configuration file
	 * 
	 * @param fileName
	 *            The path of property file located in project
	 * @return Properties set
	 * @throws Exception
	 */
	public static Properties readProperties(String path) throws Exception {
		Properties prop = new Properties();
		try {
			InputStream input = null;
			input = new FileInputStream(path);
			prop.load(input);
			for (String key : prop.stringPropertyNames()) {
				String configValue = System.getProperty(key);
				if (configValue != null) {
					prop.setProperty(key, configValue);
				}
			}
		} catch (Exception e) {
			System.out.print("Cannot read property file: [" + path + "]");
			throw e;
		}
		return prop;
	}

	/**
	 * This method is used to read the configuration file
	 * 
	 * @param filePath
	 *            The path of property file located in resource folder
	 * @return Properties set
	 * @throws IOException
	 * @throws Exception
	 */
	public static Properties readResourceProperties(String filePath) {
		Properties prop = new Properties();
		try (InputStream inputStream = PropertiesLoader.class.getResourceAsStream(filePath)) {
			prop.load(inputStream);
			for (String key : prop.stringPropertyNames()) {
				String configValue = System.getProperty(key);
				if (configValue != null) {
					prop.setProperty(key, configValue);
				}
			}
		} catch (Exception e) {
			Log.error("Cannot read property ");
			e.printStackTrace();
			return null;
		}
		return prop;
	}
	
	/**
	 * This method is used to read the configuration file
	 * 
	 * @param filePath
	 *            The path of property file located in resource folder
	 * @return Properties set
	 * @throws IOException
	 * @throws Exception
	 */
	public static Properties readCSVResourceProperties(String filePath) {
		Properties prop = new Properties();
		try (InputStream inputStream = PropertiesLoader.class.getResourceAsStream(filePath)) {
			prop.load(inputStream);
			for (String key : prop.stringPropertyNames()) {
				String configValue = System.getProperty(key);
				if (configValue != null) {
					prop.setProperty(key, configValue);
				}
			}
		} catch (Exception e) {
			Log.error("Cannot read property ");
			e.printStackTrace();
			return null;
		}
		return prop;
	}
	
	public static Map<Integer, Map<String, String>> getCSVData(char seperator, String filePath) throws Exception {
        Map<Integer, Map<String, String>> CSVData = new TreeMap<Integer, Map<String, String>>();
        Map<String, String> csvRowData = null;
        Reader reader = new FileReader(FilePaths.getResourcePath(filePath));
        try {
            Iterator<Map<String, String>> iterator = new CsvMapper().readerFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader()
                            .withColumnSeparator(seperator).withoutQuoteChar()).readValues(reader);
            int index = 1;
            while(iterator.hasNext()){
                csvRowData = iterator.next();
                CSVData.put(index, csvRowData);
                index++;
            }
        } catch (IOException e) {
        	Log.error("Cannot read CSV data" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return CSVData;
    }

}

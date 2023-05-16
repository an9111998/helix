package com.an.automation.utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.an.automation.report.Log;


public class FilePaths {
	
	private static String REPORT_ROOT_FOLDER;
	private static String REPORT_FOLDER;
	private static String SCREENSHOT_FOLDER;
	private static String REPORT_FILE_PATH;
	
	
	public static String getRootProject() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getResourcePath(String filename) throws Exception {
		try {
		URL rsc = FilePaths.class.getResource(filename);
		return Paths.get(rsc.toURI()).toFile().getAbsolutePath();
		}catch(Exception e) {
			Log.info("Cannot get resource file path");
			throw e;
		}
	}
	
	public static void initReportDirectory() throws IOException {
		String awsReportRoot = System.getenv("DEVICEFARM_LOG_DIR");
		if(awsReportRoot != null) {
			REPORT_FOLDER = awsReportRoot + File.separator + "Reports";
			SCREENSHOT_FOLDER = REPORT_FOLDER + File.separator + "Screenshots";
			REPORT_FILE_PATH = REPORT_FOLDER + File.separator + "Report_" + System.getenv("DEVICEFARM_DEVICE_NAME") + ".html";
		}else {
			REPORT_ROOT_FOLDER = getRootProject() + File.separator + "Reports";
			REPORT_FOLDER = REPORT_ROOT_FOLDER + File.separator + "Latest Report";
			checkExistReportAndReName(REPORT_FOLDER);
			SCREENSHOT_FOLDER = REPORT_FOLDER + File.separator + "Screenshots";
			REPORT_FILE_PATH = REPORT_FOLDER + File.separator + "Report.html";
		}
		createDirectory(REPORT_ROOT_FOLDER);
		createDirectory(REPORT_FOLDER);
		createDirectory(SCREENSHOT_FOLDER);
	}
	
	private static void checkExistReportAndReName(String path) throws IOException {
		if(pathExist(path)) {
			File oldReport = new File(path);
			BasicFileAttributes oldReportAttribute = Files.readAttributes(oldReport.toPath(), BasicFileAttributes.class);
			File renameOldReport = new File(getRootProject() + File.separator + "Reports" + File.separator + "Report_" + oldReportAttribute.creationTime().toString().replace(":", "."));
			oldReport.renameTo(renameOldReport);
		}
	}
	
	/**
	 * @return
	 */
	public static String getReportFolder() {
		return REPORT_FOLDER;
	}
	
	/**
	 * @return
	 */
	public static String getScreenshotFolder() {
		return SCREENSHOT_FOLDER;		
	}
	
	/**
	 * @return
	 */
	public static String getReportFilePath() {
		return REPORT_FILE_PATH;		
	}
	
	/**
	 * @param timeStamp
	 * @return
	 */
	public static String getCurrentDateTimeString(String timeStamp) {
		
		DateFormat dateFormat = new SimpleDateFormat(timeStamp);
		Date date = new Date();
		
		return dateFormat.format(date);
	}
	
	
	/**
	 * Correct the file path based on the OS system type
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static String correctPath(String path) {

		return path.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"));

	}

	/**
	 * Verify file or path is existing on system
	 * 
	 * @param path
	 *            the path to file
	 */
	public static boolean pathExist(String path) {
		
			return Files.exists(new File(path).toPath());

	}

	/**
	 * Delete a file or a path
	 * 
	 * @param path
	 *            the path to file
	 * @throws IOException 
	 */
	public static void deletePath(String path) throws IOException  {
		
			Files.delete(new File(path).toPath());

	}

	/**
	 * create a path
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static void createFile(String path) throws Exception {
		
			if (!pathExist(path))
				Files.createFile(new File(path).toPath());
	}

	/**
	 * create a path
	 * 
	 * @param path
	 *            the path to file
	 * @throws IOException 
	 * @throws Exception
	 */
	public static void createDirectory(String path) throws IOException  {
		
			if (!pathExist(path))
				Files.createDirectory(new File(path).toPath());
	}

	/**
	 * To copy a file from the source file to the destination file
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException 
	 */
	public static void copyFile(String src, String dest) throws IOException  {
		
			File sourceFile = new File(src);
			File destFile = new File(dest);
			Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

	}

	/**
	 * To copy a folder from the source folder to the destination folder
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException 
	 */
	public static void copyDirectory(String src, String dest) throws IOException {
		
			File sourceFile = new File(src);
			File destFile = new File(dest);
			createDirectory(dest);
			FileUtils.copyDirectory(sourceFile, destFile, true);

	}
}

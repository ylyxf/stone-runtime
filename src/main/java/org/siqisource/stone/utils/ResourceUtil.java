package org.siqisource.stone.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceUtil {

	private static String homePath = null;

	private static Boolean runInJar = true;

	public static String getHomePath() {
		return homePath;
	}

	public static Boolean getRunInJar() {
		return runInJar;
	}

	public static void init() {
		if (homePath != null) {
			return;
		}
		ApplicationHome home = new ApplicationHome(ResourceUtil.class);
		File homeFolder = home.getSource();
		String path = homeFolder.getAbsolutePath();
		runInJar = !path.endsWith("classes");

		if (runInJar) {
			homePath = path.substring(0, path.lastIndexOf(System.getProperties().getProperty("file.separator")));
		} else {
			homePath = path.substring(0, path.length() - 8);
		}
	}

	/**
	 * 读取资源
	 *
	 * @param folder
	 *            folder
	 * @return listResources
	 */
	public static List<String> listResources(String folder) {
		init();
		try {
			if (!runInJar) {
				ClassPathResource classPathResource = new ClassPathResource(folder);
				File classFolder = classPathResource.getFile();
				return Arrays.asList(classFolder.list());
			} else {
				List<String> resourceList = new ArrayList<String>();
				ClassPathResource classPathResource = new ClassPathResource(folder);
				String directoryToScan = classPathResource.getURL().toExternalForm();
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				Resource[] resources = resolver.getResources(directoryToScan + "/**");
				for (Resource resource : resources) {
					if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
						URL url = resource.getURL();
						String urlString = url.toExternalForm();
						String targetName = "classpath:/" + folder + urlString.substring(directoryToScan.length());
						resourceList.add(targetName);
					}
				}
				return resourceList;
			}
		} catch (Exception e) {
			throw new RuntimeException("读取资源列表的时候出错了", e);
		}
	}

	/**
	 * 释放资源
	 *
	 * @param folder
	 *            folder
	 *
	 */
	public static void extraResources(String folder) {
		init();
		try {
			File destDir = new File(homePath);
			if (!runInJar) {
				ClassPathResource classPathResource = new ClassPathResource(folder);
				FileUtils.copyDirectory(classPathResource.getFile(), new File(homePath));
			} else {
				ClassPathResource classPathResource = new ClassPathResource(folder);
				String directoryToScan = classPathResource.getURL().toExternalForm();
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				Resource[] resources = resolver.getResources(directoryToScan + "/**");
				for (Resource resource : resources) {
					if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
						URL url = resource.getURL();
						String urlString = url.toExternalForm();
						String targetName = urlString.substring(directoryToScan.length());
						File destination = new File(destDir, targetName);
						FileUtils.copyURLToFile(url, destination);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("释放资源的时候出错了", e);
		}
	}

}
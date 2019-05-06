package com.matrix.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by tan on 2016/8/30.
 */
public class FileUtil {
	public static String getRecentFileName(String fileBasePath) {
		File folder = new File(fileBasePath);
		// 获得folder文件夹下面所有文件
		String[] fileNames = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
		});
		String recentFileName = null;
		long recentCreateTime = 0;
		if (fileNames != null) {
			for (String fileName : fileNames) {
				long createTime = Long.parseLong(fileName);
				if (recentCreateTime < createTime) {
					recentFileName = fileName;
					recentCreateTime = createTime;
				}
			}
		}
		if (recentFileName != null) {
			return fileBasePath + File.separatorChar + recentFileName;
		} else {
			return null;
		}

	}
}

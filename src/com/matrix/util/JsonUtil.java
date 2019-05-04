package com.matrix.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	private static ObjectMapper objectMapper;
	private static JsonFactory factory;

	static {
		factory = new JsonFactory();
		objectMapper = new ObjectMapper();
		// 处理map
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		// 禁止未知属性打断反序
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		factory.setCodec(objectMapper);
	}

	public static void saveObjToJsonFile(String fileBasePath, String fileName, Object data) throws IOException {
		File file = new File(fileBasePath + fileName + ".json");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		JsonGenerator jsonGenerator = factory.createGenerator(file, JsonEncoding.UTF8);
		jsonGenerator.useDefaultPrettyPrinter();
		jsonGenerator.writeObject(data);
	}

	public static Object objRecoverFromRecentJsonFile(String fileBasePath, Class clazz) throws IOException {
		File recentFile = getRecentJsonFile(fileBasePath);
		if (recentFile == null)
			return null;
		Object object = objectMapper.readValue(recentFile, clazz);
		return object;
	}

	public static File getRecentJsonFile(String fileBasePath) {
		File folder = new File(fileBasePath);
		// 获得folder文件夹下面所有文件
		File[] files = folder.listFiles();
		File recentFile = null;
		long recentCreateTime = 0;
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					// 获取后缀名
					String fileName = file.getName();
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀
					String prefix = fileName.substring(0, fileName.lastIndexOf("."));// 文件名
					if (suffix.equals("json")) {
						long createTime = Long.parseLong(prefix);
						if (recentCreateTime < createTime) {
							recentFile = file;
							recentCreateTime = createTime;
						}
					}
				}
			}
		}

		return recentFile;
	}
}

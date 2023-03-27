package cn.pandaframework.data;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;

public class DataProcess {
	private static Logger logger = LoggerFactory.getLogger(DataProcess.class);
	
	private static JSONArray data;
	
	private static boolean isLoad = false;
	
	public static void loadData(String filePath) throws Exception {
		File file = new File(filePath);
		FileUtils.createParentDirectories(file);
		if(!file.exists()) {
			file.createNewFile();
		}
		String dataStr = FileUtils.readFileToString(file, "utf-8");
		data = JSON.parseArray(dataStr);
		isLoad = true;
	}
	
	public static JSONArray getData() {
		if(!isLoad) {
			throw new RuntimeException("请先调用 loadData() 方法");
		}
		if(data == null) {
			data = new JSONArray();
		}
		return data;
	}
	
	public static void writeData(String filePath) throws Exception {
		FileUtils.writeStringToFile(new File(filePath), data.toString(), "utf-8");
	}
}
package cn.pandaframework;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import cn.pandaframework.data.DataProcess;

public class AppStart {
	private static SimpleCommandLinePropertySource simpleCommandLinePropertySource = null;
	private static final String panda_microserviceTestTool_dataPath = "panda.microserviceTestTool.dataPath";
	private static final String dataCfgPathDefault = "/panda/microservice-test-tool/data-cfg.json";
	public static String dataCfgPath;
	
	public static void main(String[] args) throws Exception {
		simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
		dataCfgPath = simpleCommandLinePropertySource.getProperty(panda_microserviceTestTool_dataPath);
		dataCfgPath = StringUtils.isNotBlank(dataCfgPath)?dataCfgPath:dataCfgPathDefault;
		DataProcess.loadData(dataCfgPath);
		
		App.main(args);
	}
}

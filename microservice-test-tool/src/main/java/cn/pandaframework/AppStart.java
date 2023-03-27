package cn.pandaframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.alibaba.fastjson2.JSON;

import cn.pandaframework.common.compiler.JavaCompiler;
import cn.pandaframework.common.freemarker.FreemarkerUtils;
import cn.pandaframework.data.DataProcess;
import cn.pandaframework.model.Method;
import cn.pandaframework.service.dubbo.model.User;

public class AppStart {
	private static SimpleCommandLinePropertySource simpleCommandLinePropertySource = null;
	private static final String panda_microserviceTestTool_dataPath = "panda.microserviceTestTool.dataPath";
	private static final String dataCfgPathDefault = "/panda/microservice-test-tool/data-cfg.json";
	public static String dataCfgPath;
	
	public static void main(String[] args) throws Exception {
		simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
		dataCfgPath = simpleCommandLinePropertySource.getProperty(panda_microserviceTestTool_dataPath);
		dataCfgPath = StringUtils.isNotBlank(dataCfgPath)?dataCfgPath:dataCfgPathDefault;
		System.out.println(dataCfgPath);
		DataProcess.loadData(dataCfgPath);
		System.out.println(DataProcess.getData());
		
		App.main(args);
		
		/*
		String fullName = "cn.pandaframework.service.dubbo.api.GreetingsService";
		int lastIndexOf = fullName.lastIndexOf(".");
		String packageName = fullName.substring(0,lastIndexOf);
		String className = fullName.substring(lastIndexOf+1);
		Map<String,Object> dataModel = new HashMap<>();
		dataModel.put("packageName", packageName);
		dataModel.put("className", className);
		List<Method> listMethod = new ArrayList<>();
		Method method = new Method();
		method.setName("sayHi");
		List<String> listArgs = new ArrayList<>();
		listArgs.add("java.lang.String");
		method.setArgs(listArgs);
		listMethod.add(method);
		
		method = new Method();
		method.setName("sayHi");
		listArgs = new ArrayList<>();
		listArgs.add("com.alibaba.fastjson2.JSONObject");
		method.setArgs(listArgs);
		listMethod.add(method);
		
		dataModel.put("listMethod", listMethod);
		System.out.println(dataModel);
		String sourceCode = FreemarkerUtils.generate("javaInterface.ftl", dataModel);
		System.out.println(sourceCode);
		Class<?> javaCompiler = JavaCompiler.javaCompiler(packageName, className, sourceCode);
		
		ReferenceConfig<Object> reference = new ReferenceConfig<>();
        reference.setInterface(javaCompiler);

        DubboBootstrap.getInstance()
                .application("first-dubbo-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference);

        Object object = reference.get();
        Class<? extends Object> clazz = object.getClass();
        java.lang.reflect.Method[] methods = clazz.getMethods();
        System.out.println(JSON.toJSON(methods));
        
        java.lang.reflect.Method method2 = clazz.getMethod("sayHi", String.class);
        Object invoke = method2.invoke(object, new Object[] {"一二三四五"});
        System.out.println(invoke);
        
        Class<?> clazzUser = User.class;
        String jsonStr = "{\"name\":\"张三疯\",\"age\":20,\"sale\":4444.4}";
        Object parseObject = JSON.parseObject(jsonStr,clazzUser);
        method2 = clazz.getMethod("sayHi", clazzUser);
        invoke = method2.invoke(object, new Object[] {parseObject});
        System.out.println(invoke);
        */
	}
}

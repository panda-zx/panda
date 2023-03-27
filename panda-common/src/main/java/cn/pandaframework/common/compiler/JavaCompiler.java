package cn.pandaframework.common.compiler;

import java.util.Map;

import com.itranswarp.compiler.JavaStringCompiler;

public class JavaCompiler {
	public static Class<?> javaCompiler(String packageName, String className, String sourceCode) throws Exception {
		JavaStringCompiler compiler = new JavaStringCompiler();
		Map<String, byte[]> results = compiler.compile(className+".java", sourceCode);
        Class<?> clazz = compiler.loadClass(packageName+"."+className, results);
        return clazz;
	}
}
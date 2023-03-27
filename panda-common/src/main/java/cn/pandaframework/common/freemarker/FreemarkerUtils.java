package cn.pandaframework.common.freemarker;

import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * freemarker工具类
 * @author zhaoxin
 * @date   2016年10月14日 下午10:28:22
 */
public class FreemarkerUtils {
	private static Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);
	
	private static String basePackagePath = "/ftl";
	private static Configuration cfg = null;
	
	static {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(FreemarkerUtils.class, basePackagePath);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        logger.debug("FreemarkerUtils.basePackagePath 默认路径：{}",basePackagePath);
	}
	
	public static String generate(String template,Object dataModel) throws Exception {
        Template temp = cfg.getTemplate(template);

        Writer out = new StringWriter();
        temp.process(dataModel, out);
        
        return out.toString();
	}
}
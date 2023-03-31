package cn.pandaframework.model;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author zxdzy
 *
 */
public class MethodTreeItem {
	private JSONObject methodJson;
	
	public MethodTreeItem(JSONObject methodJson) {
		super();
		this.methodJson = methodJson;
	}

	public JSONObject getMethodJson() {
		return methodJson;
	}

	public void setMethodJson(JSONObject methodJson) {
		this.methodJson = methodJson;
	}

	@Override
	public String toString() {
		return methodJson.getString("name");
	}
}
package cn.pandaframework.model;

import java.util.List;

public class Method {
	private String name;
	private List<String> args;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}
}
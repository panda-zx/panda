package cn.pandaframework.service.dubbo.model;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	private Double sale;
	public User() {
		super();
	}
	public User(String name, int age, Double sale) {
		super();
		this.name = name;
		this.age = age;
		this.sale = sale;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", sale=" + sale + "]";
	}
}
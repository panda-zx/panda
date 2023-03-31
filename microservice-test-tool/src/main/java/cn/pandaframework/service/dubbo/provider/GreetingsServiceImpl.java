package cn.pandaframework.service.dubbo.provider;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;

import cn.pandaframework.service.dubbo.api.GreetingsService;
import cn.pandaframework.service.dubbo.model.User;

public class GreetingsServiceImpl implements GreetingsService {
	public static void main(String[] args) {
		GreetingsService s = new GreetingsServiceImpl();
		int a1 = 1;
		Integer a2 = 1;
		Number a3 = 1;
		System.out.println(s.sayHi(a1));
		System.out.println(s.sayHi(a2));
		System.out.println(s.sayHi(a3));
	}

	@Override
	public String sayHi(int a) {
		return "int a";
	}

	@Override
	public String sayHi(Integer a) {
		return "Integer a";
	}

	@Override
	public String sayHi(Number a) {
		return "Number a";
	}
	
	@Override
	public String sayHi(User user) {
		System.out.println("user");
		return JSON.toJSONString(user);
	}

	@Override
	public String sayHi(List<User> list) {
		System.out.println("list");
		return JSON.toJSONString(list);
	}

	@Override
	public String sayHi(Map<String, User> map) {
		System.out.println("map");
		return JSON.toJSONString(map);
	}
}
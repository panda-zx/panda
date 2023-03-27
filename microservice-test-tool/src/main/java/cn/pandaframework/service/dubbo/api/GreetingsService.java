package cn.pandaframework.service.dubbo.api;

import java.util.List;
import java.util.Map;

import cn.pandaframework.service.dubbo.model.User;

public interface GreetingsService {
	String sayHi(int a);
	String sayHi(Integer a);
	String sayHi(Number a);
	String sayHi(List<User> list);
	String sayHi(Map<String,User> map);
}

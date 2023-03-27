package cn.pandaframework.service.dubbo.client;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

import com.alibaba.fastjson2.JSONObject;

import cn.pandaframework.service.dubbo.api.GreetingsService;
import cn.pandaframework.service.dubbo.model.User;

public class App {
	public static void main(String[] args) throws Exception {
		ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
        reference.setInterface(GreetingsService.class);

        DubboBootstrap.getInstance()
                .application("first-dubbo-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference);

        GreetingsService service = reference.get();
        System.in.read();
	}
}
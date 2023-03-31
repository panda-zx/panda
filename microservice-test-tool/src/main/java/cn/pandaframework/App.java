package cn.pandaframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import cn.pandaframework.common.compiler.JavaCompilerUtils;
import cn.pandaframework.common.freemarker.FreemarkerUtils;
import cn.pandaframework.common.password.MD5Utils;
import cn.pandaframework.data.DataProcess;
import cn.pandaframework.menu.AddPane;
import cn.pandaframework.model.Method;
import cn.pandaframework.model.MethodTreeItem;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
	private VBox left = new VBox();
	private VBox rightVBox = new VBox();
	private TextArea resultArea = new TextArea();
	
	private static final String[] javaTypes = new String[]{"byte","short","int","long","float","double","boolean","char"
			,"java.lang.Byte","java.lang.Short","java.lang.Integer","java.lang.Long","java.lang.Float","java.lang.Double","java.lang.Boolean","java.lang.Character"
			,"java.util.ArrayList","java.util.HashMap"
			};
	private static List<Map<String,Control>> paramComponentList = new ArrayList<>();
	private TextField classFullNameText = new TextField();
	private TextField methodNameText = new TextField();
	private static Integer leftClickedIndex = null;
	private static String[] keywords = new String[] {"type","registry","name"}; 
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu();
		Label label = new Label("连接");
		label.setOnMouseClicked(e -> new AddPane(this,primaryStage).show());
		menu.setGraphic(label);
		menuBar.getMenus().addAll(menu);
		root.setTop(menuBar);
		
		refreshLeft();
		root.setLeft(left);
		
		BorderPane right = new BorderPane();
		right.setCenter(rightVBox);
		VBox vBox = new VBox();
		vBox.getChildren().add(new Label("结果"));
		vBox.getChildren().add(resultArea);
		right.setBottom(vBox);
		classFullNameText.setPromptText("全类名");
		rightVBox.getChildren().add(classFullNameText);
		
		methodNameText.setPromptText("方法名");
		rightVBox.getChildren().add(methodNameText);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BASELINE_LEFT);
		Button addParam = new Button("添加参数");
		addParam.setOnMouseClicked(e -> {
			addParam();
		});
		hBox.getChildren().add(addParam);
		Button save = new Button("保存");
		save.setOnMouseClicked(e -> {
			if(leftClickedIndex == null) {
				new Alert(AlertType.ERROR, "请选择左侧一个连接").show();
				return ;
			}
			JSONObject params = getParams();
			String classFullName = params.getString("classFullName");
			JSONArray newMethodArr = params.getJSONArray("method");
			JSONObject newMethodJson = newMethodArr.getJSONObject(0);
			JSONObject jsonObject = DataProcess.getData().getJSONObject(leftClickedIndex);
			JSONArray methodArr = jsonObject.getJSONArray(classFullName);
			if(methodArr == null) {
				jsonObject.put(classFullName, newMethodArr);
			}else {
				boolean exist = false;
				
				String newId = newMethodJson.getString("id");
				for (int i = 0; i < methodArr.size(); i++) {
					JSONObject methodJson = methodArr.getJSONObject(i);
					if(StringUtils.equals(newId, methodJson.getString("id"))) {
						methodJson.putAll(newMethodJson);
						exist = true;
						break;
					}
				}
				if(!exist) {
					methodArr.add(newMethodJson);
				}
			}
			try {
				DataProcess.writeData(AppStart.dataCfgPath);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			refreshLeft();
		});
		hBox.getChildren().add(save);
		Button test = new Button("测试");
		test.setOnMouseClicked(e -> {
			JSONObject params = getParams();
			String classFullName = params.getString("classFullName");
			JSONObject methodJson = params.getJSONArray("method").getJSONObject(0);
			try {
				test(classFullName,methodJson);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		hBox.getChildren().add(test);
		rightVBox.getChildren().add(hBox);
		
		root.setCenter(right);
		
		Scene scene = new Scene(root, 1000, 600);
		primaryStage.setTitle("microservice-test");
		primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public void refreshLeft() {
		left.getChildren().clear();
		JSONArray data = DataProcess.getData();
		for (int i = 0; i < data.size(); i++) {
			JSONObject jsonObject = data.getJSONObject(i);
			String type = jsonObject.getString("type");
			String registry = jsonObject.getString("registry");
			String name = jsonObject.getString("name");
			name = StringUtils.isNotBlank(name)?name:(type+registry);
			TreeItem<Object> rootItem = new TreeItem<>(name);
			for(String key : jsonObject.keySet()) {
				if(!ArrayUtils.contains(keywords, key)) {
					TreeItem<Object> classItem = new TreeItem<>(key);
					rootItem.getChildren().add(classItem);
					JSONArray methodArr = jsonObject.getJSONArray(key);
					for (int j = 0; j < methodArr.size(); j++) {
						JSONObject methodJson = methodArr.getJSONObject(j);
						TreeItem<Object> methodItem = new TreeItem<>(new MethodTreeItem(methodJson));
						classItem.getChildren().add(methodItem);
					}
				}
			}
			TreeView<Object> leftTree = new TreeView<>(rootItem);
			int index = i;
			leftTree.setOnMouseClicked(e -> {
				leftClickedIndex = index;
			});
			leftTree.getSelectionModel().selectedItemProperty().addListener(e -> {
				TreeItem<Object> selectedItem = leftTree.getSelectionModel().getSelectedItem();
				Object value = selectedItem.getValue();
				rightVBox.getChildren().remove(3, rightVBox.getChildren().size());
				paramComponentList.clear();
				if(value instanceof String && selectedItem.getParent()!=null) {
					classFullNameText.setText(value.toString());
					methodNameText.setText("");
				}else if(value instanceof MethodTreeItem) {
					classFullNameText.setText(selectedItem.getParent().getValue().toString());
					MethodTreeItem methodTreeItem = (MethodTreeItem)value;
					JSONObject methodJson = methodTreeItem.getMethodJson();
					methodNameText.setText(methodJson.getString("name"));
					JSONArray paramArr = methodJson.getJSONArray("params");
					for(int j=0; j<paramArr.size(); j++) {
						JSONObject paramJson = paramArr.getJSONObject(j);
						addParam(paramJson.getString("javaType"), paramJson.getString("value"));
					}
				}
			});
			left.getChildren().add(leftTree);
		}
	}
	
	public void addParam() {
		HBox hBox = new HBox();
		ComboBox<String> cbb = new ComboBox<>();
		cbb.getItems().addAll(javaTypes);
		cbb.setEditable(true);
		cbb.setPromptText("类型");
		hBox.getChildren().add(cbb);
		
		TextField valueText = new TextField();
		valueText.setPromptText("值");
		hBox.getChildren().add(valueText);
		
		Map<String,Control> map = new HashMap<>();
		map.put("javaType", cbb);
		map.put("value", valueText);
		paramComponentList.add(map);
		
		Button delParam = new Button(" - ");
		delParam.setOnMouseClicked(e -> {
			rightVBox.getChildren().remove(hBox);
			paramComponentList.remove(map);
		});
		hBox.getChildren().add(delParam);
		rightVBox.getChildren().add(hBox);
	}
	
	public void addParam(String javaType,String value) {
		HBox hBox = new HBox();
		ComboBox<String> cbb = new ComboBox<>();
		cbb.getItems().addAll(javaTypes);
		cbb.setEditable(true);
		cbb.setPromptText("类型");
		cbb.setValue(javaType);
		hBox.getChildren().add(cbb);
		
		TextField valueText = new TextField(value);
		valueText.setPromptText("值");
		hBox.getChildren().add(valueText);
		
		Map<String,Control> map = new HashMap<>();
		map.put("javaType", cbb);
		map.put("value", valueText);
		paramComponentList.add(map);
		
		Button delParam = new Button(" - ");
		delParam.setOnMouseClicked(e -> {
			rightVBox.getChildren().remove(hBox);
			paramComponentList.remove(map);
		});
		hBox.getChildren().add(delParam);
		rightVBox.getChildren().add(hBox);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getParams() {
		JSONObject json = new JSONObject();
		json.put("classFullName", classFullNameText.getText());
		JSONArray methodArr = new JSONArray();
		JSONObject methodJson = new JSONObject();
		methodJson.put("name", methodNameText.getText());
		JSONArray paramArr = new JSONArray();
		for (Map<String,Control> map : paramComponentList) {
			String javaType = ((ComboBox<String>)map.get("javaType")).getValue();
			String value = ((TextField)map.get("value")).getText();
			JSONObject paramJson = new JSONObject();
			paramJson.put("javaType", javaType);
			paramJson.put("value", value);
			paramArr.add(paramJson);
		}
		methodJson.put("params", paramArr);
		methodJson.put("id", getUid(methodJson));
		methodArr.add(methodJson);
		json.put("method", methodArr);
		return json;
	}
	
	public String getUid(JSONObject methodJson) {
		JSONArray paramArr = methodJson.getJSONArray("params");
		List<String> list = new ArrayList<>();
		list.add(methodJson.getString("name"));
		for (int i = 0; i < paramArr.size(); i++) {
			JSONObject paramJson = paramArr.getJSONObject(i);
			String javaType = paramJson.getString("javaType");
			list.add(javaType);
		}
		return MD5Utils.enMD5(JSON.toJSONString(list));
	}
	
	public void test(String classFullName, JSONObject methodJson) throws Exception {
		if(leftClickedIndex == null) {
			new Alert(AlertType.ERROR, "请选择左侧一个连接").show();
			return ;
		}
		
		String methodName = methodJson.getString("name");
		JSONArray paramArr = methodJson.getJSONArray("params");
		int lastIndexOf = classFullName.lastIndexOf(".");
		String packageName = classFullName.substring(0,lastIndexOf);
		String className = classFullName.substring(lastIndexOf+1);
		Map<String,Object> dataModel = new HashMap<>();
		dataModel.put("packageName", packageName);
		dataModel.put("className", className);
		List<Method> listMethod = new ArrayList<>();
		Method method = new Method();
		method.setName(methodName);
		List<String> listArgs = new ArrayList<>();
		Class<?>[] paramClasses = new Class[paramArr.size()];
		Object[] values = new Object[paramArr.size()];
		for (int i = 0; i < paramArr.size(); i++) {
			String javaType = paramArr.getJSONObject(i).getString("javaType");
			String value = paramArr.getJSONObject(i).getString("value");
			listArgs.add(javaType);
			paramClasses[i] = getClassByJavaType(javaType);
			values[i] = getValueByJavaType(javaType,value);
		}
		
		method.setArgs(listArgs);
		listMethod.add(method);
		
		dataModel.put("listMethod", listMethod);
		String sourceCode = FreemarkerUtils.generate("javaInterface.ftl", dataModel);
		Class<?> javaCompiler = JavaCompilerUtils.javaCompiler(packageName, className, sourceCode);
		
		ReferenceConfig<Object> reference = new ReferenceConfig<>();
        reference.setInterface(javaCompiler);

        JSONObject jsonObject = DataProcess.getData().getJSONObject(leftClickedIndex);
        DubboBootstrap.getInstance()
                .application(jsonObject.getString("name"))
                .registry(new RegistryConfig(jsonObject.getString("registry")))
                .reference(reference);

        Object object = reference.get();
        Class<? extends Object> clazz = object.getClass();
        java.lang.reflect.Method method2 = clazz.getMethod(method.getName(), paramClasses);
        
        Object result = method2.invoke(object, values);
        resultArea.setText(JSON.toJSONString(result));
	}
	
	public Class<?> getClassByJavaType(String javaType) throws Exception {
		switch (javaType) {
		case "byte": {
			return byte.class;
		}
		case "short": {
			return short.class;
		}
		case "int": {
			return int.class;
		}
		case "long": {
			return long.class;
		}
		case "float": {
			return float.class;
		}
		case "double": {
			return double.class;
		}
		case "boolean": {
			return boolean.class;
		}
		case "char": {
			return char.class;
		}
		default:
			return Class.forName(javaType);
		}
	}
	
	public Object getValueByJavaType(String javaType,String value) throws Exception {
		if(StringUtils.equalsAny(javaType, "byte", "java.lang.Byte")) {
			return Byte.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "short", "java.lang.Short")) {
			return Short.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "int", "java.lang.Integer")) {
			return Integer.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "long", "java.lang.Long")) {
			return Long.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "float", "java.lang.Float")) {
			return Float.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "double", "java.lang.Double")) {
			return Double.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "boolean", "java.lang.Boolean")) {
			return Boolean.valueOf(value);
		}else if(StringUtils.equalsAny(javaType, "char", "java.lang.Character")) {
			return Character.valueOf(value.charAt(0));
		}
		return null;
	}
}
package cn.pandaframework;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import cn.pandaframework.data.DataProcess;
import cn.pandaframework.menu.AddPane;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
	private static VBox left = new VBox();
	private static VBox right = new VBox();
	private static final String[] javaTypes = new String[]{"byte","short","int","long","float","double","boolean","char"
			,"java.lang.Byte","java.lang.Short","java.lang.Integer","java.lang.Long","java.lang.Float","java.lang.Double","java.lang.Boolean","java.lang.Character"
			,"java.util.ArrayList","java.util.HashMap"
			};
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu();
		Label label = new Label("连接");
		label.setOnMouseClicked(e -> new AddPane().show());
		menu.setGraphic(label);
		menuBar.getMenus().addAll(menu);
		root.setTop(menuBar);
		
		refreshLeft();
		root.setLeft(left);
		
		TextField classFullNameText = new TextField();
		classFullNameText.setPromptText("全类名");
		right.getChildren().add(classFullNameText);
		
		
		TextField methodNameText = new TextField();
		methodNameText.setPromptText("方法名");
		right.getChildren().add(methodNameText);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BASELINE_LEFT);
		Button addParam = new Button("添加参数");
		addParam.setOnMouseClicked(e -> {
			addParam();
		});
		hBox.getChildren().add(addParam);
		Button save = new Button("保存");
		hBox.getChildren().add(save);
		Button test = new Button("测试");
		hBox.getChildren().add(test);
		right.getChildren().add(hBox);
		
		root.setCenter(right);
		
		Scene scene = new Scene(root, 1000, 600);
		primaryStage.setTitle("microservice-test");
		primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void refreshLeft() {
		left.getChildren().clear();
		JSONArray data = DataProcess.getData();
		for (int i = 0; i < data.size(); i++) {
			JSONObject jsonObject = data.getJSONObject(i);
			String type = jsonObject.getString("type");
			String registry = jsonObject.getString("registry");
			String name = jsonObject.getString("name");
			name = StringUtils.isNotBlank(name)?name:(type+registry);
			TreeItem<String> rootItem = new TreeItem<>(name);
			TreeView<String> leftTree = new TreeView<>(rootItem);
			left.getChildren().add(leftTree);
		}
	}
	
	public static void addParam() {
		HBox hBox = new HBox();
		ComboBox<String> cbb = new ComboBox<>();
		cbb.getItems().addAll(javaTypes);
		cbb.setEditable(true);
		cbb.setPromptText("类型");
		hBox.getChildren().add(cbb);
		
		TextField valueText = new TextField();
		valueText.setPromptText("值");
		hBox.getChildren().add(valueText);
		
		Button delParam = new Button(" - ");
		delParam.setOnMouseClicked(e -> {
			right.getChildren().remove(hBox);
		});
		hBox.getChildren().add(delParam);
		right.getChildren().add(hBox);
	}
}
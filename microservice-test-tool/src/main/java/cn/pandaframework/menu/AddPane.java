package cn.pandaframework.menu;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import cn.pandaframework.App;
import cn.pandaframework.AppStart;
import cn.pandaframework.data.DataProcess;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class AddPane {
	private App app;
	private Stage parentStage; 
	
	public AddPane(App app, Stage parentStage) {
		super();
		this.app = app;
		this.parentStage = parentStage;
	}

	public void show() {
		Stage stage = new Stage();
		
		VBox root = new VBox();
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
//		choiceBox.setItems(FXCollections.observableArrayList("duboo","openfeign"));
		choiceBox.getItems().addAll("openfeign","duboo");
		choiceBox.getSelectionModel().selectLast();
		root.getChildren().add(choiceBox);
		
		TextField registryText = new TextField();
		registryText.setPromptText("registry。例如：zookeeper://127.0.0.1:2181");
		root.getChildren().add(registryText);
		
		TextField nameText = new TextField();
		nameText.setPromptText("name");
		root.getChildren().add(nameText);
		
		Button button = new Button("保存");
		button.setOnMouseClicked(e -> {
			String type = choiceBox.getValue();
			String registry = registryText.getText();
			String name = nameText.getText();
			name = StringUtils.isNotBlank(name)?name:(type+registry);
			
			JSONArray dataArr = DataProcess.getData();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", type);
			jsonObject.put("registry", registry);
			jsonObject.put("name", name);
			dataArr.add(jsonObject);
			
			try {
				DataProcess.writeData(AppStart.dataCfgPath);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			app.refreshLeft();
			stage.close();
		});
		root.getChildren().add(button);
		
		Scene scene = new Scene(root, 400, 300);
		stage.setScene(scene);
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    public void handle(WindowEvent we) {
		    	System.out.println(we);
		    }
		});
	}
}
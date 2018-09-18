import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class FxFrame extends Application{
	private String[] searchContents;
	private String[] positionx;
	private String[] positiony;
	JsonParser parser;
	
	@Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();	//grid panel
        FlowPane flow = new FlowPane();	//flow panel
        Button searchButton = new Button("Search");
        Text scenetitle = new Text("Welcome");
        Label input_dest_label = new Label("Input destination: ");
        TextField searchtext = new TextField();
        ComboBox<String> searchInform = new ComboBox<String>();
        
        //title font 지정
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                
        
        
        //App 제목 설정
        stage.setTitle("Jeju Map route");
        
        //webview fram set
        WebView webview = new WebView();
        
        //webengine set
        WebEngine engine = webview.getEngine();
        //webpage 불러오기
        engine.load("http://localhost:8080/");

        grid.setAlignment(Pos.CENTER);	//중앙 정렬
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));	//테두리 간격
        
        //flowpane
        flow.getChildren().add(input_dest_label);
        flow.getChildren().add(searchtext);;
        flow.getChildren().add(searchButton);
        
        //gridpane 
        //(0,0) 열 방향 2개 병합
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(flow, 0, 1,2,1);
        grid.add(searchInform, 0, 2);
        grid.add(webview, 0, 3,3,1);
        Scene scene = new Scene(grid, 800, 700);
        stage.setScene(scene);
        stage.show();
    }
	
	
}

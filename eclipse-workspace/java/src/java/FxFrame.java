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
	private String[] roadAddress;
	private PathList pathlist;
	
	//private String[] latlngx;
	//private String[] latlngy;
	
	JsonParser parser;
	
	@Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();	//grid panel
        FlowPane flow = new FlowPane();	//flow panel
        Button searchButton = new Button("Search");
        Button startDestinationselectButton = new Button("Select Start Dest");
        Button arrivalDesitionationselectButton = new Button("Select Arrival Dest");
        Button DestinationselectButton = new Button("Select mid Dest");
        Button calculator = new Button("�Ÿ� ���");
        Text scenetitle = new Text("Welcome");
        Label input_dest_label = new Label("Input destination: ");
        TextField searchtext = new TextField();
        ComboBox<String> searchInform = new ComboBox<String>();
        
        //title font ����
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	//���� ��� ����ϱ�
		            	searchInform.getItems().clear();        	
						String str = searchtext.getText();
						searchAPI(str);
						
						//�˻���� ���
						for(int i=0; i<searchContents.length; i++)
							searchInform.getItems().add(searchContents[i]);
												
		            }
		        });

		pathlist = new PathList();
		
		startDestinationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//�޺��ڽ��� ���õ� ������ �����ϱ�	
            	//index 0���� ����
            	//���õ� index�� true�� �ٲ��ش�
            	int index = searchInform.getSelectionModel().getSelectedIndex();
            	
            	//������ ��� ����
            	pathlist.setStartDestination(parser.getTitle(index), parser.getlatlngy(index) ,parser.getlatlngx(index));
            	pathlist.printTravelRoute();
            }
        });
		
		DestinationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//�޺��ڽ��� ���õ� ������ �����ϱ�	
            	//index 0���� ����
            	//���õ� index�� true�� �ٲ��ش�
            	int index = searchInform.getSelectionModel().getSelectedIndex();
            	
            	//������ ��� ����
            	pathlist.addDestination(2, parser.getTitle(index), parser.getlatlngy(index) ,parser.getlatlngx(index));
            	pathlist.printTravelRoute();
            }
        });
		
		
		arrivalDesitionationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//�޺��ڽ��� ���õ� ������ �����ϱ�	
            	//index 0���� ����
            	//���õ� index�� true�� �ٲ��ش�
            	int index = searchInform.getSelectionModel().getSelectedIndex();
                        	
            	//������ ��� ����
            	pathlist.setArrivalDestination(parser.getTitle(index), parser.getlatlngy(index) ,parser.getlatlngx(index));
            	pathlist.printTravelRoute();
            }
        });

		
		calculator.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Algorithm algorithm = new Algorithm(pathlist);
            	algorithm.distance();
            }
        });
        //App ���� ����
        stage.setTitle("Jeju Map route");
        
        //webview fram set
        WebView webview = new WebView();
        
        //webengine set
        WebEngine engine = webview.getEngine();
        //webpage �ҷ�����
        engine.load("http://localhost:8080/");

        grid.setAlignment(Pos.CENTER);	//�߾� ����
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));	//�׵θ� ����
        
        //flowpane
        flow.getChildren().add(input_dest_label);
        flow.getChildren().add(searchtext);;
        flow.getChildren().add(searchButton);
        flow.getChildren().add(startDestinationselectButton);
        flow.getChildren().add(DestinationselectButton);
        flow.getChildren().add(arrivalDesitionationselectButton);
        flow.getChildren().add(calculator);
        
        //gridpane 
        //(0,0) �� ���� 2�� ����
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(flow, 0, 1,2,1);
        grid.add(searchInform, 0, 2);
        grid.add(webview, 0, 3,3,1);
        Scene scene = new Scene(grid, 800, 750);
        stage.setScene(scene);
        stage.show();
    }
	
	public void searchAPI(String searchText) {
		String clientId = "wTtvnHGIqwor89f7Dx_V";//���ø����̼� Ŭ���̾�Ʈ ���̵�";
        String clientSecret = "7fcuen5EPN";//���ø����̼� Ŭ���̾�Ʈ ��ũ����";
        
        try {
            String text = URLEncoder.encode(searchText, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local?query="+ text; // json ���
            URL url = new URL(apiURL);
            
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            
            int responseCode = con.getResponseCode();	//�����ڵ� 200�̸� ����
            String str;
            
            if(responseCode==200) { // ���� ȣ��
            	InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
            	BufferedReader reader = new BufferedReader(tmp);
            	StringBuffer buffer = new StringBuffer();
            	while((str = reader.readLine()) != null){
            		str = removeHTML(str);
            		buffer.append(str).append("\n");
            	}
            	
            	String jsonString = buffer.toString();
            	//System.out.println(jsonString);
            	//���� �˻� ��� ����ֱ�
            	parser = new JsonParser();	//json parser
            	parser.setJsonString(jsonString);
              	parser.Parsing();	//������ parsing�ϱ�
              	
              	this.searchContents = parser.getTitle();	//combobox�� �˻��� ��� �־��ֱ�
              	this.roadAddress = parser.getAddress();
              	reader.close();
              	
            } else {  // ���� �߻�
            	System.out.println(con.getResponseCode());
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
	}
	
	//html tag remove function
	public static String removeHTML(String str) {
		str = str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		return str;
	}
}

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
        Button calculator = new Button("거리 계산");
        Text scenetitle = new Text("Welcome");
        Label input_dest_label = new Label("Input destination: ");
        TextField searchtext = new TextField();
        ComboBox<String> searchInform = new ComboBox<String>();
        
        //title font 지정
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	//새로 목록 출력하기
		            	searchInform.getItems().clear();        	
						String str = searchtext.getText();
						searchAPI(str);
						
						//검색목록 출력
						for(int i=0; i<searchContents.length; i++)
							searchInform.getItems().add(searchContents[i]);
												
		            }
		        });

		pathlist = new PathList();
		
		startDestinationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//콤보박스에 선택된 여행지 선택하기	
            	//index 0부터 시작
            	//선택된 index를 true로 바꿔준다
            	int index = searchInform.getSelectionModel().getSelectedIndex();
            	
            	//여행지 노드 생성
            	pathlist.setStartDestination(parser.getTitle(index), parser.getlatlngy(index) ,parser.getlatlngx(index));
            	pathlist.printTravelRoute();
            }
        });
		
		DestinationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//콤보박스에 선택된 여행지 선택하기	
            	//index 0부터 시작
            	//선택된 index를 true로 바꿔준다
            	int index = searchInform.getSelectionModel().getSelectedIndex();
            	
            	//여행지 노드 생성
            	pathlist.addDestination(2, parser.getTitle(index), parser.getlatlngy(index) ,parser.getlatlngx(index));
            	pathlist.printTravelRoute();
            }
        });
		
		
		arrivalDesitionationselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//콤보박스에 선택된 여행지 선택하기	
            	//index 0부터 시작
            	//선택된 index를 true로 바꿔준다
            	int index = searchInform.getSelectionModel().getSelectedIndex();
                        	
            	//여행지 노드 생성
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
        //App 제목 설정
        stage.setTitle("Jeju Map route");
        
        //webview fram set
        WebView webview = new WebView();
        
        //webengine set
        WebEngine engine = webview.getEngine();
        //webpage 불러오기
        engine.load("http://localhost:8080/");

        grid.setAlignment(Pos.CENTER);	//중앙 정렬
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));	//테두리 간격
        
        //flowpane
        flow.getChildren().add(input_dest_label);
        flow.getChildren().add(searchtext);;
        flow.getChildren().add(searchButton);
        flow.getChildren().add(startDestinationselectButton);
        flow.getChildren().add(DestinationselectButton);
        flow.getChildren().add(arrivalDesitionationselectButton);
        flow.getChildren().add(calculator);
        
        //gridpane 
        //(0,0) 열 방향 2개 병합
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(flow, 0, 1,2,1);
        grid.add(searchInform, 0, 2);
        grid.add(webview, 0, 3,3,1);
        Scene scene = new Scene(grid, 800, 750);
        stage.setScene(scene);
        stage.show();
    }
	
	public void searchAPI(String searchText) {
		String clientId = "wTtvnHGIqwor89f7Dx_V";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7fcuen5EPN";//애플리케이션 클라이언트 시크릿값";
        
        try {
            String text = URLEncoder.encode(searchText, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local?query="+ text; // json 결과
            URL url = new URL(apiURL);
            
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            
            int responseCode = con.getResponseCode();	//응답코드 200이면 정상
            String str;
            
            if(responseCode==200) { // 정상 호출
            	InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
            	BufferedReader reader = new BufferedReader(tmp);
            	StringBuffer buffer = new StringBuffer();
            	while((str = reader.readLine()) != null){
            		str = removeHTML(str);
            		buffer.append(str).append("\n");
            	}
            	
            	String jsonString = buffer.toString();
            	//System.out.println(jsonString);
            	//지역 검색 목록 띄워주기
            	parser = new JsonParser();	//json parser
            	parser.setJsonString(jsonString);
              	parser.Parsing();	//정보들 parsing하기
              	
              	this.searchContents = parser.getTitle();	//combobox에 검색된 목록 넣어주기
              	this.roadAddress = parser.getAddress();
              	reader.close();
              	
            } else {  // 에러 발생
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
	public static final int MAX_CONTENTS = 10;
	private String jsonString;
	private String[] searchContents;	//장소
	private String[] category;	//분류
	private String[] roadAddress;
	private String[] latlngx;	//longtitude(경도)
	private String[] latlngy;	//latitude(위도)
	
	
	//Counstructor
	public JsonParser() {
		this.searchContents = new String[MAX_CONTENTS];
		this.category = new String[MAX_CONTENTS];
		this.roadAddress = new String[MAX_CONTENTS];
		this.latlngx = new String[MAX_CONTENTS];
		this.latlngy = new String[MAX_CONTENTS];
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	//latlng 좌표계 x와 y축 parsing
	public void Parsing(int i) throws ParseException{
		JSONParser parser = new JSONParser();
		//json 형태로 parse
		JSONObject item = (JSONObject)parser.parse(jsonString);
		//item 하나씩 json에 담기
		
		JSONObject tmp = (JSONObject)item.get("result");
		JSONArray tmp2 = (JSONArray)tmp.get("items");
		
		JSONObject jarr = (JSONObject)tmp2.get(0);
		JSONObject point = (JSONObject)jarr.get("point");
		latlngx[i] = point.get("x").toString();
		latlngy[i] = point.get("y").toString();
		
		//System.out.println("x: " + latlngx[i] + " y: " + latlngy[i]);
	}
	
	//검색된 정보들 parsing
	public void Parsing() throws ParseException {
		JSONParser parser = new JSONParser();
		//json 형태로 parse
		JSONObject item = (JSONObject)parser.parse(jsonString);
		//item 하나씩 json에 담기
		
		JSONArray jarr = (JSONArray)item.get("items");
		
		for (int i = 0; i < jarr.size(); i++) {
			JSONObject tmp = (JSONObject)jarr.get(i);
			
			searchContents[i] = (String)tmp.get("title");
			
			category[i] = (String)tmp.get("category");
			roadAddress[i] = (String)tmp.get("roadAddress");
			
		}
		AddressToWGS84();
	}
	
	public void AddressToWGS84() {
		String clientId = "wTtvnHGIqwor89f7Dx_V";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7fcuen5EPN";//애플리케이션 클라이언트 시크릿값";
        
        for(int i=0; i<MAX_CONTENTS; i++) {    
	        try {
	        	String text = URLEncoder.encode(this.roadAddress[i], "UTF-8");
	            String apiURL = "https://openapi.naver.com/v1/map/geocode?codeType=latlng&query="+ text; // json 결과
	            URL url = new URL(apiURL);
	            
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            con.setRequestProperty("X-Naver-Client-Id", clientId);
	            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	            
	            int responseCode = con.getResponseCode();	//응답코드 200이면 정상
	            String str;
	            
	            Thread.sleep(100);
	            if(responseCode==200) { // 정상 호출
	            	InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
	            	BufferedReader reader = new BufferedReader(tmp);
	            	StringBuffer buffer = new StringBuffer();
	            	while((str = reader.readLine()) != null){
	            		str = FxFrame.removeHTML(str);
	            		buffer.append(str).append("\n");
	            	}
	            	
	            	String jsonString = buffer.toString();
	            	
	            	this.jsonString = jsonString;
	            	Parsing(i);
	            	
	              	reader.close();
	            } else {  // 에러 발생
	            	System.out.println(con.getResponseCode());
	            }
	            
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		}
	}
	
		
	
	//Get Method
	public String[] getTitle() {
		return searchContents;
	}
	public String getTitle(int i) {
		return searchContents[i];
	}
	
	public String[] getCategory() {
		return category;
	}
	public String getCategory(int i) {
		return category[i];
	}
	
	public String[] getAddress() {
		return roadAddress;
	}
	
	public String getAddress(int i) {
		return roadAddress[i];
	}
	
	public String getlatlngx(int i) {
		return latlngx[i];
	}
	
	public String getlatlngy(int i) {
		return latlngy[i];
	}
	
}

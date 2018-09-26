import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
	public static final int MAX_CONTENTS = 10;
	private String jsonString;
	private String[] searchContents;	//장소
	private String[] category;	//분류
	private String[] mapx;	//x축위치
	private String[] mapy;	//y축위치
	
	//Counstructor
	public JsonParser(String jsonString) {
		this.jsonString = jsonString;
		this.searchContents = new String[MAX_CONTENTS];
		this.category = new String[MAX_CONTENTS];
		this.mapx = new String[MAX_CONTENTS];
		this.mapy = new String[MAX_CONTENTS];
	}
	
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
			mapx[i] = (String)tmp.get("mapx");
			mapy[i] = (String)tmp.get("mapy");
			
			//확인을 위한 출력
			System.out.println(i + "번째  name");
			System.out.println(searchContents[i]);
			System.out.println("category: " + category[i]);
			System.out.println("mapx: " + mapx[i] + ", mapy: "+ mapy[i]);
			System.out.println();
		}
	}
	
	//
	public String[] getTitle() {
		return searchContents;
	}
	
	public String[] getCategory() {
		return category;
	}
	
	public String[] getMapx() {
		return mapx;
	}
	
	public String[] getMapy() {
		return mapy;
	}
}

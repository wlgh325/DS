import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
	public static final int MAX_CONTENTS = 10;
	private String jsonString;
	private String[] searchContents;	//���
	private String[] category;	//�з�
	private String[] mapx;	//x����ġ
	private String[] mapy;	//y����ġ
	
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
		//json ���·� parse
		JSONObject item = (JSONObject)parser.parse(jsonString);
		//item �ϳ��� json�� ���
		
		JSONArray jarr = (JSONArray)item.get("items");
		
		for (int i = 0; i < jarr.size(); i++) {
			JSONObject tmp = (JSONObject)jarr.get(i);
			
			searchContents[i] = (String)tmp.get("title");
			
			category[i] = (String)tmp.get("category");
			mapx[i] = (String)tmp.get("mapx");
			mapy[i] = (String)tmp.get("mapy");
			
			//Ȯ���� ���� ���
			System.out.println(i + "��°  name");
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

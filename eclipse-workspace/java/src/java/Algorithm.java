import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Algorithm {
	
	private PathList pathlist;
	public final int nearKm=25;
	private int pathnum;
	final int INF = 100000;
	static int check;
	private ArrayList<Integer>[] permu;
	private int fact;
	private String jsonString;
	
	//0번이 start_destination
	//마지막 index가 final_destination
	
	Algorithm(PathList pathlist){
		this.pathlist = pathlist;
		this.pathnum = pathlist.getcount();
		
		this.fact = fact(pathnum-2);
		permu = new ArrayList[this.fact];
		
		for (int i=0; i < this.fact; i++) {
			permu[i] = new ArrayList<Integer>();
		}

	}
	
	public void cal_time(int num) throws IOException {
        String appKey = "appkey";	//appKey
    	String startX = URLEncoder.encode(pathlist.getDestinationLon(num), "UTF-8");
    	String startY = URLEncoder.encode(pathlist.getDestinationLat(num), "UTF-8");

        for(int i=0; i<pathnum; i++) {    
	        try {
	        	if(i == num)
	        		continue;
	        	
	        	String endX = URLEncoder.encode(pathlist.getDestinationLon(i), "UTF-8");
	        	String endY = URLEncoder.encode(pathlist.getDestinationLat(i), "UTF-8");
	        	
	            String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&callback=application/json&appKey=" + appKey 
	            		+ "&endX=" + endX + "&endY=" + endY + "&startX=" + startX + "&startY=" + startY + "&totalValue=2"; // json 결과
	            URL url = new URL(apiURL);
	            
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            
	            
	            int responseCode = con.getResponseCode();	//응답코드 200이면 정상
	            String str;
	            
	            Thread.sleep(500);
	            if(responseCode==200) { // 정상 호출
	            	InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
	            	BufferedReader reader = new BufferedReader(tmp);
	            	StringBuffer buffer = new StringBuffer();
	            	while((str = reader.readLine()) != null){
	            		str = FxFrame.removeHTML(str);
	            		buffer.append(str).append("\n");
	            	}
	            	
	            	String jsonString = buffer.toString();
	            	//System.out.println(jsonString);
	            	this.jsonString = jsonString;
	            	Parsing(num, i);
	            	
	              	reader.close();
	            } else {  // 에러 발생
	            	System.out.println(con.getResponseCode());
	            }
	            
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		}
	}


	//검색된 정보들 parsing
	public void Parsing(int num, int i) throws ParseException {
		JSONParser parser = new JSONParser();
		//json 형태로 parse
		JSONObject item = (JSONObject)parser.parse(jsonString);
		//item 하나씩 json에 담기
		
		JSONArray tmp = (JSONArray)item.get("features");
		JSONObject tmp2 = (JSONObject)tmp.get(0);
		
		JSONObject tmp3 = (JSONObject)tmp2.get("properties");
		
		String str = tmp3.get("totalTime").toString();
		double dist = Double.parseDouble(str);
		
		System.out.println(num + " -> " + i + " : " + dist);
		pathlist.setTime(num, dist, i);	
	}
	
	//모든 경로를 탐색하여 최단 거리를 찾는다
	public void optimumRoute() {
		double sum = 0.0;
		double[] temp;
		int[] arr = new int[pathnum-2];
		double min=100000;
		ArrayList<Integer> path = new ArrayList<Integer>();	//최단 경로일때 경로를 담은 배열
		int i,j, k;
		
		check=0;
		for(i =0 ; i<pathnum-2; i++) {
			arr[i]=i+1;
		}
		
		perm(arr, 0,pathnum-2,pathnum-2);
	
		//모든 경로 탐색
		for(i=0; i<fact; i++) {
			temp = pathlist.getTime(0);
			for(j=0; j<pathnum-2; j++) {
				sum = sum + temp[permu[i].get(j)];
				temp = pathlist.getTime(permu[i].get(j));
			}
			sum+= temp[pathnum-1];

			if(min > sum) {
				min = sum;
				path = permu[i];
			}
			sum=0;
		}
		
		System.out.println(Math.round(min/60*10)/10.0 + " 분");
		
		for(i=0, k=0; i<pathnum -2; i++, k++) {
			pathlist.addDestination(i+1, pathlist.getDestination(path.get(i) +k));
			pathlist.printTravelRoute();
		}
		for(i=pathnum + path.size(), j=path.size(); j>0; i--, j--) {
			pathlist.delete(i-1);
			pathlist.printTravelRoute();
		}
		
		
		
	}
	
	public void perm(int[] arr, int depth, int n, int k) { 
		if (depth == k) { // 한번 depth 가 k로 도달하면 사이클이 돌았음. 출력함. 
			print(arr,k); 
			return; 
			} 
		for (int i = depth; i < n; i++) { 
			swap(arr, i, depth); 
			perm(arr, depth + 1, n, k); 
			swap(arr, i, depth); 
			} 
		} 
	   
	// 자바에서는 포인터가 없기 때문에 아래와 같이, 인덱스 i와 j를 통해 바꿔줌. 
	public void swap(int[] arr, int i, int j) {       
		int temp = arr[i]; 
		arr[i] = arr[j]; 
		arr[j] = temp; 
	}
	   
	public void print (int[] arr, int k) {
		for(int i=0; i<k; i++) {
			if(i==k -1) {
				permu[check].add(arr[i]);
				//System.out.println(arr[i]);
			}
	        else {
				permu[check].add(arr[i]);
				//System.out.print(arr[i] + ",");
			}
		}
		check++;
	}

	public int fact(int n) {
		if (n <= 1)
			return n;
		else 
			return fact(n-1) * n;

	}
	
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	
	//0���� start_destination
	//������ index�� final_destination
	
	Algorithm(PathList pathlist){
		this.pathlist = pathlist;
		this.pathnum = pathlist.getcount();
		
		this.fact = fact(pathnum-2);
		permu = new ArrayList[this.fact];
		
		for (int i=0; i < this.fact; i++) {
			permu[i] = new ArrayList<Integer>();
		}

	}
	
	public void realtimeDistance(int num) throws IOException {
        String appKey = "16a77aea-dbaa-4ddb-9b0a-355fcb924a58";	//appKey
    	String startX = URLEncoder.encode(pathlist.getDestinationLon(num), "UTF-8");
    	String startY = URLEncoder.encode(pathlist.getDestinationLat(num), "UTF-8");

        for(int i=0; i<pathnum; i++) {    
	        try {
	        	if(i == num)
	        		continue;
	        	
	        	String endX = URLEncoder.encode(pathlist.getDestinationLon(i), "UTF-8");
	        	String endY = URLEncoder.encode(pathlist.getDestinationLat(i), "UTF-8");
	        	
	            String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&callback=application/json&appKey=" + appKey 
	            		+ "&endX=" + endX + "&endY=" + endY + "&startX=" + startX + "&startY=" + startY + "&totalValue=2"; // json ���
	            URL url = new URL(apiURL);
	            
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            
	            
	            int responseCode = con.getResponseCode();	//�����ڵ� 200�̸� ����
	            String str;
	            
	            if(responseCode==200) { // ���� ȣ��
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
	            } else {  // ���� �߻�
	            	System.out.println(con.getResponseCode());
	            }
	            
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		}
	}


	//�˻��� ������ parsing
	public void Parsing(int num, int i) throws ParseException {
		JSONParser parser = new JSONParser();
		//json ���·� parse
		JSONObject item = (JSONObject)parser.parse(jsonString);
		//item �ϳ��� json�� ���
		
		JSONArray tmp = (JSONArray)item.get("features");
		JSONObject tmp2 = (JSONObject)tmp.get(0);
		
		JSONObject tmp3 = (JSONObject)tmp2.get("properties");
		
		String str = tmp3.get("totalDistance").toString();
		double dist = Double.parseDouble(str) / 1000.0;
		
		System.out.println(num + " -> " + i + " : " + dist);
		pathlist.setLength(num, dist, i);	
	}
	
	public void cal_distance(int num) {
		double cal_latitude = Double.parseDouble(pathlist.getDestinationLat(num));
		double cal_longtitude = Double.parseDouble(pathlist.getDestinationLon(num));
		
		pathlist.initialLength(num);
		
		for(int i=0; i<pathnum; i++) {
		
			//�ڱ��ڽ��� ����
			if(num == i) 
				continue;
			
			double latitude = Double.parseDouble(pathlist.getDestinationLat(i));
			double longtitude = Double.parseDouble(pathlist.getDestinationLon(i));
			
			double theta = cal_longtitude - longtitude;
			double dist = Math.sin(deg2rad(cal_latitude)) * Math.sin(deg2rad(latitude)) + 
					Math.cos(deg2rad(cal_latitude)) * Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(theta));
			
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			
			//kilometer	
			dist = Math.round(dist * 1.609344 * 1000)/ 1000.0;	//��°�ڸ� ���� �ݿø��Ͽ� ǥ��
			
			System.out.println(num + " -> " + i + " : " + dist);
			//pathlist.setLength(num, dist);
			pathlist.setLength(num, dist, i);
		}
		
	}
	
	
	//��� ��θ� Ž���Ͽ� �ִ� �Ÿ��� ã�´�
	public void optimumRoute() {
		double sum = 0.0;
		double[] temp;
		int[] arr = new int[pathnum-2];
		double min=100000;
		ArrayList<Integer> path = new ArrayList<Integer>();	//�ִ� ����϶� ��θ� ���� �迭
		int i,j, k;
		
		check=0;
		for(i =0 ; i<pathnum-2; i++) {
			arr[i]=i+1;
		}
		
		perm(arr, 0,pathnum-2,pathnum-2);
	
		//��� ��� Ž��
		for(i=0; i<fact; i++) {
			temp = pathlist.getLength(0);
			for(j=0; j<pathnum-2; j++) {
				sum = sum + temp[permu[i].get(j)];
				temp = pathlist.getLength(permu[i].get(j));
			}
			sum+= temp[pathnum-1];

			if(min > sum) {
				min = sum;
				path = permu[i];
			}
			sum=0;
		}
		
		System.out.println(min + " km");
		
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
		if (depth == k) { // �ѹ� depth �� k�� �����ϸ� ����Ŭ�� ������. �����. 
			print(arr,k); 
			return; 
			} 
		for (int i = depth; i < n; i++) { 
			swap(arr, i, depth); 
			perm(arr, depth + 1, n, k); 
			swap(arr, i, depth); 
			} 
		} 
	   
	// �ڹٿ����� �����Ͱ� ���� ������ �Ʒ��� ����, �ε��� i�� j�� ���� �ٲ���. 
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
	
	//�������� �ٲ��ֱ�
	public double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	//������ �Ϲ� ������ �ٲ��ֱ�
	public double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}

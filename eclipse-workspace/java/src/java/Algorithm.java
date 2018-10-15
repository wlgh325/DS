public class Algorithm {
	
	private PathList pathlist;
	private boolean[] nearEndpoint;
	public final int nearKm=25;
	private int pathnum;
	final int INF = 100000;
	//0번이 start_destination
	//마지막 index가 final_destination
	
	Algorithm(PathList pathlist){
		this.pathlist = pathlist;
		this.nearEndpoint= new boolean[JsonParser.MAX_CONTENTS];	//default : false
		nearEndpoint();
		this.pathnum = pathlist.getcount();
	}
	
	public void cal_distance(int num) {
		double cal_latitude = Double.parseDouble(pathlist.getDestinationLat(num));
		double cal_longtitude = Double.parseDouble(pathlist.getDestinationLon(num));
		
		pathlist.initialLength(num);
		
		for(int i=num; i<pathnum-1; i++) {
		
			//자기자신은 제외
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
			dist = Math.round(dist * 1.609344 * 1000)/ 1000.0;	//셋째자리 까지 반올림하여 표시
			
			System.out.println(num + " -> " + i + " : " + dist);
			//pathlist.setLength(num, dist);
			pathlist.setLength(num, dist, i);
		}
		
	}
	
	public void optimumRoute() {
		for(int i=0; i<this.pathnum-2; i++) {
			cal_distance(i);

			int min_index = closeDestination(i);
			
			if(min_index == 0)
				break;
			
			pathlist.addDestination(i+2, pathlist.getDestination(min_index));
			pathlist.delete(min_index+2);
			pathlist.printTravelRoute();
			
			
		}
	}
	
	public int closeDestination(int index) {
		double[] temp;
		double min=0;
		int min_index=0;
		
		
		temp = pathlist.getLength(index);
		min = INF;
		
		for(int i=0; i<pathnum -1; i++) {	
			System.out.println("test" + i + ": " + temp[i]);
			
			if(temp[i] != 0 && pathlist.getVisit(i) == false) {
				if(min > temp[i]) {
					min = temp[i];
					min_index= i;
				}
			}
		}
		
		pathlist.setVisit(min_index);
		System.out.println(min_index + ":" + min);
		
		return min_index;
	}
	
	//25km이내를 가까운 거리로 판단
	//end_point 근처를 판단해서 index를 저장해둔다
	//end_point 근처의 destination은 end_point에서 방문한다
	public void nearEndpoint() {
		double final_latitude = Double.parseDouble(pathlist.getDestinationLat(pathlist.getcount()-1));	//도착지 latitude
		double final_longtitude = Double.parseDouble(pathlist.getDestinationLon(pathlist.getcount()-1));	//도착지 longtitude
		
		//출발지, 도착지를 제외한 나머지 Destination에 대해서
		//25km내는 가깝다고 판단!
		for(int i=1; i<pathlist.getcount(); i++) {
			double latitude = Double.parseDouble(pathlist.getDestinationLat(i));
			double longtitude = Double.parseDouble(pathlist.getDestinationLon(i));
			
			double theta = final_longtitude - longtitude;
			double dist = Math.sin(deg2rad(final_latitude)) * Math.sin(deg2rad(latitude)) + 
					Math.cos(deg2rad(final_latitude)) * Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(theta));
			
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			
			//kilometer
			dist = Math.round(dist * 1.609344 * 1000)/ 1000.0;	//셋째자리 까지 반올림하여 표시
			
			if(dist < this.nearKm) 
				this.nearEndpoint[i] = true;
		}
	}
	
	
	//라디안으로 바꿔주기
	public double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	//라디안을 일반 각도로 바꿔주기
	public double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}

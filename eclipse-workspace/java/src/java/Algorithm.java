public class Algorithm {
	
	private PathList pathlist;
	private boolean[] nearEndpoint;
	public final int nearKm=25;
	private int pathnum;
	final int INF = 100000;
	//0���� start_destination
	//������ index�� final_destination
	
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
	
	//25km�̳��� ����� �Ÿ��� �Ǵ�
	//end_point ��ó�� �Ǵ��ؼ� index�� �����صд�
	//end_point ��ó�� destination�� end_point���� �湮�Ѵ�
	public void nearEndpoint() {
		double final_latitude = Double.parseDouble(pathlist.getDestinationLat(pathlist.getcount()-1));	//������ latitude
		double final_longtitude = Double.parseDouble(pathlist.getDestinationLon(pathlist.getcount()-1));	//������ longtitude
		
		//�����, �������� ������ ������ Destination�� ���ؼ�
		//25km���� �����ٰ� �Ǵ�!
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
			dist = Math.round(dist * 1.609344 * 1000)/ 1000.0;	//��°�ڸ� ���� �ݿø��Ͽ� ǥ��
			
			if(dist < this.nearKm) 
				this.nearEndpoint[i] = true;
		}
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

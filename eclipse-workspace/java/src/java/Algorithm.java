import java.util.ArrayList;

public class Algorithm {
	
	private PathList pathlist;
	private boolean[] nearEndpoint;
	public final int nearKm=25;
	private int pathnum;
	final int INF = 100000;
	static int check;
	private ArrayList<Integer>[] permu;
	private int fact;
	
	//0���� start_destination
	//������ index�� final_destination
	
	Algorithm(PathList pathlist){
		this.pathlist = pathlist;
		this.nearEndpoint= new boolean[JsonParser.MAX_CONTENTS];	//default : false
		this.pathnum = pathlist.getcount();
		
		this.fact = fact(pathnum-2);
		permu = new ArrayList[this.fact];
		
		for (int i=0; i < this.fact; i++) {
			permu[i] = new ArrayList<Integer>();
		}

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

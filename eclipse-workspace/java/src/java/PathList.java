public class PathList {
	private Destination head;
	private Destination tail;
	private int count =0;

	private class Destination{
		private String latitude;	//����, 33~43
		private String longtitude;	//�浵,	124~132
		private String destName;	//������ �̸�
		private double[] time;
		
		//���� ��带 ����Ŵ
		private Destination next;	// ���� ������ ����
		
		//Constructor
		Destination(String destName, String latitude, String longtitude){
			this.next = null;
			this.destName = destName;
			this.latitude = latitude;
			this.longtitude = longtitude;
			this.time = new double[JsonParser.MAX_CONTENTS];
		}
		
		//Constructor
		Destination(String destName, String latitude, String longtitude, double[] time){
				this.next = null;
				this.destName = destName;
				this.latitude = latitude;
				this.longtitude = longtitude;
				this.time = time;
			}
	}
	
	//����� ��� �߰�
	public void setStartDestination(String destName, String latitude, String longtitude) {
		Destination firstDestination = new Destination(destName, latitude, longtitude);
		firstDestination.next = head;	//������ ù ��带 ���� 2��° ���� ����
		head = firstDestination;
		count++;
		if(head.next == null){
	        tail = head;
	    }
	}
	
	//������ ��� �߰�
	public void setArrivalDestination(String destName, String latitude, String longtitude) {
		Destination lastDestination = new Destination(destName, latitude, longtitude);
		if(count == 0)
			setStartDestination(destName, latitude, longtitude);
		else {
			tail.next = lastDestination;
			tail = lastDestination;
			count++;	
		}
	}
	
	//Head ����
	public void setHead(Destination head) {
		this.head = head;
	}
	
	//Tail ����
	public void setTail(Destination tail) {
		this.tail = tail;
	}
	
	//������ ����
	Destination createDestination(int number) {
		Destination newDestination = head;
		for(int i=0; i< number; i++)
			newDestination = newDestination.next;
		return newDestination;
	}

	//�������� ���ϴ� ���� �߰�
	public void addDestination(int number, String destName, String latitude, String longtitude) {
		if(number==1)
			setStartDestination(destName, latitude, longtitude);
		else {
			Destination temp1 = createDestination(number-2);
			Destination temp2 = temp1.next;
			
			Destination newDestination = new Destination(destName, latitude, longtitude);
			temp1.next = newDestination;
			newDestination.next = temp2;
			if(temp2 == null)
				tail = newDestination;
			count++;
			
		}
	}
	
	public void addDestination(int number, Destination dest) {
		
		Destination temp1 = createDestination(number-1);
		Destination temp2 = temp1.next;
		
		Destination newDestination = new Destination(dest.destName, dest.latitude, dest.longtitude, dest.time);
		temp1.next = newDestination;
		newDestination.next = temp2;
		if(temp2 == null)
			tail = newDestination;
		count++;
	}
	
	//���� ��� ���
	public void printTravelRoute() {
		if(head == null)
			System.out.println("No Route!!");
		
		Destination dest = head;
		String route = "Route: ";
		
		while(dest.next != null) {
			route += dest.destName + " -> ";
			dest = dest.next;
		}
		route += dest.destName;
		System.out.println(route);
	}
	
	//����� ����
	public Object deleteStartDestination() {
		Destination temp = head;
		head = temp.next;
		
		Object delDestination = temp.destName;
		temp = null;
		count--;
		
		return delDestination;
	}
	
	public Object delete(int number){
	    if(number == 1)
	        return deleteStartDestination();
	    
	    Destination temp = createDestination(number-2);
	    Destination todoDeleted = temp.next;
	    
	    temp.next = temp.next.next;
	    
	    Object deleteData = todoDeleted.destName;
	    if(todoDeleted == tail){
	        tail = temp;
	    }
	    
	    todoDeleted = null; 
	    count--;
	    return deleteData;
	}
	
	//�������� ����
	public int getcount() {
		return count;
	}
	
	public Destination getDestination(int number) {
		Destination dest = createDestination(number);
		return dest;
	}
	//���° �湮�� �������� �������� ���
	public String getDestinationName(int number) {
		//Destination dest = createDestination(number-1);
		Destination dest = createDestination(number);
		return dest.destName;
	}
	
	public String getDestinationLat(int number) {
		Destination dest= createDestination(number);
		return dest.latitude;
	}
	
	public String getDestinationLon(int number) {
		Destination dest= createDestination(number);
		return dest.longtitude;
	}
	
	//�������� �� ��° �湮���� ��ȸ
	public int getDestOrder(String destName) {
		Destination dest = head;
		int order = 1;
		
		while(!dest.destName.equals(destName)) {
			dest = dest.next;
			order++;
			
			//���� ��� �϶�(������ �϶�)
			if(dest == null)
				return -1;
		}
		return order;
	}

	public void setTime(int num, double length, int index) {
		Destination dest = head;
	
		
		for(int i=0; i<num; i++) {
			dest = dest.next;
		}

		//0��°���� ����
		dest.time[index]=length;
		
		//	System.out.println(num + "->" + (index) + ", length: " + dest.time[num].get(index));
	}
	
	public void initialTime(int num) {
		Destination dest = head;
	
		
		for(int i=0; i<num; i++) {
			dest = dest.next;
		}
		
		dest.time = new double[JsonParser.MAX_CONTENTS];
	}
	
	public double[] getTime(int num) {
		Destination dest = head;
		
		for(int i=0; i<num; i++) {
			dest = dest.next;
		}
		
		return dest.time;
	}
}
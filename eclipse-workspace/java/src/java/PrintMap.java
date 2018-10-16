import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrintMap{
	private PathList pathlist;
	
	PrintMap(PathList pathlist){
		this.pathlist = pathlist;
	}
	
	public void writeJSP() throws IOException {
		File file = new File("C:\\Users\\jiho\\Downloads\\apache-tomcat-9.0.11\\webapps\\ROOT\\index.jsp");
		String key = "7e81644e2ebd6dd53fea05d0393f6450";
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("<!DOCTYPE html>\r\n"); 
		writer.write("<html>\r\n");
		writer.write("<head>\r\n");
		writer.write("    <meta charset=\"utf-8\">\r\n");
		writer.write("</head>\r\n");
		writer.write("<body>\r\n");
		writer.write("<div id=\"map\" style=\"width:100%;height:400px;\"></div>    \r\n");
		writer.write("<script type=\"text/javascript\" src=\"//dapi.kakao.com/v2/maps/sdk.js?appkey=");
		writer.write(key + "\"></script>\r\n");
		writer.write("<script>\r\n\r\n");
		
		writeMapContainer(writer, this.pathlist);
		writer.write("var map = new daum.maps.Map(mapContainer, mapOption);\r\n\r\n");
		writeLinePath(writer, this.pathlist);
		writeDrawLine(writer);
		writeMarker(writer, pathlist);
		writer.write("</script>");
		writer.write("</body>");
		writer.write("</html>");
		writer.newLine(); // \r\n 써도 되고 newLine 함수를 사용해 다음라인으로 내려도 된다 writer.write("newLine 사용해서 내림\r\n"); 
		writer.close();
	}
	
	public void writeMapContainer(BufferedWriter writer, PathList pathlist) throws IOException{
		int mapLevel = 9;
		double centerlat;
		double centerlon;
		
		writer.write("var mapContainer = document.getElementById('map'),\r\n");
		writer.write("    mapOption = { \r\n");
		writer.write("        center: new daum.maps.LatLng(");
		
		centerlat = calCenterlat();
		centerlon = calCenterlon();
		
		writer.write(Double.toString(centerlat) + ", " + Double.toString(centerlon) + "),\r\n");
		writer.write("        level: " );
		writer.write(Integer.toString(mapLevel) + "\r\n    };");
		writer.newLine();
		writer.newLine();
	}	
	
	public void writeLinePath(BufferedWriter writer, PathList pathlist) throws IOException{
		int pathnum = pathlist.getcount();
		writer.write("var linePath = [\r\n");
		
		for(int i=0; i<pathnum; i++) {
			writer.write("	new daum.maps.LatLng(");
			writer.write(pathlist.getDestinationLat(i) + ", " + pathlist.getDestinationLon(i));
			
			writer.write(")");
			if(i != (pathnum -1 ))
				writer.write(",");
			writer.write("\r\n");
		}
		writer.write("];\r\n");
		writer.newLine();
		
	}
	
	public void writeDrawLine(BufferedWriter writer) throws IOException{
		
		writer.write("var polyline = new daum.maps.Polyline({\r\n");
		writer.write("	path: linePath,\r\n"); 
	    writer.write("	strokeWeight: 5,\r\n"); 
	    writer.write("	strokeColor: '#FFAE00',\r\n"); 
	    writer.write("	strokeOpacity: 0.7,\r\n"); 
	    writer.write("	strokeStyle: 'solid'\r\n"); 
		writer.write("});\r\n");
		writer.write("\r\npolyline.setMap(map);  \r\n");
	}
	
	public void writeMarker(BufferedWriter writer, PathList pathlist) throws IOException{
		int pathnum = pathlist.getcount();
		String imageURL = "http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
		writer.write("var positions = [	");
		for(int i=0; i < pathnum; i++) {	
			writer.write("\r\n	{\r\n\t	title: '");
			writer.write(Integer.toString(i+1) + pathlist.getDestinationName(i) +"' ,\r\n");
			writer.write("\t\tlatlng: new daum.maps.LatLng(");
			writer.write(pathlist.getDestinationLat(i) + ", " + pathlist.getDestinationLon(i) + ")	}");
			
			if(i != (pathnum -1 ))
				writer.write(",");
		}
		writer.write("\r\n];\r\n");
		writer.newLine();
		writer.write("var imageSrc = " + "\"" + imageURL + "\"" + ";\r\n");
		writer.write("for (var i = 0; i < positions.length; i ++) {\r\n");
		writer.write("	var imageSize = new daum.maps.Size(24, 35); \r\n");
		writer.write("	var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize);\r\n");    
		writer.write("	var marker = new daum.maps.Marker({\r\n");
		writer.write("		map: map,\r\n");
		writer.write("		position: positions[i].latlng,\r\n");
		writer.write("		title : positions[i].title,\r\n");
		writer.write("		image : markerImage"); 
		writer.write("    });\r\n}");
	}
	
	public double calCenterlat() {
		int pathnum = pathlist.getcount();
		double min = 100000.0;
		double max = 0.0;
		double center;
		
		//min값 찾기
		for(int i=0; i<pathnum; i++) {
			double temp = Double.parseDouble(pathlist.getDestinationLat(i));
			
			if( min > temp)
				min = temp;
		}
		
		//max값 찾기
		for(int i=0; i<pathnum; i++) {
			double temp = Double.parseDouble(pathlist.getDestinationLat(i));
			
			if( max < temp)
				max = temp;
		}
		
		center = (min + max) / 2.0;
		return center;
	}
	
	public double calCenterlon() {
		int pathnum = pathlist.getcount();
		double min = 100000.0;
		double max = 0.0;
		double center;
		
		//min값 찾기
		for(int i=0; i<pathnum; i++) {
			double temp = Double.parseDouble(pathlist.getDestinationLon(i));
			
			if( min > temp)
				min = temp;
			
		}
		
		//max값 찾기
		for(int i=0; i<pathnum; i++) {
			double temp = Double.parseDouble(pathlist.getDestinationLon(i));
			
			if( max < temp)
				max = temp;
			
		}
		
		center = (min + max) / 2.0;
		return center;
	}
}
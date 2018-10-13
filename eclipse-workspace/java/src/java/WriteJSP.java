package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main{
	
	public static void main(String[] args) throws IOException{
		PathList pathlist = new PathList(33.450701, 126.570667 );
		writeJSP(pathlist);

	}
	
	public static void writeJSP(PathList pathlist) throws IOException {
		File file = new File("C:\\Users\\Guest1\\Downloads\\index.jsp");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
		writer.write("<!DOCTYPE html>\r\n"); 
		writer.write("<html>\r\n");
		writer.write("<head>\r\n");
		writer.write("    <meta charset=\"utf-8\">\r\n");
		writer.write("</head>\r\n");
		writer.write("<body>\r\n");
		writer.write("<div id=\"map\" style=\"width:100%;height:350px;\"></div>    \r\n");
		writer.write("<script type=\"text/javascript\" src=\"//dapi.kakao.com/v2/maps/sdk.js?appkey=발급받은 APP KEY를 사용하세요\"></script>\r\n");
		writer.write("<script>\r\n\r\n");
		
		writeMapContainer(writer, pathlist);
		writer.write("var map = new daum.maps.Map(mapContainer, mapOption);\r\n\r\n");
		writeLinePath(writer, pathlist);
		writeDrawLine(writer);
		writeMarker(writer, pathlist);
		writer.write("</script>");
		writer.write("</body>");
		writer.write("</html>");
		writer.newLine(); // \r\n 써도 되고 newLine 함수를 사용해 다음라인으로 내려도 된다 writer.write("newLine 사용해서 내림\r\n"); 
		writer.close();
	}
	
	public static void writeMapContainer(BufferedWriter writer, PathList pathlist) throws IOException{
		int mapLevel = 3;
		
		writer.write("var mapContainer = document.getElementById('map'),\r\n");
		writer.write("    mapOption = { \r\n");
		writer.write("        center: new daum.maps.LatLng(");
		writer.write(Double.toString(pathlist.latitude) + ", " + Double.toString(pathlist.longtitude) + "),\r\n");
		writer.write("        level: " );
		writer.write(Integer.toString(mapLevel) + "\r\n    };");
		writer.newLine();
		writer.newLine();
	}	
	
	public static void writeLinePath(BufferedWriter writer, PathList pathlist) throws IOException{
		int pathnum=5;
		writer.write("var linePath = [\r\n");
		
		for(int i=0; i<pathnum; i++) {
			writer.write("	new daum.maps.LatLng(");
			writer.write(Double.toString(pathlist.latitude) + ", " + Double.toString(pathlist.longtitude));
			
			writer.write(")");
			if(i != (pathnum -1 ))
				writer.write(",");
			writer.write("\r\n");
		}
		writer.write("];\r\n");
		writer.newLine();
		
	}
	
	public static void writeDrawLine(BufferedWriter writer) throws IOException{
		
		writer.write("var polyline = new daum.maps.Polyline({\r\n");
		writer.write("	path: linePath,\r\n"); 
	    writer.write("	strokeWeight: 5,\r\n"); 
	    writer.write("	strokeColor: '#FFAE00',\r\n"); 
	    writer.write("	strokeOpacity: 0.7,\r\n"); 
	    writer.write("	strokeStyle: 'solid'\r\n"); 
		writer.write("});\r\n");
		writer.write("\r\npolyline.setMap(map);  \r\n");
	}
	
	public static void writeMarker(BufferedWriter writer, PathList pathlist) throws IOException{
		int pathnum = 4;
		String imageURL = "http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
		writer.write("var positions = [	");
		for(int i=0; i < pathnum; i++) {	
			writer.write("\r\n	{\r\n\t	title: '");
			writer.write(pathlist.name[i] + "' ,\r\n");
			writer.write("\t\tlatlng: new daum.maps.LatLng(");
			writer.write(pathlist.lat[i] + ", " + pathlist.log[i] + ")	}");
			
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
}

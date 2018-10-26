package lzhao;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;

import redis.clients.jedis.GeoCoordinate;

public class Finder {
	public void find() {
		
		try {
			Instant start = Instant.now();
			String filePath = getFilePath();
			System.out.println(filePath);
			File file = new File(filePath);
			if(file.exists()){
				FileInputStream in = new FileInputStream(file);
				CsvReader csv = new CsvReader(in, Charset.forName("UTF-8"));
				if (csv.readHeaders()) {
					//String[] headers = csv.getHeaders();
					int colLat = -1, colLon = -1;
					for (int i = 0; i < csv.getHeaderCount(); i++) {
						
						if ("Longitude".equals(csv.getHeader(i))) {
							colLon = i;
							
						}
						if ("Latitude".equals(csv.getHeader(i))) {
							colLat = i;
							
						}
					}
					System.out.printf("Column %d for lat, %d for lon\n", colLat, colLon);
					
					Map<String, GeoCoordinate> map = new HashMap<String, GeoCoordinate>();
					//List<HashMap<String, GeoCoordinate>> mapList = new LinkedList<HashMap<String, GeoCoordinate>>();
					int i = 0, sub_i = 0;
					int batchSize = 1000;//5000;//10000;//100000;
					while (csv.readRecord()) {
						GeoCoordinate coord = new GeoCoordinate(
								Double.valueOf(csv.get(colLon)),
								Double.valueOf(csv.get(colLat)));
						map.put(String.valueOf(i), coord);
						i++;sub_i++;
						if (sub_i >= batchSize) {
							break;
						}
					}
					Instant endFile = Instant.now();
					System.out.println(String.valueOf(start.until(endFile, ChronoUnit.MILLIS)));
					
					int notFound = 0;
					Instant beginDb = Instant.now();
					JedisUtils.add(map);
					for (Map.Entry<String, GeoCoordinate> entry : map.entrySet()) {
						
					String found = JedisUtils.find(entry.getValue().getLongitude(), entry.getValue().getLatitude());
					if (null == found || found.isEmpty()) {
						notFound++;
						
					}
					}
					//add the odds
					if (!map.isEmpty()) {
					JedisUtils.add(map);
					}
					
					Instant endDb = Instant.now();
					System.out.println(String.valueOf(beginDb.until(endDb, ChronoUnit.MILLIS)));
					System.out.printf("%d not found\n", notFound);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static String getFilePath() {
		String fileName = "foo2.csv";
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		if (path.contains("WEB-INF")) {// for Tomcat environment
			path = path.replace("file:", "");
			path += fileName;
		} else {// for Eclipse environment
			path = "src/main/resources/" + fileName;
		}
		return path;
	}

}

package lzhao;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.csvreader.CsvReader;

import redis.clients.jedis.GeoCoordinate;

public class Loader {
	// Define the logger object for this class
	//protected final Logger log = LoggerFactory.getLogger(Loader.class);

	public void load() {
		
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
						
						if ("SITE_LONGITUDE".equals(csv.getHeader(i))) {
							colLon = i;
							
						}
						if ("SITE_LATITUDE".equals(csv.getHeader(i))) {
							colLat = i;
							
						}
					}
					System.out.printf("Column %d for lat, %d for lon\n", colLat, colLon);
					
					HashMap<String, GeoCoordinate> map = new HashMap<String, GeoCoordinate>();
					List<HashMap<String, GeoCoordinate>> mapList = new LinkedList<HashMap<String, GeoCoordinate>>();
					int i = 0, sub_i = 0;
					int batchSize = 10000;
					while (csv.readRecord()) {
						GeoCoordinate coord = new GeoCoordinate(
								Double.valueOf(csv.get(colLon)),
								Double.valueOf(csv.get(colLat)));
						map.put(String.valueOf(i), coord);
						i++;sub_i++;
						if (sub_i >= batchSize) {
							mapList.add(map);
							map = new HashMap<String, GeoCoordinate>();
							sub_i = 0;
						}
					}
					Instant endFile = Instant.now();
					System.out.println(String.valueOf(start.until(endFile, ChronoUnit.MILLIS)));
					
					JedisUtils.add(map);
					for (HashMap<String, GeoCoordinate> map1 : mapList) {
					JedisUtils.add(map1);
					}
					//add the odds
					if (!map.isEmpty()) {
					JedisUtils.add(map);
					}
					
					Instant endDb = Instant.now();
					System.out.println(String.valueOf(endFile.until(endDb, ChronoUnit.MILLIS)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static String getFilePath() {
		String fileName = "foo.csv";
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

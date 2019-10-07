package lzhao;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import redis.clients.jedis.GeoCoordinate;
import com.google.common.base.Strings;

public class Loader {
	static final String lonColName = "SITE_LONGITUDE";
	static final String latColName = "SITE_LATITUDE";
	static final String enbColName = "enodeB_ID";
	static final String cellColName = "CELL_ID";
	static final int batchSize = 10000;


	
	// Define the logger object for this class
	protected final Logger log = LoggerFactory.getLogger(Loader.class);
	

	public void load(String filePath) {
		
		try {
			Instant start = Instant.now();
			if (Strings.isNullOrEmpty(filePath)) {
				filePath = getFilePath();
			}
			log.info("loading {}", filePath);
			File file = new File(filePath);
			if(file.exists()){
				FileInputStream in = new FileInputStream(file);
				CsvReader csv = new CsvReader(in, Charset.forName("UTF-8"));
				if (csv.readHeaders()) {
					//String[] headers = csv.getHeaders();
					int colLat = -1, colLon = -1, colEnb = -1, colCell = -1;
					for (int i = 0; i < csv.getHeaderCount(); i++) {
						
						if (lonColName.equals(csv.getHeader(i))) {
							colLon = i;
						}
						if (latColName.equals(csv.getHeader(i))) {
							colLat = i;
						}
						if (enbColName.equals(csv.getHeader(i))) {
							colEnb = i;
							
						}
						if (cellColName.equals(csv.getHeader(i))) {
							colCell = i;
							
						}
					}
					System.out.printf("Column %d for lat, %d for lon, %d for eNB ID, %d for Cell ID\n", 
							colLat, colLon, colEnb, colCell);
					
					HashMap<String, GeoCoordinate> map = new HashMap<String, GeoCoordinate>();
					List<HashMap<String, GeoCoordinate>> mapList = new LinkedList<HashMap<String, GeoCoordinate>>();
					int i = 0, sub_i = 0, n = 0;
					while (csv.readRecord()) {
						GeoCoordinate coord = new GeoCoordinate(
								Double.valueOf(csv.get(colLon)),
								Double.valueOf(csv.get(colLat)));
						map.put(csv.get(colEnb)+":"+csv.get(colCell), coord);
						i++;sub_i++;
						if (sub_i >= batchSize) {
							mapList.add(map);
							map = new HashMap<String, GeoCoordinate>();
							sub_i = 0;
							n++;
						}
					}
					//add the odds
					if (!map.isEmpty()) {
						mapList.add(map);
					}
					Instant endFile = Instant.now();
					System.out.printf("parsing file: %d entries in %d maps in %s ms\n",
							i, n+1,
							String.valueOf(start.until(endFile, ChronoUnit.MILLIS)));
					
					for (HashMap<String, GeoCoordinate> map1 : mapList) {
						JedisUtils.add(map1);
					}
					
					Instant endDb = Instant.now();
					System.out.println("adding to Redis: "+String.valueOf(endFile.until(endDb, ChronoUnit.MILLIS))+" ms");
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

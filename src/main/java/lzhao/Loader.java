package lzhao;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
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
					
					Map<String, GeoCoordinate> map = new HashMap<String, GeoCoordinate>();
					int i = 0;
					Instant start = Instant.now();
					while (csv.readRecord()) {
						GeoCoordinate coord = new GeoCoordinate(
								Double.valueOf(csv.get(colLon)),
								Double.valueOf(csv.get(colLat)));
						map.put(csv.get(colEnb)+":"+csv.get(colCell), coord);
						i++;
					}
					Instant endFile = Instant.now();
					System.out.printf("parsing file: %d entries, storing %d in map, in %s ms\n",
							i, map.size(),
							String.valueOf(start.until(endFile, ChronoUnit.MILLIS)));
					
					// split the map into smaller ones for geoadd
					Map<String, GeoCoordinate> map1 = new HashMap<String, GeoCoordinate>();
					i = 0;
					int n = 0;
					Instant startDb = Instant.now();

					for (Map.Entry<String, GeoCoordinate> entry : map.entrySet()) {
						map1.put(entry.getKey(), entry.getValue());
						i++;
						if (i >= batchSize) {
							JedisUtils.add(map1);
							i = 0;
							map1.clear();
							n++;
						}
					}
					if (!map1.isEmpty()) {
						JedisUtils.add(map1);
						n++;
					}
					
					Instant endDb = Instant.now();
					System.out.printf("adding %d maps to Redis: in %s ms\n",
							n,
							String.valueOf(startDb.until(endDb, ChronoUnit.MILLIS))+" ms");
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

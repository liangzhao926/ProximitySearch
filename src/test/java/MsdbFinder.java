import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import city.City;
import lzhao.JedisUtils;
import redis.clients.jedis.GeoCoordinate;

public class MsdbFinder {

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		 
		 
        /**
         * Read JSON from a file into a Map
         */
        try {
        	File file = new File("src/test/resources/1000cities.json");
        	if (file.exists()) {
        		City[] cities = mapper.readValue(file, City[].class);
				Instant beginDb = Instant.now();
				int numFound = 0, numNotFound = 0;
        		for (City city : cities) {
        			Double lat = city.getFields().getCoordinates().get(0);
        			Double lon = city.getFields().getCoordinates().get(1);
					List<String> found = JedisUtils.find(lon, lat, 15);
					if (null == found || found.isEmpty()) {
						numNotFound++;

					} else {
						numFound++;
					}
				}
        		Instant endDb = Instant.now();
        		System.out.println(String.valueOf(beginDb.until(endDb, ChronoUnit.MILLIS))+"ms");
				System.out.printf("%d found, %d not found\n", numFound, numNotFound);
        	}
 
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}

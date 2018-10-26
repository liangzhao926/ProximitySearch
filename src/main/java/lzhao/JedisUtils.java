package lzhao;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

public class JedisUtils {
	static final String key = "MSDB";
	static Jedis jedis = new Jedis();
	
	public static void add(Map<String, GeoCoordinate> memberCoordinateMap) {
		jedis.geoadd(key, memberCoordinateMap);
	}
	
	public static String find(double longitude, double latitude) {
		int i = 1;
		for (i = 1; i <1000; i*=10) {
			List<GeoRadiusResponse> rspList = jedis.georadius(key, longitude, latitude, i, 
					GeoUnit.MI, 
					GeoRadiusParam.geoRadiusParam().sortAscending());
			if (rspList != null & !rspList.isEmpty()) {
				return rspList.get(0).getMemberByString();
				
			}
		}
		return null;
	}

}

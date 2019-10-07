import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lzhao.Loader;

public class SimRedis {
	static Logger log = LoggerFactory.getLogger(SimRedis.class);	
	
	@BeforeClass
	public static void setup() {
		// TODO Auto-generated method stub
		Loader loader = new Loader();
		try {
			loader.load("../../samples/sprint-femto/LTE_MSDB_05042019.csv");
		} catch (Exception e) {
			log.error("failed to load", e);
			
		}
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

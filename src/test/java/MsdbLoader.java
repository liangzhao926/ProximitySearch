import lzhao.Loader;


public class MsdbLoader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Loader loader = new Loader();
		try {
			loader.load("../../samples/sprint-femto/LTE_MSDB_05042019.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

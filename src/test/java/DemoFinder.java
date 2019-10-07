import lzhao.Finder;


public class DemoFinder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Finder finder = new Finder();
		try {
			finder.find("../../samples/sprint-femto/LTE_MSDB_05042019.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

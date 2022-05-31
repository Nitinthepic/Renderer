public class Main {
	public static void main(String[] args) {
		try {
			boolean vSync = true;
			ILogic gameLogic = new PlaceHolderGame();
			Engine gameEng = new Engine("GAME", 2560, 980, vSync, gameLogic);
			gameEng.run();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}
}

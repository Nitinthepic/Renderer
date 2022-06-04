public interface IHUD {
	GameObj[] getGameItems();

	default void cleanup() {
		GameObj[] gameObjs = getGameItems();
		for (GameObj obj : gameObjs) {
			obj.getMesh().cleanUp();
		}
	}

	void setStatusText(String statusText);
}

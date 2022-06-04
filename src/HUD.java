import org.joml.Vector4f;

import java.io.IOException;

public class HUD implements IHUD {
	private static final int FONT_COLS = 16;

	private static final int FONT_ROWS = 16;

	private static final String FONT_TEXTURE = "/textures/font_texture.png";

	private final GameObj[] gameItems;

	private final TextItem statusTextItem;

	public HUD(String statusText) throws IOException {
		this.statusTextItem = new TextItem(statusText, FONT_TEXTURE, FONT_COLS,
				FONT_ROWS);
		this.statusTextItem.getMesh().getMaterial().setAmbientColor(new Vector4f(1, 1, 1, 1));
		gameItems = new GameObj[]{statusTextItem};

	}

	@Override
	public GameObj[] getGameItems() {
		return gameItems;
	}

	public void updateSize(Window window) {
		this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
	}

	@Override
	public void cleanup() {
		IHUD.super.cleanup();
	}

	public void setStatusText(String statusText) {
		this.statusTextItem.setText(statusText);
	}
}

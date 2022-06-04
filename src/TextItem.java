import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextItem extends GameObj {
	private static final float ZPOSITION = 0f;
	private static final int VERTICESPERQUAD = 4;
	private final int numColumns;
	private final int numRows;
	private String text;

	public TextItem(String text, String fontFile, int numCols, int numRows) throws IOException {
		super();
		this.text = text;
		this.numColumns = numCols;
		this.numRows = numRows;
		Texture texture = new Texture(fontFile);
		this.setMesh(buildMesh(texture, numCols, numRows));
	}

	private Mesh buildMesh(Texture texture, int columns, int rows) {
		byte[] chars = text.getBytes(StandardCharsets.ISO_8859_1);
		int numberOfChars = chars.length;

		List<Float> positions = new ArrayList<>();
		List<Float> textCoordinates = new ArrayList<>();
		float[] normals = new float[0];
		List<Integer> indices = new ArrayList<>();

		float tileWidth = (float) texture.getWidth() / (float) columns;
		float tileHeight = (float) texture.getHeight() / (float) rows;

		for (int n = 0; n < numberOfChars; n++) {
			byte currChar = chars[n];
			int col = currChar % columns;
			int row = currChar / columns;

			//Top Left vertex
			positions.add((float) n * tileWidth);
			positions.add(0f);
			positions.add(ZPOSITION);
			textCoordinates.add((float) col / (float) columns);
			textCoordinates.add((float) row / (float) rows);
			indices.add(n * VERTICESPERQUAD);

			//Bottom Left vertex
			positions.add((float) n * tileWidth);
			positions.add(tileHeight);
			positions.add(ZPOSITION);
			textCoordinates.add((float) col / (float) columns);
			textCoordinates.add((float) (row + 1) / (float) rows);
			indices.add(n * VERTICESPERQUAD + 1);

			positions.add((float) n * tileWidth + tileWidth);
			positions.add(tileHeight);
			positions.add(ZPOSITION);
			textCoordinates.add((float) (col + 1) / (float) columns);
			textCoordinates.add((float) (row + 1) / (float) rows);
			indices.add(n * VERTICESPERQUAD + 2);

			positions.add((float) n * tileWidth + tileWidth);
			positions.add(0f);
			positions.add(ZPOSITION);
			textCoordinates.add((float) (col + 1) / (float) columns);
			textCoordinates.add((float) row / (float) rows);
			indices.add(n * VERTICESPERQUAD + 3);

			indices.add(n * VERTICESPERQUAD);
			indices.add((n * VERTICESPERQUAD + 2));
		}
		float[] positionArray = Utils.listToArray(positions);
		float[] textCoordinatesArray = Utils.listToArray(textCoordinates);
		int[] indicesArray =
				indices.stream().mapToInt((i) -> i).toArray();
		Mesh mesh = new Mesh(positionArray, textCoordinatesArray, normals,
				indicesArray);
		mesh.setMaterial(new Material(texture));
		return mesh;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		Texture texture = this.getMesh().getMaterial().getTexture();
		this.getMesh().deleteBuffers();
		this.setMesh(buildMesh(texture, numColumns, numRows));
	}
}

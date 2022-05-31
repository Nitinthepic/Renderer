import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class OBJLoader {
	public static Mesh loadMesh(String filePath) throws Exception {
		List<String> lines = Utils.readAllLines(filePath);
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();

		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch ((tokens[0])) {
				case "v":
					Vector3f vector3f =
							new Vector3f(Float.parseFloat(tokens[1]),
									Float.parseFloat(tokens[2]),
									Float.parseFloat(tokens[3]));
					vertices.add(vector3f);
					break;
				case "vt":
					Vector2f vector2f =
							new Vector2f(Float.parseFloat(tokens[1]),
									Float.parseFloat(tokens[2]));
					textures.add(vector2f);
					break;
				case "vn":
					Vector3f vector3f1 =
							new Vector3f(Float.parseFloat(tokens[1]),
									Float.parseFloat(tokens[2]),
									Float.parseFloat(tokens[3]));
					normals.add(vector3f1);
					break;
				case "f":
					Face face = new Face(tokens[1], tokens[2], tokens[3]);
					faces.add(face);
					break;
				default:
					// Ignore other lines
					break;
			}
		}
		return reorderLists(vertices,textures,normals,faces);
	}

	private static Mesh reorderLists(List<Vector3f> positionList,
									 List<Vector2f> textCoordinatesList,
									 List<Vector3f> normalList,
									 List<Face> faceList) {
		List<Integer> indices = new ArrayList<>();
		float[] posArray = new float[positionList.size()*3];
		int i = 0;
		for (Vector3f pos: positionList) {
			posArray[i*3] = pos.x;
			posArray[i * 3 + 1] = pos.y;
			posArray[i * 3 + 2] = pos.z;
			i++;
		}
		float[] textCoordArr = new float[positionList.size() * 2];
		float[] normArr = new float[positionList.size() * 3];

		for (Face face : faceList) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IdxGroup indValue : faceVertexIndices) {
				processFaceVertex(indValue, textCoordinatesList, normalList,
						indices, textCoordArr, normArr);
			}
		}
		int[] indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		Mesh mesh = new Mesh(posArray, textCoordArr, normArr, indicesArr);
		return mesh;
	}

	private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList,
										  List<Vector3f> normList, List<Integer> indicesList,
										  float[] texCoordArr, float[] normArr) {

		// Set index for vertex coordinates
		int posIndex = indices.idxPos;
		indicesList.add(posIndex);

		// Reorder texture coordinates
		if (indices.idxTextureCoordinate >= 0) {
			Vector2f textCoord = textCoordList.get(indices.idxTextureCoordinate);
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
		if (indices.idxVectorNormal >= 0) {
			// Reorder vectornormals
			Vector3f vecNorm = normList.get(indices.idxVectorNormal);
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}

	protected static class Face {
		private IdxGroup[] idxGroups;

		public Face(String v1, String v2, String v3) {
			idxGroups = new IdxGroup[3];
			idxGroups[0] = parseLine(v1);
			idxGroups[1] = parseLine(v2);
			idxGroups[2] = parseLine(v3);
		}

		private IdxGroup parseLine(String line) {
			IdxGroup idxGroup = new IdxGroup();
			String[] lineTokens = line.split("/");
			int length = lineTokens.length;
			idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
			if (length > 1) {
				// It can be empty if the obj does not define text coords
				String textCoord = lineTokens[1];
				idxGroup.idxTextureCoordinate = textCoord.length() > 0 ?
						Integer.parseInt(textCoord) - 1 : IdxGroup.NOVALUE;
				if (length > 2) {
					idxGroup.idxVectorNormal = Integer.parseInt(lineTokens[2]) - 1;
				}
			}

			return idxGroup;
		}

		public IdxGroup[] getFaceVertexIndices() {
			return idxGroups;
		}
	}

	protected static class IdxGroup {
		public static final int NOVALUE = -1;
		public int idxPos;
		public int idxTextureCoordinate;
		public int idxVectorNormal;

		public IdxGroup() {
			idxPos = NOVALUE;
			idxTextureCoordinate = NOVALUE;
			idxVectorNormal = NOVALUE;
		}
	}
}

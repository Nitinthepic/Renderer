import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

	public static String loadResource(String fileName) throws Exception {
		String result;
		try (InputStream in = Utils.class.getResourceAsStream(fileName);
			 Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
			result = scanner.useDelimiter("\\A").next();
		}
		return result;
	}

	public static List<String> readAllLines(String filePath) throws Exception {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br =
					 new BufferedReader(new InputStreamReader(Class
							 .forName(Utils.class.getName())
							 .getResourceAsStream(filePath)))) {
			String line;
			while((line=br.readLine())!=null){
				lines.add(line);
			}
		}
		return lines;
	}

}
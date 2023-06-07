package de.zeroco.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class FileOperations {

	public static final String EXTENSION = ".ZC_LIC";
	public static final String FILE_PATH = "/home/hari/files/JSONfile.json";
	public static final String REPLACED_VALUE = "$no$";

	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		System.out.println(getValueByKey("/home/hari/files/FirstJSONFile.json", "second"));
//		System.out.println(createFile("/home/hari/files/FirstJSONFile.json"));
//		System.out.println(generateLisence("Hari",1, 10,"/home/hari/files/"));
//		System.out.println(getValueOfNestedJson("{\"first\":{\"second\":{\"third\":{\"fourth\":5}}}}""first.second.third.fourth"));
//		System.out.println(createFile("/home/hari/files/serilizationFile.txt"));
//		System.out.println(serielzationOfData("/home/hari/files/serilizationFile.txt"));
		System.out.println(deserilizationOfData("/home/hari/files/serilizationFile.txt"));
	}

	public static String createDirectory(String path) {
		File folder = new File(path);
		return (folder.exists() || folder.isHidden()) ? path : (folder.mkdir() ? path : null);
	}

	public static String createFile(String path) throws IOException {
		File file = new File(path);
		String result = "";
		if (file.exists() || file.isHidden()) {
			result = path;
		} else {
			file.createNewFile();
			result = "new File is created";
		}
		return result;
	}

	public static String getFileNames(String path) {
		File file = new File(path);
		return Arrays.toString(file.listFiles());
	}

	public static String searchFile(String directoryPath, String fileName) {
		String resultData = "";
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					String result = searchFile(file.getAbsolutePath(), fileName);
					resultData = resultData + result;
				} else if (file.getName().equals(fileName)) {
					resultData = file.getAbsolutePath();
				}
			}
		}
		return resultData;
	}

	public static boolean usingFileOutPut(String path, String data) throws IOException {
		File file = new File(path);
		FileOutputStream fileStream = new FileOutputStream(file);
		byte bit[] = data.getBytes();
		fileStream.write(bit);
		fileStream.close();
		if (file.length() == 0) {
			return false;
		}
		return true;
	}

	public static boolean usingFilewriter(String path, String data) throws IOException {
		File file = new File(path);
		FileWriter writer = new FileWriter(file);
		writer.write(data);
		writer.close();
		if (file.length() == 0) {
			return false;
		}
		return true;
	}

	public static boolean usingBufferWriter(String path, String data, String word) throws IOException {
		File file = new File(path);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
		bufferedWriter.write(data);
		bufferedWriter.newLine();
		bufferedWriter.write(word);
		bufferedWriter.close();
		if (file.length() == 0) {
			return false;
		}
		return true;
	}

	public static String usingFileInput(String path) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(path);
		int i = 0;
		String result = "";
		while ((i = fileInputStream.read()) != -1) {
			result = result + (char) i;
		}
		fileInputStream.close();
		return result;
	}

	public static String readingFileData(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String data = "";
		String result = "";
		while ((data = reader.readLine()) != null) {
			result += data + "\n";
		}
		reader.close();
		return result;
	}

	public static String writeDataToFile(String path, String data) {
		try {
			File file = new File(path);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(data);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String insertToFile(String path, String data) {
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			String holder = readingFileData(path);
			holder = holder + data;
			writeDataToFile(path, holder);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return "Action Performed";
	}

	public static String lineInsertion(String filePath, int lineToReplace, String newLine) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		if (lineToReplace >= 1 && lineToReplace <= lines.size()) {
			lines.add(lineToReplace - 1, newLine);
		} else {
			lines.add(newLine);
		}
		Files.write(Paths.get(filePath), lines);
		return "Action Performed";
	}

	public static String insertAdditionalTextToLine(String filePath, int lineNumber, String input,
			String additionalText) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		int position;
		String originalLine = "";
		if (lineNumber >= 1 && lineNumber <= lines.size()) {
			originalLine = lines.get(lineNumber - 1);
			StringBuffer modifiedLine = new StringBuffer(originalLine);
			position = originalLine.indexOf(input);
			if (position != -1) {
				modifiedLine.insert(position + input.length(), additionalText);
				lines.set(lineNumber - 1, modifiedLine.toString());
			}
		} else {
			return "Invalid line number or  Line does not exist.";
		}
		Files.write(Paths.get(filePath), lines);
		return "Action Performed";
	}

	public static String serielzationOfData(String path) throws IOException {
		Serilization serilization = new Serilization("Hari", 4627);
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
		outputStream.writeObject(serilization);
		outputStream.close();
		return path;
	}

	public static String deserilizationOfData(String path)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(path));
		Serilization serilized = (Serilization) objectIn.readObject();
		objectIn.close();
		return serilized.name + " " + serilized.id;
	}

	public static String generateLisence(String client, int from, int to, String path) throws IOException {
		if (path.isEmpty()) {
			path = System.getProperty("java. io. tmpdir");
		}
		String data = readingFileData(FILE_PATH);
		for (int i = from; i <= to; i++) {
			String clientName = client + String.format("%02d", i);
			String fileName = path + clientName + EXTENSION;
			createFile(fileName);
			String replacedValue = data.replace(REPLACED_VALUE, clientName);
			writeDataToFile(fileName, replacedValue);
		}
		return path;
	}

	public static String getValueOfNestedJson(String jsonString, String key) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			String[] keys = key.split("\\.");
			for (String nestedKey : keys) {
				if (jsonObject.has(nestedKey)) {
					Object value = jsonObject.get(nestedKey);
					if (value instanceof JSONObject) {
						jsonObject = (JSONObject) value;
					} else {
						return value.toString();
					}
				} else {
					return null;
				}
			}
			return null;
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

}

package de.zeroco.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileOperations {

	public static String EXTENSION = ".zc_lic";
	public static String FILE_PATH = "/home/hari/files/JSONfile.json";
	public static String REPLACE_VALUE = "$no$";

	public static void main(String[] args) throws IOException {
//       System.out.println(createFile("/home/hari/files/JSONfile.json"));
//		System.out.println(readingFileData("/home/hari/files/JSONfile.json"));
		System.out.println(generateLisence("hari", 36, 45,""));
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

	public static String generateLisence(String client, int from, int to, String path) throws IOException {
		String clientName = "";
		String fileName = "";
		String replacedValue = "";
		if(path.isEmpty()) {
			String tmpdir = System.getProperty("java.io.tmpdir");
			path=tmpdir;
		}
		String readerFile = readingFileData(FILE_PATH);
		for (int i = from; i <= to; i++) {
			clientName = client + String.format("%02d", i);
			fileName = path + clientName + EXTENSION;
			File file = new File(fileName);
			file.createNewFile();
			replacedValue = readerFile.replace(REPLACE_VALUE, clientName);
			writeDataToFile(fileName, replacedValue);
		}
		return path;
	}

}

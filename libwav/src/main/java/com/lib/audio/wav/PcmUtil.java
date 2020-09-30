package com.lib.audio.wav;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 说明:
 *
 * @author wangshengxing  02.21 2020
 */
public class PcmUtil {

	public static boolean toWav(String inputPath) {
		return toWav(inputPath, inputPath);
	}

	public static boolean toWav(String inputPath, String outputPath) {
		return toWav(inputPath, outputPath, getDefaultHeader());
	}

	public static boolean toWav(String inputPath, WavHeader header) {
		return toWav(inputPath, inputPath, header);
	}

	public static boolean toWav(String inputPath, String outputPath, WavHeader header) {
		boolean ret = false;
		File readFile = new File(inputPath);
		if ((!readFile.exists()) || outputPath == null || outputPath.length() <= 0) {
			return ret;
		}
		boolean sameFile = inputPath.equals(outputPath);
		WavFileWriter writer = new WavFileWriter();
		File tmpWriteFile = new File(sameFile ? (outputPath + ".tmp") : outputPath);
		try {
			writer.openFile(tmpWriteFile.getAbsolutePath(), header.getSampleRate(), header.getNumChannel(), header.getBitsPerSample());
			InputStream reader = new FileInputStream(readFile);
			byte[] buffer = new byte[2048];
			int readSize = -1;
			while (((readSize = reader.read(buffer, 0, buffer.length)) == buffer.length)) {
				writer.writeData(buffer, 0, readSize);
			}
			if (readSize > 0) {
				writer.writeData(buffer, 0, readSize);
			}
			if (writer != null) {
				writer.closeFile();
			}
			if (reader != null) {
				reader.close();
			}
			if (sameFile) {
				if (readFile.delete()) {
					tmpWriteFile.renameTo(readFile);
				}
			}
			ret = true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return ret;
	}

	private static WavHeader getDefaultHeader() {
		return new WavHeader(16000, 1, 16);
	}
}

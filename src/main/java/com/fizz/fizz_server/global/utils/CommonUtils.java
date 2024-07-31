package com.fizz.fizz_server.global.utils;

public class CommonUtils {
	private static final String FILE_EXTENSION_SEPARATOR = ".";

	public static String buildFileName(String originalFileName) {
		int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		String fileExtension = originalFileName.substring(fileExtensionIndex);
		String fileName = originalFileName.substring(0, fileExtensionIndex);

		return fileName + fileExtension;
	}
}

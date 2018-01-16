package com.newexcavator.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;

/**
 * Class for handling media files in a filesystem.
 * 
 */
public class FileUtils {

	/**
	 * Retrieves the filename separated by name and extension.
	 * 
	 * @param file
	 *            File to be used.
	 * @return A pair consisting of name and extension of the passed file.
	 */
	public String[] getFileNameAndExtension(File file) {
		return getFileNameAndExtension(file.getName());
	}

	/**
	 * Retrieves the filename separated by name and extension.
	 * 
	 * @param filename
	 *            File to be used.
	 * @return A pair consisting of name and extension of the passed file.
	 */
	public String[] getFileNameAndExtension(String filename) {
		String name = filename.substring(0, filename.lastIndexOf("."));
		String extension = filename.substring(filename.lastIndexOf("."));
		return new String[] { name, extension };
	}

	/**
	 * Retrieves the filename separated by path, name and extension.
	 * 
	 * @param filename
	 *            File to be used.
	 * @return A set consisting of path, name and extension of the passed file.
	 */
	public String[] getFullFileNameAndExtension(String filename) {
		String path = new File(filename).getParentFile().getPath() + File.separator;
		String[] fullname = getFileNameAndExtension(new File(filename).getName());

		String name = fullname[0];
		String extension = fullname[1];

		return new String[] { path, name, extension };
	}

	/**
	 * Simple method for saving a file.
	 * 
	 * @param file
	 *            File to be saved.
	 * @param input
	 *            Data to be saved.
	 * @throws IOException
	 *             if any error arises.
	 */
	public void saveFile(File file, InputStream input) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		BufferedInputStream bis = new BufferedInputStream(input);
		int aByte;
		while ((aByte = bis.read()) != -1) {
			bos.write(aByte);
		}
		bos.flush();
		bos.close();
		bis.close();
		input.close();
	}

	/**
	 * Simple method for saving a file.
	 * 
	 * @param fileName
	 *            Name of the file to be saved.
	 * @param input
	 *            Data to be saved.
	 * @throws IOException
	 *             if any error arises.
	 */
	public void saveFile(String fileName, InputStream input) throws IOException {
		saveFile(new File(fileName), input);
	}

	/**
	 * Deletes a file
	 * 
	 * @param filename
	 *            Name of the file to be deleted
	 */
	public void deleteFile(String filename) {
		deleteFile(new File(filename));
	}

	/**
	 * Deletes a file
	 * 
	 * @param fileToDelete
	 *            File to be deleted
	 */
	public void deleteFile(File fileToDelete) {
		if (fileToDelete.exists()) {
			fileToDelete.delete();
		}
	}

	/**
	 * Creates a .lock file with the usedId written on it.
	 * 
	 * @param lockFileName
	 *            Name of the lock file.
	 * @param userId
	 *            User Id that is locking.
	 * @throws IOException
	 */
	public void createLock(String lockFileName, Long userId) throws IOException {
		File file = new File(lockFileName);
		org.apache.commons.io.FileUtils.writeStringToFile(file, userId.toString());
	}

	/**
	 * Reads the userId from the passed .lock file.
	 * 
	 * @param lockFileName
	 *            Name of the lock file to be red.
	 * @return userId
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public Long readLockFile(String lockFileName) throws IOException {
		Long userId = 0L;

		org.apache.commons.io.FileUtils utils = new org.apache.commons.io.FileUtils();
		String line = utils.readFileToString(new File(lockFileName));

		userId = Long.parseLong(line.trim());
		return userId;
	}

	public static boolean printFile(String path, String name, String ext, String type) {
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(MediaSizeName.ISO_A4);
		pras.add(new Copies(1));

		DocFlavor flavor = null;
		if (ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".jpeg"))
			flavor = DocFlavor.INPUT_STREAM.JPEG;
		else if (ext.equalsIgnoreCase(".png"))
			flavor = DocFlavor.INPUT_STREAM.PNG;
		else if (ext.equalsIgnoreCase(".gif"))
			flavor = DocFlavor.INPUT_STREAM.GIF;

		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		if (defaultService != null) {
			try {
				DocPrintJob job = defaultService.createPrintJob(); // 创建打印作业
				StringBuffer absolutePath = new StringBuffer();
				absolutePath.append(path);
				absolutePath.append("\\");
				absolutePath.append(name);
				absolutePath.append(type);
				absolutePath.append(ext);

				Doc doc = new InputStreamDoc(absolutePath.toString(), flavor);
				job.print(doc, pras); // 进行文件的打印
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}

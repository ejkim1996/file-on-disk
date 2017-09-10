/**
 * Program that takes in 2 arguments, the first the path name of
 * the directory/file, and the second the sorting category.
 * the program then prints up to 20 files in the folder that are
 * newest, oldest, or largest.
 * @author EJ (Eui Joon) Kim
 */

package project2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DirectoryInfo {
	//create static ArrayList of FileOnDisk objects
	static ArrayList<FileOnDisk> files = new ArrayList<>();

	public static void main(String[] args) {
		//print error and exit if there is less than 2 arguments
		if (args.length < 2) {
			System.err.println("You must have more than two arguments.");
			System.exit(1);
		}
		
		//print error and exit if the file/directory does not exist
		File directory = new File(args[0]);
		if (!directory.exists()) {
			System.err.println("File/directory does not exist.");
			System.exit(1);
		}
		
		//print error and exit if the second argument is not largest, newest, or oldest.
		if (!(args[1].equalsIgnoreCase("largest") || args[1].equalsIgnoreCase("newest") || args[1].equalsIgnoreCase("oldest"))) {
			System.err.println("Sorting category " + args[1] + " not supported.");
			System.exit(1);
		}
		
		//if there are no errors, call the recursive getSize method for the File object
		int size = getSize(directory);
		
		//print the search information and the 20 or less files that match the sort category
		printSearchInfo(size, args[1]);
		printFileInfo(args[1]);
	}
	
	/**
	 * Recursive function to get the total size of all the file(s)
	 * passed in as a parameter.
	 * @param dir the File object to find size for.
	 * @return size of the directory
	 */
	static int getSize(File dir) {
		FileOnDisk file = new FileOnDisk(dir.getAbsolutePath());
		int totalSize = 0;
		
		//if the FileOnDisk object is a symbolic link, add its size
		//to totalSize and return it if it's a directory. If it is a file,
		//return totalSize
		try {
			if (!file.getAbsolutePath().equalsIgnoreCase(file.getCanonicalPath())) {
				if (file.isDirectory()) {
					totalSize += file.length();
					return totalSize;
				}
				if (file.isFile()) {
					return totalSize;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//if the parameter is a directory add its size to totalSize and 
		//recursively call getSize() to all the files within the directory.
		if (file.isDirectory()) {
			totalSize += file.length();
			File[] allFiles = file.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				totalSize += getSize(allFiles[i]);
			}
		}
		
		//if the parameter is a file, add it to the ArrayList and add its size to totalSize
		else {
			files.add(file);
			totalSize += file.length();
		}
			
		return totalSize;
		
	}
	
	/**
	 * Print the total space used and total number of files
	 * in the directory provided in the command line argument.
	 * Also prints the sort category that was specified.
	 * @param size the total size of the directory/file
	 * @param category String of the sort category
	 */
	public static void printSearchInfo(int size, String category) {
		//use FileOnDisk's formattedSize() method to convert units and
		//format size. Then remove padding.
		String spaceUsed = FileOnDisk.formattedSize(size);
		spaceUsed = spaceUsed.replaceAll(" ", "");
		
		//print space used and number of files
		System.out.print("Total space used: " + spaceUsed + ",");
		System.out.println(" total number of files: " + files.size());
		
		//print description of order file information will be printed
		if (category.equalsIgnoreCase("largest")) {
			System.out.println("Largest 20 files:");
		}
		if (category.equalsIgnoreCase("oldest")) {
			System.out.println("Least recently modified 20 files:");
		}
		if (category.equalsIgnoreCase("newest")) {
			System.out.println("Most recently modified 20 files:");
		}
	}
	
	/**
	 * Print up to 20 toString() representations of 
	 * FileOnDisk objects based on their sort category
	 * @param category String of the sort category
	 */
	public static void printFileInfo(String category) {
		if (category.equalsIgnoreCase("largest")) {
			Collections.sort(files, new CompareFilesBySize());
			int numOfResults = 20;
			
			if (files.size() < 20) {
				numOfResults = files.size();
			}
			
			for (int i = 0; i < numOfResults; i++) {
				System.out.println(files.get(i).toString());
			}
		}
		
		if (category.equalsIgnoreCase("oldest")) {
			Collections.sort(files, new CompareFilesByDate());
			int numOfResults = 20;
			
			if (files.size() < 20) {
				numOfResults = files.size();
			}
			
			for (int i = 0; i < numOfResults; i++) {
				System.out.println(files.get(i).toString());			}
		}
		
		if (category.equalsIgnoreCase("newest")) {
			Collections.sort(files, new CompareFilesByDate());
			int numOfResults = 20;
			
			if (files.size() < 20) {
				numOfResults = files.size();
			}
			
			for (int i = files.size() - 1; i > files.size() - (numOfResults + 1); i--) {
				System.out.println(files.get(i).toString());
			}
		}
	}

}



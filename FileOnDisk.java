/**
 * Class extends the file class and has a method to convert and
 * format the size into a String, a method to format the date of
 * the last modified date, and a toString() method that
 * prints out the size, last modified date, and path name of the file.
 * @author EJ (Eui Joon) Kim
 */


package project2;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class FileOnDisk extends File {
	//data fields for size and last modified date
	private long size;
	private long lastModified;

	
	/**
	 * Constructor that takes a String and saves values from the length() and
	 * lastModified() method to the size and lastModified variables.
	 * @param pathname a String variable that will be the parameter for the superclass.
	 */
	public FileOnDisk(String pathname) {
		super(pathname);
		size = this.length();
		lastModified = this.lastModified();
	}
	
	/**
	 * format the size of the file with appropriate padding and units
	 * @param size the value to convert and format into a String
	 * @return a String that is 
	 */
	public static String formattedSize(long size) {
		//initialize variables
		double sizeToFormat = (double) size;
		double conversion = 1024;
		String[] units = new String[4];
		units[0] = "bytes";
		units[1] = "KB";
		units[2] = "MB";
		units[3] = "GB";
		int numOfConversions = 0;
		
		//if the size has more than 3 digits before the decimal point, convert units
		for (int i = 0; i < 4; i++) {
			if (sizeToFormat > 1000) {
				sizeToFormat = sizeToFormat/conversion;
				numOfConversions++;
			}
			//stop converting units when size has less than 3 digits before decimal point
			else {
				break;
			}
		}
		
		//take the converted size and format it so it has 2 digits after the decimal point
		String formattedSize = String.format("%1$.2f", sizeToFormat);
		//take the converted size and pad it on the left with spaces
		formattedSize = String.format("%1$6s", formattedSize);
		//take the converted size and pad it on the right with spaces
		String unit = String.format("%1$-8s", units[numOfConversions]);
		
		return formattedSize + " " + unit;
	}
	
	/**
	 * format the last modified date and return it as a String.
	 * @return the formatted last modified date as a String
	 */
	public String formattedDate() {
		//format the last modified date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date date = new Date(lastModified);
		String date2 = sdf.format(date);
		
		return date2;
	}
	
	/**
	 * Overrides the toString() method to return a String that is formatted
	 * and has information of size, last modified date, and absolute path
	 */
	@Override
	public String toString() {
		return formattedSize(size) + formattedDate() + "   " + getAbsolutePath();
	}

	
}

class CompareFilesByDate implements Comparator<FileOnDisk> { 
	@Override
	public int compare(FileOnDisk o1, FileOnDisk o2) {
		long comp = o1.lastModified() - o2.lastModified() ; 
		if (comp == 0 ) {
			return o1.compareTo(o2); 
		}
		if (comp > 0 ) return 1;
		else return -1; 
	}
}

class CompareFilesBySize implements Comparator<FileOnDisk> { 
	@Override
	public int compare(FileOnDisk o1, FileOnDisk o2) { 
		int comp = (int) ( o2.length() - o1.length() ); 
		if (comp == 0 ) {
			return o2.compareTo(o1); 
		}
		return comp;
	} 
}


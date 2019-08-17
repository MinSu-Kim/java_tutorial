package java_tutorial.file_upload;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JFileChooser;

import org.apache.tika.Tika;

public class MediaUtils {
	
	private static Map<String, String> mediaMap;
	
	static {
		mediaMap = new HashMap<String, String>();
		mediaMap.put("JPG", "jpeg");
		mediaMap.put("GIF", "gif");
		mediaMap.put("PNG", "png");
	}
	
	public static String getMediaType(String type) {
		return mediaMap.get(type.toUpperCase());
	}
	
	public static boolean checkImageType(String fileName) {
		return fileName.contains("jpg") || fileName.contains("gif") || fileName.contains("png") || fileName.contains("jpeg") ;
	}
	
	public static void main(String[] args) throws IOException {
		
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		String mimeType = mimeTypesMap.getContentType(getFilePath().toString());
		
	    System.out.println(mimeType);

	    File file = getFilePath();

	    mimeType = new Tika().detect(file);

	    System.out.println(mimeType);
	    
	}
	
	public static File getFilePath() throws IOException {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		
		int returnValue = fileChooser.showOpenDialog(null);
	
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}

	
}

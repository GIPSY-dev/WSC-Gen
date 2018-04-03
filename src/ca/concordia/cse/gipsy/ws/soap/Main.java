package ca.concordia.cse.gipsy.ws.soap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;

public class Main {

	public static void main(String[] args) throws Exception {
            SoapClient client = new SoapClient();

		Generator g = new Generator();
                
                g.setDefault();
                
                if (g.start()) {
                    System.out.println("Generator start method sucessful.");
                    System.out.println(g.getFile(g.getBpelFileName()).getAbsolutePath().toString());
                } else {
                    System.out.println("Error in generator start method.");
                }
		
//		// need first an output folder.
//		g.browseOutputFolder();
//		
//
//		// ------------Example----------------------------
//		// --------- if files already exist in folder --------
//		// user input a name for bpelFileName
//		String bpelFileName = "bpelFileName";
//		if(g.containsFile(g.getOutputFolder(), bpelFileName) && !g.getOverrideFiles()){
//			// if this check is not done, an exception is thrown.
//			// you can either handle the exception in the controller or output a response here
//			System.out.println("folder already exist...");
//		}
//
//		
//		g.setDefault();
//
//		// start to generate files and save them in folder.
//		g.start();
//
//		if(g.getErrorMessages() != null){
//				System.out.println(g.getErrorMessages());
//					}
		
	}

        private static File saveToFile(byte[] bytes) throws IOException {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream os = null;
                
                try {
                    os = new FileOutputStream(fileChooser.getSelectedFile().toPath().toString());
                    os.write(bytes);                   
                } catch (Exception ex) {
                    System.out.println("Exception when writing the file to disk. Error " + ex.getMessage());
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }

            return null;
        }
}
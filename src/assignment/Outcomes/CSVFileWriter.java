/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.Outcomes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prithpal Sooriya
 */
public class CSVFileWriter {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private String dir;
    private String fileName;
    private String fileHeader;
    private FileWriter fw;
    
    
    public CSVFileWriter(String fileDir,String fileName, String fileHeader) {
        this.dir = fileDir;
        this.fileName = fileName;
        this.fileHeader = fileHeader;
        createFile();
        writeHeader();
    }
    
    private void createFile() {
        try {
            File file = new File(dir, fileName);
            if(!file.exists()) {
                file.createNewFile();
            }
            else {
                file.delete();
                file.createNewFile();
            }
            fw = new FileWriter(file);
        } catch (IOException ex) {
            System.out.println("Could not create file");
            Logger.getLogger(CSVFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeHeader() {
        try {
            fw.append(fileHeader);
            fw.append(NEW_LINE_SEPARATOR);
        } catch (IOException ex) {
            System.out.println("Could not write to file");
            Logger.getLogger(CSVFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writePopulation(String line) {
        try {
            fw.append(line);
            fw.append(NEW_LINE_SEPARATOR);
        } catch (IOException ex) {
            System.out.println("could not write to file");
            Logger.getLogger(CSVFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        try {
            fw.close();
        } catch (IOException ex) {
            System.out.println("could not close");
            Logger.getLogger(CSVFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

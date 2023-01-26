package services;

import java.nio.file.Paths;

import com.google.common.io.Files;

import constant.MainConstant;
import exception.MergerException;

public class FileReaderFactory {

    public static FileService getFileService(String fileName) throws MergerException {

        //Check provided files are in data directory
        if(!java.nio.file.Files.exists(Paths.get(MainConstant.FILE_DIR_PATH+fileName))) {
            throw new MergerException("File not found::"+fileName);
        }
        
        String ext = Files.getFileExtension(fileName);
        //Need to add new else if condition for new File type 
        if(MainConstant.FILE_TYPE_CSV.equalsIgnoreCase(ext)) {
            return new CSVFileServiceImpl(fileName);
        } else if (MainConstant.FILE_TYPE_HTML.equalsIgnoreCase(ext)) {
            return new HtmlFileServiceImpl(fileName);
        } else {
            throw new MergerException("Unsupported file type ::"+fileName);
        }
    }
    
}

package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import exception.MergerException;

public class MergerService {

    private String outFileName;

    private String[] fileNames;

    public MergerService(String[] fileNames, String outFileName){
        this.fileNames = fileNames;
        this.outFileName = outFileName;
    }

    public void mergeFiles() throws MergerException{
        // Initilize the all variables
		Map<String, Integer> finalHeaders = new HashMap<>();
		Map<String, List<String> >fileHeaders = new HashMap<>();
		List<FileService> fileServices = new ArrayList<>();
		TreeMap<Integer, String[]> records = new TreeMap<>();
		CommonService common = new CommonServiceImpl();

		//Combined headers
  		for(String fileName : this.fileNames) {
            //Get Instance based on file type
			FileService fileService=  FileReaderFactory.getFileService(fileName);
			List<String> headersList = fileService.getHearders();
			fileHeaders.put(fileName, headersList);
			common.updateHeaderMap(finalHeaders, headersList);
			fileServices.add(fileService);
		}

		//Read the file and added to records map
		for(FileService fileService : fileServices) {
			fileService.readRecords(records, finalHeaders);
		}

		//Generate final csv based on merged result
		common.writeFinalCSV(records, finalHeaders, false, this.outFileName);
    }
    
}

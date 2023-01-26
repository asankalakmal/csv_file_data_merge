package services;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.jsoup.helper.StringUtil;

import au.com.bytecode.opencsv.CSVWriter;
import constant.MainConstant;
import exception.MergerException;

public class CommonServiceImpl implements CommonService {

    @Override
    public void writeFinalCSV(TreeMap<Integer, String[]> data, Map<String, Integer> finalHeaders, boolean append, String finalFileName) throws MergerException {
        try {
            FileWriter fileWriter = new FileWriter(MainConstant.FILE_DIR_PATH+finalFileName, append);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            List<String[]> list = new ArrayList<>();
            list.add(finalHeaders.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(x -> x.getKey()).collect(Collectors.toList()).toArray(String[]::new));
            list.addAll(data.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList()));
            csvWriter.writeAll(list);
            csvWriter.close();
        } catch (Exception e) {
            //System.err.println("Exception happen while writing to CSV file::"+e);
            throw new MergerException("Error while Writing to final CSV file ::"+finalFileName);
        }
        
    }

    @Override
    public void updateHeaderMap(Map<String, Integer> headersMap , List<String> headers) {

		if(headersMap.size() == 0) {
			headersMap.put(MainConstant.UNIQUE_COL, 0);
		} 

		for(String header: headers) {
			if(MainConstant.UNIQUE_COL.equalsIgnoreCase(header)) {
				continue;
			} 
			if(headersMap.get(header) == null) {
				headersMap.put(header, headersMap.size());
			}
		}
	}

    public static Map<Integer, Integer> getHeaderMapping(Map<String, Integer> headers, String[] fileHeaders) {

        Map<Integer, Integer> map = new HashMap<>();

        for(int i =0; i<fileHeaders.length; i++) {
            if(headers.get(fileHeaders[i]) != null) {
                map.put(i, headers.get(fileHeaders[i]));
            }
        }

        return map;
    }
    
    public static int getIDColIndex(String[] fileHeaders) {
        int index = -1;
        for (int i=0; i<fileHeaders.length; i++) {
            if (MainConstant.UNIQUE_COL.equalsIgnoreCase(fileHeaders[i])) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static String[] recordsUpdate(String[] oldData, String[] newData) {
        
        for(int i=0; i<newData.length; i++) {
            if(!StringUtil.isBlank(newData[i])) {
                oldData[i] = newData[i];
            }
        }

        return oldData;
    }

}

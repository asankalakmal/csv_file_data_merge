package services;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import exception.MergerException;

public interface CommonService {
    
    public void writeFinalCSV(TreeMap<Integer, String[]> data, Map<String, Integer> finalHeaders, boolean append, String finalFileName) throws MergerException;

    public  void updateHeaderMap(Map<String, Integer> headersMap , List<String> headers);

}

package services;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import exception.MergerException;

public interface FileService {

    public List<String> getHearders() throws MergerException;

    public void readRecords(TreeMap<Integer, String[]> records, Map<String, Integer> headers) throws MergerException;
    
}

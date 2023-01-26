package services;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVReader;
import constant.MainConstant;
import exception.MergerException;

public class CSVFileServiceImpl implements FileService {

    private String[] fileHeaders = null;

    private String fileName;

    public CSVFileServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> getHearders() throws MergerException {

        try (CSVReader csvReader=new CSVReader(
            new InputStreamReader(new FileInputStream(MainConstant.FILE_DIR_PATH+this.fileName), "UTF-8"));) {

            this.fileHeaders = csvReader.readNext();
            return Arrays.stream(this.fileHeaders).collect(Collectors.toList());

        } catch (Exception e) {
            throw new MergerException("Invalid CSV File!");
        }
     
    }

    @Override
    public void readRecords(TreeMap<Integer, String[]> records, Map<String, Integer> headers) throws MergerException {

        try (CSVReader csvReader=new CSVReader(
            new InputStreamReader(new FileInputStream(MainConstant.FILE_DIR_PATH+this.fileName), "UTF-8"));) {
 
            String[] nextRecord;
            //Skip 1st line (heades line)
            csvReader.readNext();
            Map<Integer, Integer> headerMap = CommonServiceImpl.getHeaderMapping(headers,this.fileHeaders);
            int idColIndex = CommonServiceImpl.getIDColIndex(this.fileHeaders);

            while ((nextRecord = csvReader.readNext()) != null) {
                //Create new Array size same as headers count
                String[] recordArray = new String[headers.size()];
                for (int j=0; j < headerMap.size(); j++) {
                    recordArray[headerMap.get(j)] = nextRecord[j];
                }

                //Check record already in the map
                if(records.get(Integer.parseInt(nextRecord[idColIndex])) != null) {
                    String[] oldRecords =  records.get(Integer.parseInt(nextRecord[idColIndex]));
                    String[] updatedRecords = CommonServiceImpl.recordsUpdate(oldRecords, recordArray);
                    records.put(Integer.parseInt(nextRecord[idColIndex]), updatedRecords);
                } else {
                    records.put(Integer.parseInt(nextRecord[idColIndex]), recordArray);
                }
                
            }

        } catch (Exception e) {
            //System.err.println("Exception happen while CSV file reading!!");
            throw new MergerException("Invalid CSV File contents ::"+this.fileName);
        }
        
    }

}

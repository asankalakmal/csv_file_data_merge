package services;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import constant.MainConstant;
import exception.MergerException;

public class HtmlFileServiceImpl implements FileService {

    private String[] fileHeaders = null;
    private String fileName;

    public HtmlFileServiceImpl(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public List<String> getHearders() throws MergerException {
        //Load the html file  and find the  "directory" table
        Document doc = Jsoup.parse(loadHTML());
        Element table = doc.select("table[id=directory]").first();

        //Check table is exist in html file
        if (table == null){
            throw new MergerException("Invalid CSV File contents ::"+this.fileName);
        }

        //Load the header items in table
        Elements elts = table.select("th");
        String[] headerArray = new String[elts.size()];
        int i =0;
        for(Element elt : elts) {
            headerArray[i]=elt.text();
            i++;
        }

        this.fileHeaders=headerArray;
        return Arrays.stream(this.fileHeaders).collect(Collectors.toList());

    }

    private String loadHTML() throws MergerException {
        
        try {
            //Return the html file as string
            return Jsoup.parse(new File(MainConstant.FILE_DIR_PATH+this.fileName), MainConstant.CHAR_SET).toString();
           
        } catch (IOException e) {
            throw new MergerException("Invalid HTML File ::"+this.fileName);
        } catch(Exception ex) {
            throw new MergerException("Invalid HTML File ::"+this.fileName);
        }
        
    }

    @Override
    public void readRecords(TreeMap<Integer, String[]> records, Map<String, Integer> headers) throws MergerException {
        Document doc = Jsoup.parse(loadHTML());
        Element table = doc.select("table[id=directory]").first();

        Elements rows = table.select("tr");
        Map<Integer, Integer> headerMap = CommonServiceImpl.getHeaderMapping(headers, this.fileHeaders);
        int idColIndex = CommonServiceImpl.getIDColIndex(this.fileHeaders);

        try {

            for (Element row : rows) {
                Elements tds = row.select("td");
                // Check colum side (for heder row tds is zero)
                if(tds.size() == 0 ) {
                    continue;
                } else if(tds.size() != this.fileHeaders.length) {
                    throw new MergerException("Invalid HTML Table structure ::"+this.fileName);
                }
                
                String[] recordArray = new String[headers.size()];
                String[] nextRecord = new String[tds.size()];
    
                //Load the data from table record to array
                int i = 0;
                for (Element td : tds) {
                    nextRecord[i] = td.text();
                    i++;
                }
    
                //Add values to specific position in final array
                for (int j=0; j < headerMap.size(); j++) {
                    recordArray[headerMap.get(j)] = nextRecord[j];
                }
    
                //Check record already in the map, if exists then update it based on new colum valus
                if(records.get(Integer.parseInt(nextRecord[idColIndex])) != null) {
                    String[] oldRecords =  records.get(Integer.parseInt(nextRecord[idColIndex]));
                    String[] updatedRecords = CommonServiceImpl.recordsUpdate(oldRecords, recordArray);
                    records.put(Integer.parseInt(nextRecord[idColIndex]), updatedRecords);
                } else {
                    records.put(Integer.parseInt(nextRecord[idColIndex]), recordArray);
                }
            }

        } catch(Exception ex) {
            throw new MergerException("Invalid HTML File contents ::"+this.fileName);
        }
        
        
    }

}

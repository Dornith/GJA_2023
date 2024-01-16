package vut.fit.gja2023.app.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.util.CsvReaderConfig;
import vut.fit.gja2023.app.validator.CsvValidator;


/**
 * A service for reading csv files.
 */
@Service
@RequiredArgsConstructor
public class CsvReaderService {
    
    private final CsvValidator validator;

    /**
     * Reads a csv file and returns entries read.
     * 
     * @param filePath The path of the csv file.
     * @param config Reader configuration @see CsvReaderConfig.
     * @return A list of lines from the file split by a specified separator.
     * @throws IOException If no file on the specified path exists.
     * @throws IllegalArgumentException If values in the config are invalid or null.
     * @throws IndexOutOfBoundsException If values in the column order from config contain indexes for which there are no columns in the csv file.
     * @throws CsvException If there is a problem while reading the file.
     */
    public List<String[]> readCsv(String filePath, CsvReaderConfig config) throws IOException, IllegalArgumentException, IndexOutOfBoundsException, CsvException {
        return readCsv(new FileInputStream(filePath), config);
    }

    public List<String[]> readCsv(InputStream stream, CsvReaderConfig config) throws IOException, IllegalArgumentException, IndexOutOfBoundsException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(config.getSeparator()).build();
        
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(stream))
                .withCSVParser(parser)
                .build()) {
            
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) return rows;
            int[] columnOrder = config.getColumnOrder();
            
            validator.validate(rows.getFirst(), columnOrder);
            

            if (config.isHavingHeader()) {
                rows.removeFirst();
            }

            // Create a list to store the extracted data
            List<String[]> dataList = new ArrayList<>();
            for (String[] row : rows) {
                
                String[] data = new String[columnOrder.length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = row[columnOrder[i]].trim();
                }
                dataList.add(data);
            }

            return dataList;
        }
    }
}

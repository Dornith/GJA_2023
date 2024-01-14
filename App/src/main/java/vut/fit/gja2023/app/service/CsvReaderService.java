package vut.fit.gja2023.app.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.app.util.CsvReaderConfig;
import vut.fit.gja2023.app.validator.CsvValidator;

@Service
@RequiredArgsConstructor
public class CsvReaderService {
    
    private final CsvValidator validator;
    
    public List<String[]> readCsv(String filePath, CsvReaderConfig config) throws IOException, IllegalArgumentException, IndexOutOfBoundsException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(config.getSeparator()).build();
        
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
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

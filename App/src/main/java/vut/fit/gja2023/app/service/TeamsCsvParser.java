package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vut.fit.gja2023.app.util.CsvReaderConfig;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamsCsvParser {
    
    private final CsvReaderService reader;
    
    public Map<String, List<String>> parse(MultipartFile file, CsvReaderConfig config) {
        try {
            List<String[]> rows = reader.readCsv(file.getInputStream(), config);
            
            return rows.stream().collect(Collectors.groupingBy(
                        entry -> entry[0],
                        Collectors.mapping(entry -> entry[1], Collectors.toList())));
            
        } catch (Exception ex) {
            log.error("Error occurred when parsing CSV.", ex);
            return Map.of();
        }
    }
}

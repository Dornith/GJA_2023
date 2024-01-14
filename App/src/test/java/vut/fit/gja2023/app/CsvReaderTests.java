package vut.fit.gja2023.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vut.fit.gja2023.app.service.CsvReaderService;
import vut.fit.gja2023.app.util.CsvReaderConfig;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import vut.fit.gja2023.app.validator.CsvValidator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvReaderTests {
    
    private static final String TEST_FILE_ONE = "test.csv";
    private static final String TEST_FILE_TWO = "test2.csv";

    private static final List<String[]> TEAMS_RESULT = Arrays.asList(
            new String[]{"team1", "xhonza00"},
            new String[]{"team2", "xjenda00"},
            new String[]{"team1", "xpepik00"},
            new String[]{"team3", "xrozar00"},
            new String[]{"team2", "xjosef00"},
            new String[]{"team3", "xpolet00"});

    private static final List<String[]> TEAMS_RESULT_REVERSE = Arrays.asList(
            new String[]{"xhonza00", "team1"},
            new String[]{"xjenda00", "team2"},
            new String[]{"xpepik00", "team1"},
            new String[]{"xrozar00", "team3"},
            new String[]{"xjosef00", "team2"},
            new String[]{"xpolet00", "team3"});


    private String filePath1;
    private String filePath2;

    private CsvReaderService csvReaderService;
    
    @BeforeAll
    void setup() {
        filePath1 = this.getClass().getClassLoader().getResource(TEST_FILE_ONE).getPath();
        filePath2 = this.getClass().getClassLoader().getResource(TEST_FILE_TWO).getPath();
        
        csvReaderService = new CsvReaderService(new CsvValidator());
    }

    @Test
    void testReadExistingFile() {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{0, 1}, true);
        
        assertDoesNotThrow(() -> {
            csvReaderService.readCsv(filePath1, config);
        });
    }
    
    @Test
    void testReadNonexistentFile() {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{0, 1}, true);
        
        assertThrows(IOException.class, () -> {
            csvReaderService.readCsv("nonexistent/file/path", config);
        });
    }
    
    @Test
    void testOrderMaxOutOfBounds() {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{0, 2}, true);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            csvReaderService.readCsv(filePath1, config);
        });
    }
    
    @Test
    void testOrderMinOutOfBounds() {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{1, -1}, true);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            csvReaderService.readCsv(filePath1, config);
        });
    }
    
    @Test
    void testOrderTooManyColumnsSpecified() {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{1, 0, 1}, true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            csvReaderService.readCsv(filePath1, config);
        });
    }
    
    @Test
    void testReadExistingFileAndCheckResult() throws Exception {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{0, 1}, true);
        
        List<String[]> res = csvReaderService.readCsv(filePath1, config);

        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < TEAMS_RESULT.getFirst().length; j++) {
                assertEquals(TEAMS_RESULT.get(i)[j], res.get(i)[j]);
            }
        }
    }
    
    @Test
    void testReadExistingFileAndCheckResultNoHeader() throws Exception {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{0, 1}, false);
        
        List<String[]> res = csvReaderService.readCsv(filePath2, config);

        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < TEAMS_RESULT.getFirst().length; j++) {
                assertEquals(TEAMS_RESULT.get(i)[j], res.get(i)[j]);
            }
        }
    }
    
    @Test
    void testReadExistingFileAndCheckResultReverseOrder() throws Exception {
        CsvReaderConfig config = new CsvReaderConfig(new int[]{1, 0}, true);
        
        List<String[]> res = csvReaderService.readCsv(filePath1, config);

        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < TEAMS_RESULT_REVERSE.getFirst().length; j++) {
                assertEquals(TEAMS_RESULT_REVERSE.get(i)[j], res.get(i)[j]);
            }
        }
    }
}

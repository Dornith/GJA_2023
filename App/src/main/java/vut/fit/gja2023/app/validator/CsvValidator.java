package vut.fit.gja2023.app.validator;

import java.util.Arrays;
import org.springframework.stereotype.Component;

/**
 * Used for validating provided column order from \ref %CsvReaderConfig with the file being read.
 */
@Component
public class CsvValidator {
    
    /**
     * Checks whether the specified column order can be used with columns from the file being read.
     * 
     * @param row A row split into column values by a separator.
     * @param columnOrder Column order from a csv reader configurator.
     */
    public void validate(String[] row, int[] columnOrder) {
        if (row == null || columnOrder == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        
        int rowElementCount = row.length;
        if (columnOrder.length > rowElementCount) {
            throw new IllegalArgumentException("More columns specified than how many exists in the csv");
        }
        if (Arrays.stream(columnOrder).max().getAsInt() > rowElementCount - 1) {
            throw new IndexOutOfBoundsException("Column index in column order larger than maximal column index");
        }
        if (Arrays.stream(columnOrder).min().getAsInt() < 0) {
            throw new IndexOutOfBoundsException("Column index in column order less than 0");
        }
    }
}

package vut.fit.gja2023.app.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A class for configuring \ref %CsvReaderService.
 */
@Data
@RequiredArgsConstructor
public class CsvReaderConfig {
    private static final char DEFAULT_SEPARATOR = ',';
    
    private final int[] columnOrder;
    private final boolean havingHeader;
    private final char separator;
    
    public CsvReaderConfig(int[] columnOrder, boolean havingHeader) {
        this.columnOrder = columnOrder;
        this.havingHeader = havingHeader;
        this.separator = DEFAULT_SEPARATOR;
    }
}

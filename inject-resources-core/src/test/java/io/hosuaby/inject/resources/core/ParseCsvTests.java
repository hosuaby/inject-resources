package io.hosuaby.inject.resources.core;

import static io.hosuaby.inject.resources.core.InjectResources.resource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;

import de.siegmar.fastcsv.reader.CsvReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParseCsvTests {
    private static final String PATH_PREFIX = "/io/hosuaby";

    @Test
    @DisplayName("Parse CSV with Commons Csv")
    public void testParseWithCommonsCsv() {

        /* Given */
        var csvParser = CSVFormat.EXCEL.withHeader();

        var csvRecords = new ArrayList<CSVRecord>();

        /* When */
        try (var resource = resource()
                .onClassLoaderOf(ParseCsvTests.class)
                .withPath(PATH_PREFIX, "cities.csv")
                .asReader();
             var reader = resource.reader();
             var rows = csvParser.parse(reader)) {

            for (CSVRecord record : rows) {
                csvRecords.add(record);
            }
        } catch (IOException ioException) {
            fail(ioException.getMessage());
        }

        /* Then */
        Assertions.assertThat(csvRecords)
                .isNotEmpty()
                .hasSize(128);
    }

    @Test
    @DisplayName("Parse CSV with FastCsv")
    public void testParseWithFastCsv() {

        /* Given */
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        int rowCount = 0;

        /* When */
        try (var resource = resource()
                .onClassLoaderOf(ParseCsvTests.class)
                .withPath(PATH_PREFIX, "cities.csv")
                .asReader();
             var reader = resource.reader();
             var csv = csvReader.parse(reader)) {
            while (csv.nextRow() != null) {
                rowCount++;
            }
        } catch (IOException ioException) {
            fail(ioException.getMessage());
        }

        /* Then */
        assertThat(rowCount)
                .isEqualTo(128);
    }
}

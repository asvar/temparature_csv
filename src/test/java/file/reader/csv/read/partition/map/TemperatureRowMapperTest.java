package file.reader.csv.read.partition.map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TemperatureRowMapperTest {

    @Test
    void shouldMapRow(){
        String row = "Warszawa;2023-06-08 05:33:08.412;-1.69";
        TemperatureRowMapper rowMapper = new TemperatureRowMapper();

        TemperatureFileRow result = rowMapper.map(row);

        assertThat(result).isEqualTo(new TemperatureFileRow("Warszawa", 2023, -169));
    }
}
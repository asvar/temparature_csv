package file.reader.csv.read.partition.aggregate;

import file.reader.csv.read.YearAggregate;
import file.reader.csv.read.partition.map.TemperatureRowMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AggregationTaskTest {

    @Test
    void shouldAggregateLines() throws Exception {
        List<String> lines = List.of(
                "Warszawa;2023-06-08 05:33:08.412;-1.69",
                "Warszawa;2023-06-09 20:49:50.609;18.21",
                "Kraków;2018-09-19 09:44:47.464;37.21"
        );
        AggregationTask aggregationTask = new AggregationTask(new TemperatureRowMapper(), lines);

        PartitionAggregator result = aggregationTask.call();

        assertThat(result.getCities())
                .hasSize(2)
                .containsKey("Warszawa")
                .containsKey("Kraków");
        assertThat(result.getCities().get("Warszawa").getYears())
                .hasSize(1)
                .containsEntry(2023, new YearAggregate(2023, 1652, 2));
        assertThat(result.getCities().get("Kraków").getYears())
                .hasSize(1)
                .containsEntry(2018, new YearAggregate(2018, 3721, 1));
    }
}
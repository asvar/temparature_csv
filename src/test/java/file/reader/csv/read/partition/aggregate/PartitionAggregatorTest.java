package file.reader.csv.read.partition.aggregate;

import file.reader.csv.read.YearAggregate;
import file.reader.csv.read.partition.map.TemperatureFileRow;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PartitionAggregatorTest {

    @Test
    void shouldCollectMultipleRecords() {
        TemperatureFileRow row1 = new TemperatureFileRow("Warszawa", 2023, 1500);
        TemperatureFileRow row2 = new TemperatureFileRow("Warszawa", 2023, 1220);
        TemperatureFileRow row3 = new TemperatureFileRow("Warszawa", 2023, 1140);
        TemperatureFileRow row4 = new TemperatureFileRow("Warszawa", 2023, 1240);

        PartitionAggregator aggregator = new PartitionAggregator();

        aggregator.collect(row1);
        aggregator.collect(row2);
        aggregator.collect(row3);
        aggregator.collect(row4);


        assertThat(aggregator.getCities())
                .hasSize(1)
                .containsKey("Warszawa");
        assertThat(aggregator.getCities().get("Warszawa").getYears())
                .hasSize(1)
                .containsEntry(2023, new YearAggregate(2023, 5100, 4));
    }
}
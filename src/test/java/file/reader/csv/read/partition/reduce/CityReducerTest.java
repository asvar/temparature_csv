package file.reader.csv.read.partition.reduce;

import file.reader.csv.read.partition.aggregate.PartitionAggregator;
import file.reader.csv.read.partition.map.TemperatureFileRow;
import file.reader.csv.storage.Year;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CityReducerTest {

    @Test
    void shouldReduceAggregatedData(){
        PartitionAggregator aggregator1 = new PartitionAggregator();
        aggregator1.collect(new TemperatureFileRow("Warszawa", 2023, 1700));
        PartitionAggregator aggregator2 = new PartitionAggregator();
        aggregator2.collect(new TemperatureFileRow("Warszawa", 2023, 1900));
        CityReducer reducer = new CityReducer();
        reducer.collect(aggregator1);
        reducer.collect(aggregator2);

        Map<String, List<Year>> result = reducer.reduce();

        assertThat(result)
                .hasSize(1)
                .containsEntry("Warszawa", List.of(new Year(2023, new BigDecimal("18.00"))));

    }
}
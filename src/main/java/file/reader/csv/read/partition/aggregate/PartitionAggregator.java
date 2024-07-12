package file.reader.csv.read.partition.aggregate;

import file.reader.csv.read.partition.map.TemperatureFileRow;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PartitionAggregator {
    private final Map<String, CityAggregate> cities = new HashMap<>();

    public void collect(TemperatureFileRow row) {
        if (cities.containsKey(row.city())) {
            cities.get(row.city()).collect(new YearTemperature(row.year(), row.temp()));
        } else {
            cities.put(row.city(), new CityAggregate(row.city(), new YearTemperature(row.year(), row.temp())));
        }
    }

}

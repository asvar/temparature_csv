package file.reader.csv.read;

import file.reader.csv.read.partition.aggregate.YearTemperature;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "name")
public class YearAggregate {
    private int name;
    private long totalTemp;
    private long numberOfRecords;

    public YearAggregate(YearTemperature yearTemperature) {
        this.name = yearTemperature.year();
        this.totalTemp = yearTemperature.temp();
        this.numberOfRecords = 1;
    }

    public void add(long temp, long numberOfRecords) {
        this.totalTemp += temp;
        this.numberOfRecords += numberOfRecords;
    }

    public void add(long temp) {
        this.totalTemp += temp;
        this.numberOfRecords += 1;
    }
}

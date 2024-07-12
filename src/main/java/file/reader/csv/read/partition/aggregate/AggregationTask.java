package file.reader.csv.read.partition.aggregate;

import file.reader.csv.read.partition.map.RowMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class AggregationTask implements Callable<PartitionAggregator> {
    private final RowMapper rowMapper;
    private final List<String> lines;

    @Override
    public PartitionAggregator call() throws Exception {
        PartitionAggregator aggregator = new PartitionAggregator();
        lines.stream().map(rowMapper::map).forEach(aggregator::collect);

        return aggregator;
    }
}

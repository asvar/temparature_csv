package file.reader.csv.read.partition.map;

public interface RowMapper {
    default TemperatureFileRow map(String line) {
        String[] arguments = line.split(";");

        long temperature = Math.round(Double.parseDouble(arguments[2]) * 100);

        return new TemperatureFileRow(arguments[0], Integer.parseInt(arguments[1].substring(0, 4)), temperature);
    }
}

package file.reader.csv.storage;

import file.reader.csv.read.ReadResult;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TemperatureDataManager {
    //Reference should be taken directly each time
    private volatile Map<String, List<Year>> currentData = new HashMap<>();

    public Optional<List<Year>> getByCity(String city) {
        //At the start of processing save reference to a current state
        Map<String, List<Year>> currentData = this.currentData;

        if (currentData.containsKey(city)) {
            return Optional.of(currentData.get(city));
        } else {
            return Optional.empty();
        }
    }

    public void newReadData(ReadResult result){
        if(result.successful()){
            currentData = result.data();
        }
    }
}

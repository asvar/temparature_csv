package file.reader.csv.read;

import file.reader.csv.storage.Year;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ReadResult(boolean successful, Map<String, List<Year>> data) {
    public static ReadResult successful(Map<String, List<Year>> data){
        return new ReadResult(true, data);
    }

    public static ReadResult failed(){
        return new ReadResult(false, new HashMap<>());
    }
}

package file.reader.csv;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

    @Value("${dataFile.charset}")
    private String charset;

    @Value("${dataFile.location}")
    private String dataFile;
}

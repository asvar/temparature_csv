package file.reader.csv;

import file.reader.csv.modification.FileModificationManager;
import file.reader.csv.read.ReadManager;
import file.reader.csv.read.ReadResult;
import file.reader.csv.storage.TemperatureDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class CsvApplicationConfiguration {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public TemperatureDataManager temperatureDataManager() {
        return new TemperatureDataManager();
    }

    @Bean
    public ReadManager readManager() throws IOException {
        return new ReadManager(dataFile().toPath(), Charset.forName(applicationProperties.getCharset(), StandardCharsets.UTF_8));
    }

    @Bean
    public File dataFile() throws IOException {
        return resourceLoader.getResource(applicationProperties.getDataFile()).getFile();
    }

    @Bean
    public FileModificationManager modificationManager() throws IOException {
        return new FileModificationManager(
                dataFile(),
                readManager(),
                temperatureDataManager()
        );
    }

    @EventListener(ContextRefreshedEvent.class)
    public void readData() throws IOException {
        temperatureDataManager().newReadData(readManager().readFile());
    }
}

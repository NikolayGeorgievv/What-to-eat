package whattoeat.app.service.impl.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CML implements CommandLineRunner {

    private final CSVService csvService;

    public CML(CSVService csvService) {
        this.csvService = csvService;
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO: ADD a check if the data is already inserted
        csvService.readCsvAndInsertData();
    }
}

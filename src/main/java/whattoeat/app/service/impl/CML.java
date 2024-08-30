package whattoeat.app.service.impl;

import org.springframework.boot.CommandLineRunner;

//@Component
public class CML implements CommandLineRunner {

    private final CSVService csvService;

    public CML(CSVService csvService) {
        this.csvService = csvService;
    }

    @Override
    public void run(String... args) throws Exception {
        csvService.readCsvAndInsertData();
    }
}

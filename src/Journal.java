import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Journal {
    private List<String> log = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    void record(String operation) {
        log.add("[" + LocalDateTime.now().format(FMT) + "] " + operation);
    }

    void printLog() {
        if (log.isEmpty()) {
            System.out.println("(journal vazio)");
            return;
        }
        log.forEach(System.out::println);
    }
}

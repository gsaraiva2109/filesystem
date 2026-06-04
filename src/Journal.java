import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Journal implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;
    private final ArrayList<String> log = new ArrayList<>();
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

    boolean isEmpty() {
        return log.isEmpty();
    }

    String lastEntry() {
        return log.getLast();
    }

    void removeLast() {
        log.removeLast();
    }

    void pushBack(String entry) {
        log.add(entry);
    }
}

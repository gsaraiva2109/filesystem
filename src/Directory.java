import java.util.LinkedHashMap;
import java.util.Map;

class Directory extends FileNode {
    Map<String, FileNode> children = new LinkedHashMap<>();

    Directory(String name) {
        super(name, true);
    }
}

import java.util.LinkedHashMap;
import java.util.Map;

class Directory extends FileNode {
    private static final long serialVersionUID = UID;
    Map<String, FileNode> children = new LinkedHashMap<>();

    Directory(String name) {
        super(name, true);
    }
}

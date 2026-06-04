import java.util.LinkedHashMap;

class Directory extends FileNode {
    @java.io.Serial
    private static final long serialVersionUID = 1L;
    LinkedHashMap<String, FileNode> children = new LinkedHashMap<>();

    Directory(String name) {
        super(name, true);
    }
}

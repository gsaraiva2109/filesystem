import java.io.Serializable;

abstract class FileNode implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1;
    String name;
    boolean isDirectory;

    FileNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
}

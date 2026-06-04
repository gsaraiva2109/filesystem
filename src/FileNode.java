import java.io.Serializable;

abstract class FileNode implements Serializable {
    static final long UID = 1;
    String name;
    boolean isDirectory;

    FileNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
}

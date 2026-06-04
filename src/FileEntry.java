class FileEntry extends FileNode {
    @java.io.Serial
    private static final long serialVersionUID = 1L;
    String content;

    FileEntry(String name) {
        this(name, "");
    }

    FileEntry(String name, String content) {
        super(name, false);
        this.content = content;
    }
}

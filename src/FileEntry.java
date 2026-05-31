class FileEntry extends FileNode {
    String content;

    FileEntry(String name) {
        super(name, false);
        this.content = "";
    }

    FileEntry(String name, String content) {
        super(name, false);
        this.content = content;
    }
}

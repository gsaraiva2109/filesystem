class FileSystemSimulator {
    private final Directory root;
    private final Journal journal;

    FileSystemSimulator() {
        this.root = new Directory("/");
        this.journal = new Journal();
    }

    // --- path helpers ---

    private Directory resolveParent(String path) {
        String[] parts = path.split("/");
        Directory current = root;
        for (int i = 1; i < parts.length - 1; i++) {
            if (parts[i].isEmpty()) continue;
            FileNode node = current.children.get(parts[i]);
            if (node == null || !node.isDirectory) return null;
            current = (Directory) node;
        }
        return current;
    }

    private String lastName(String path) {
        String[] parts = path.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            if (!parts[i].isEmpty()) return parts[i];
        }
        return "";
    }

    private FileNode resolve(String path) {
        if (path.equals("/")) return root;
        String[] parts = path.split("/");
        Directory current = root;
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;
            FileNode node = current.children.get(parts[i]);
            if (node == null) return null;
            if (i == parts.length - 1) return node;
            if (!node.isDirectory) return null;
            current = (Directory) node;
        }
        return current;
    }

    // --- directory operations ---

    void createDirectory(String path) {
        journal.record("MKDIR: " + path);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || name.isEmpty()) {
            System.out.println("Erro: caminho inválido: " + path);
            return;
        }
        if (parent.children.containsKey(name)) {
            System.out.println("Erro: já existe: " + path);
            return;
        }
        parent.children.put(name, new Directory(name));
        System.out.println("Diretório criado: " + path);
    }

    void deleteDirectory(String path) {
        journal.record("RMDIR: " + path);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + path);
            return;
        }
        if (!parent.children.get(name).isDirectory) {
            System.out.println("Erro: não é diretório (use rm): " + path);
            return;
        }
        parent.children.remove(name);
        System.out.println("Diretório removido: " + path);
    }

    void renameDirectory(String path, String newName) {
        journal.record("MVDIR: " + path + " -> " + newName);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + path);
            return;
        }
        FileNode node = parent.children.get(name);
        if (!node.isDirectory) {
            System.out.println("Erro: não é diretório (use mv): " + path);
            return;
        }
        parent.children.remove(name);
        node.name = newName;
        parent.children.put(newName, node);
        System.out.println("Diretório renomeado: " + name + " -> " + newName);
    }

    // --- file operations ---

    void createFile(String path) {
        journal.record("TOUCH: " + path);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || name.isEmpty()) {
            System.out.println("Erro: caminho inválido: " + path);
            return;
        }
        parent.children.put(name, new FileEntry(name));
        System.out.println("Arquivo criado: " + path);
    }

    void copyFile(String srcPath, String destPath) {
        journal.record("COPY: " + srcPath + " -> " + destPath);
        FileNode src = resolve(srcPath);
        if (src == null || src.isDirectory) {
            System.out.println("Erro: arquivo não encontrado: " + srcPath);
            return;
        }
        Directory destParent = resolveParent(destPath);
        String destName = lastName(destPath);
        if (destParent == null || destName.isEmpty()) {
            System.out.println("Erro: destino inválido: " + destPath);
            return;
        }
        destParent.children.put(destName, new FileEntry(destName, ((FileEntry) src).content));
        System.out.println("Arquivo copiado: " + srcPath + " -> " + destPath);
    }

    void deleteFile(String path) {
        journal.record("RM: " + path);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + path);
            return;
        }
        if (parent.children.get(name).isDirectory) {
            System.out.println("Erro: é um diretório (use rmdir): " + path);
            return;
        }
        parent.children.remove(name);
        System.out.println("Arquivo removido: " + path);
    }

    void renameFile(String path, String newName) {
        journal.record("MV: " + path + " -> " + newName);
        Directory parent = resolveParent(path);
        String name = lastName(path);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + path);
            return;
        }
        FileNode node = parent.children.get(name);
        if (node.isDirectory) {
            System.out.println("Erro: é um diretório (use mvdir): " + path);
            return;
        }
        parent.children.remove(name);
        node.name = newName;
        parent.children.put(newName, node);
        System.out.println("Arquivo renomeado: " + name + " -> " + newName);
    }

    void listDirectory(String path) {
        journal.record("LS: " + path);
        FileNode node = resolve(path);
        if (node == null || !node.isDirectory) {
            System.out.println("Erro: diretório não encontrado: " + path);
            return;
        }
        Directory dir = (Directory) node;
        if (dir.children.isEmpty()) {
            System.out.println("(diretório vazio)");
            return;
        }
        for (FileNode child : dir.children.values()) {
            System.out.println((child.isDirectory ? "[DIR]  " : "[FILE] ") + child.name);
        }
    }

    void printJournal() {
        journal.printLog();
    }
}

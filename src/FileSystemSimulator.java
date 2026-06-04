import java.io.*;

class FileSystemSimulator {
    private Directory root;
    private Journal journal;
    private String currentPath = "/";

    FileSystemSimulator() {
        if (!load()) {
            this.root = new Directory("/");
            this.journal = new Journal();
        }
    }

    String getCurrentPath() {
        return currentPath;
    }

    // --- path helpers ---

    private String toAbsolute(String path) {
        if (path.startsWith("/")) return path;
        if (currentPath.equals("/")) return "/" + path;
        return currentPath + "/" + path;
    }

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

    // --- navigation ---

    boolean changeDirectory(String path) {
        if (path.equals("..")) {
            if (currentPath.equals("/")) return true;  // já está na raiz
            int lastSlash = currentPath.lastIndexOf("/");
            currentPath = lastSlash == 0 ? "/" : currentPath.substring(0, lastSlash);
            return true;
        }

        if (path.equals(".")) return true;

        String resolved = toAbsolute(path);
        FileNode node = resolve(resolved);
        if (node == null || !node.isDirectory) return false;
        currentPath = resolved;
        return true;
    }

    // --- directory operations ---

    void createDirectory(String path) {
        String abs = toAbsolute(path);
        journal.record("MKDIR: " + abs);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || name.isEmpty()) {
            System.out.println("Erro: caminho inválido: " + path);
            return;
        }
        if (parent.children.containsKey(name)) {
            System.out.println("Erro: já existe: " + abs);
            return;
        }
        parent.children.put(name, new Directory(name));
        System.out.println("Diretório criado: " + abs);
    }

    void deleteDirectory(String path) {
        String abs = toAbsolute(path);
        journal.record("RMDIR: " + abs);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + abs);
            return;
        }
        if (!parent.children.get(name).isDirectory) {
            System.out.println("Erro: não é diretório (use rm): " + abs);
            return;
        }
        parent.children.remove(name);
        System.out.println("Diretório removido: " + abs);
    }

    void renameDirectory(String path, String newName) {
        String abs = toAbsolute(path);
        journal.record("MVDIR: " + abs + " -> " + newName);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + abs);
            return;
        }
        FileNode node = parent.children.get(name);
        if (!node.isDirectory) {
            System.out.println("Erro: não é diretório (use mv): " + abs);
            return;
        }
        parent.children.remove(name);
        node.name = newName;
        parent.children.put(newName, node);
        System.out.println("Diretório renomeado: " + name + " -> " + newName);
    }

    // --- file operations ---

    void createFile(String path) {
        String abs = toAbsolute(path);
        journal.record("TOUCH: " + abs);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || name.isEmpty()) {
            System.out.println("Erro: caminho inválido: " + path);
            return;
        }
        parent.children.put(name, new FileEntry(name));
        System.out.println("Arquivo criado: " + abs);
    }

    void copyFile(String srcPath, String destPath) {
        String srcAbs = toAbsolute(srcPath);
        String destAbs = toAbsolute(destPath);
        journal.record("COPY: " + srcAbs + " -> " + destAbs);
        FileNode src = resolve(srcAbs);
        if (src == null || src.isDirectory) {
            System.out.println("Erro: arquivo não encontrado: " + srcAbs);
            return;
        }
        Directory destParent = resolveParent(destAbs);
        String destName = lastName(destAbs);
        if (destParent == null || destName.isEmpty()) {
            System.out.println("Erro: destino inválido: " + destAbs);
            return;
        }
        destParent.children.put(destName, new FileEntry(destName, ((FileEntry) src).content));
        System.out.println("Arquivo copiado: " + srcAbs + " -> " + destAbs);
    }

    void deleteFile(String path) {
        String abs = toAbsolute(path);
        journal.record("RM: " + abs);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + abs);
            return;
        }
        if (parent.children.get(name).isDirectory) {
            System.out.println("Erro: é um diretório (use rmdir): " + abs);
            return;
        }
        parent.children.remove(name);
        System.out.println("Arquivo removido: " + abs);
    }

    void renameFile(String path, String newName) {
        String abs = toAbsolute(path);
        journal.record("MV: " + abs + " -> " + newName);
        Directory parent = resolveParent(abs);
        String name = lastName(abs);
        if (parent == null || !parent.children.containsKey(name)) {
            System.out.println("Erro: não encontrado: " + abs);
            return;
        }
        FileNode node = parent.children.get(name);
        if (node.isDirectory) {
            System.out.println("Erro: é um diretório (use mvdir): " + abs);
            return;
        }
        parent.children.remove(name);
        node.name = newName;
        parent.children.put(newName, node);
        System.out.println("Arquivo renomeado: " + name + " -> " + newName);
    }

    void listDirectory(String path) {
        String abs = path.isEmpty() ? currentPath : toAbsolute(path);
        journal.record("LS: " + abs);
        FileNode node = resolve(abs);
        if (node == null || !node.isDirectory) {
            System.out.println("Erro: diretório não encontrado: " + abs);
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

    // --- persistence ---

    void save() {
        var dir = new File("data");
        dir.mkdirs();
        try (var oos = new ObjectOutputStream(new FileOutputStream("data/filesystem.dat"))) {
            oos.writeObject(root);
            oos.writeObject(journal);
            System.out.println("Salvo em data/filesystem.dat");
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    boolean load() {
        var file = new File("data/filesystem.dat");
        if (!file.exists()) return false;
        try (var ois = new ObjectInputStream(new FileInputStream(file))) {
            root = (Directory) ois.readObject();
            journal = (Journal) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            return false;
        }
    }
}

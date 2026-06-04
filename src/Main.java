import java.util.Scanner;

void main() {
	FileSystemSimulator fs = new FileSystemSimulator();
	Scanner scanner = new Scanner(System.in);

	System.out.println("Bem Vindo(a)!");
	System.out.println("Comandos: mkdir  rmdir  renamedir  touch  cp  rm  rename  ls  cd  tree  undo  save  log  exit");
	System.out.println("Exemplo:  mkdir /documentos");
	System.out.println();

	while (true) {
		System.out.print(fs.getCurrentPath() + "> ");
		if (!scanner.hasNextLine()) break;
		String line = scanner.nextLine().trim();
		if (line.isEmpty()) continue;

		String[] parts = line.split("\\s+", 3);
		String cmd = parts[0].toLowerCase();

		switch (cmd) {
			case "mkdir":
				if (parts.length < 2) {
					System.out.println("Uso: mkdir <caminho>");
					break;
				}
				fs.createDirectory(parts[1]);
				break;
			case "rmdir":
				if (parts.length < 2) {
					System.out.println("Uso: rmdir <caminho>");
					break;
				}
				fs.deleteDirectory(parts[1]);
				break;
			case "renamedir":
				if (parts.length < 3) {
					System.out.println("Uso: renamedir <caminho> <novo-nome>");
					break;
				}
				fs.renameDirectory(parts[1], parts[2]);
				break;
			case "touch":
				if (parts.length < 2) {
					System.out.println("Uso: touch <caminho>");
					break;
				}
				fs.createFile(parts[1]);
				break;
			case "cp":
				if (parts.length < 3) {
					System.out.println("Uso: cp <origem> <destino>");
					break;
				}
				fs.copyFile(parts[1], parts[2]);
				break;
			case "rm":
				if (parts.length < 2) {
					System.out.println("Uso: rm <caminho>");
					break;
				}
				fs.deleteFile(parts[1]);
				break;
			case "rename":
				if (parts.length < 3) {
					System.out.println("Uso: rename <caminho> <novo-nome>");
					break;
				}
				fs.renameFile(parts[1], parts[2]);
				break;
			case "ls":
				fs.listDirectory(parts.length >= 2 ? parts[1] : "");
				break;
			case "cd":
				if (parts.length < 2) {
					System.out.println("Uso: cd <caminho>");
					break;
				}
				if (!fs.changeDirectory(parts[1])) {
					System.out.println("Erro: diretório não encontrado: " + parts[1]);
				}
				break;
			case "tree":
				fs.printTree(parts.length >= 2 ? parts[1] : "");
				break;
			case "undo":
				fs.undo();
				break;
			case "save":
				fs.save();
				break;
			case "log":
				fs.printJournal();
				break;
			case "exit":
				System.out.println("Encerrando simulador.");
				scanner.close();
				return;
			default:
				System.out.println("Comando desconhecido: " + cmd);
				System.out.println("Comandos: mkdir  rmdir  renamedir  touch  cp  rm  rename  ls  cd  tree  undo  save  log  exit");
		}
	}

	scanner.close();
}

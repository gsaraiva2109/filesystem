void main() {
	FileSystemSimulator fs = new FileSystemSimulator();
	Scanner scanner = new Scanner(System.in);

	IO.println("=== Simulador de Sistema de Arquivos ===");
	IO.println("Comandos: mkdir  rmdir  mvdir  touch  cp  rm  mv  ls  log  exit");
	IO.println("Exemplo:  mkdir /documentos");
	IO.println();

	while (true) {
		IO.print("> ");
		if (!scanner.hasNextLine()) break;
		String line = scanner.nextLine().trim();
		if (line.isEmpty()) continue;

		String[] parts = line.split("\\s+", 3);
		String cmd = parts[0].toLowerCase();

		switch (cmd) {
			case "mkdir":
				if (parts.length < 2) {
					IO.println("Uso: mkdir <caminho>");
					break;
				}
				fs.createDirectory(parts[1]);
				break;
			case "rmdir":
				if (parts.length < 2) {
					IO.println("Uso: rmdir <caminho>");
					break;
				}
				fs.deleteDirectory(parts[1]);
				break;
			case "mvdir":
				if (parts.length < 3) {
					IO.println("Uso: mvdir <caminho> <novo-nome>");
					break;
				}
				fs.renameDirectory(parts[1], parts[2]);
				break;
			case "touch":
				if (parts.length < 2) {
					IO.println("Uso: touch <caminho>");
					break;
				}
				fs.createFile(parts[1]);
				break;
			case "cp":
				if (parts.length < 3) {
					IO.println("Uso: cp <origem> <destino>");
					break;
				}
				fs.copyFile(parts[1], parts[2]);
				break;
			case "rm":
				if (parts.length < 2) {
					IO.println("Uso: rm <caminho>");
					break;
				}
				fs.deleteFile(parts[1]);
				break;
			case "mv":
				if (parts.length < 3) {
					IO.println("Uso: mv <caminho> <novo-nome>");
					break;
				}
				fs.renameFile(parts[1], parts[2]);
				break;
			case "ls":
				fs.listDirectory(parts.length >= 2 ? parts[1] : "/");
				break;
			case "log":
				fs.printJournal();
				break;
			case "exit":
				IO.println("Encerrando simulador.");
				scanner.close();
				return;
			default:
				IO.println("Comando desconhecido: " + cmd);
				IO.println("Comandos: mkdir  rmdir  mvdir  touch  cp  rm  mv  ls  log  exit");
		}
	}

	scanner.close();
}

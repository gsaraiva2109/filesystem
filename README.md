# Simulador de Sistema de Arquivos

**Link GitHub:** *https://github.com/gsaraiva2109/filesystem*

---

## Parte 1 — Introdução ao Sistema de Arquivos com Journaling

### O que é um Sistema de Arquivos?

Um sistema de arquivos (file system) é a estrutura que um sistema operacional utiliza para organizar, armazenar e recuperar dados em dispositivos de armazenamento, como HDs e SSDs. Ele define como os dados são estruturados no disco, como os arquivos recebem nomes, e como os diretórios organizam esses arquivos de forma hierárquica.

Sem um sistema de arquivos, seria impossível gerenciar os dados persistidos em disco de forma organizada. Sistemas como FAT32, NTFS (Windows), ext4 (Linux) e APFS (macOS) são exemplos reais de sistemas de arquivos.

### Conceito de Journaling

**Journaling** é um mecanismo de integridade de dados que registra as operações a serem realizadas *antes* de executá-las. Esse registro é chamado de **journal** ou **log de transações**.

Se o sistema travar no meio de uma operação (queda de energia, por exemplo), o journal permite que o SO saiba o que estava sendo feito e possa:
- **Concluir** a operação (redo), ou
- **Desfazê-la** (undo), garantindo que o disco nunca fique em estado inconsistente.

### Tipos de Journaling

| Tipo | Descrição |
|------|-----------|
| **Write-Ahead Logging (WAL)** | Registra a intenção *antes* de executar. Estratégia usada neste simulador. |
| **Log-Structured** | Todo o sistema de arquivos é um log sequencial. Usado em SSDs e sistemas modernos. |
| **Ordered** | Metadados vão ao journal; dados são gravados antes dos metadados. Padrão no ext3. |
| **Data journaling** | Tanto dados quanto metadados são journaled. Mais seguro, porém mais lento. |

---

## Parte 2 — Arquitetura do Simulador

### Estrutura de Dados

O simulador representa o sistema de arquivos como uma **árvore na memória**. Cada nó da árvore é um `FileNode`.

```
FileNode (abstrato)
├── FileEntry  →  representa um arquivo (tem conteúdo)
└── Directory  →  representa um diretório (tem filhos)
```

Um `Directory` armazena seus filhos em um `Map<String, FileNode>` (chave = nome do item). Isso permite acesso em O(1) por nome, simulando uma tabela de inodes simplificada.

**Diagrama da árvore de exemplo:**

```
/  (root Directory)
├── docs/  (Directory)
│   ├── relatorio.txt  (FileEntry)
│   └── notas.txt      (FileEntry)
└── backup/  (Directory)
    └── relatorio.txt  (FileEntry)
```

### Implementação do Journaling

O journaling segue o padrão **Write-Ahead Logging**: o método `journal.record(operação)` é chamado **antes** de qualquer modificação na árvore.

Cada entrada no log tem o formato:

```
[2026-05-31T10:00:00] MKDIR: /docs
[2026-05-31T10:00:01] TOUCH: /docs/relatorio.txt
[2026-05-31T10:00:02] COPY: /docs/relatorio.txt -> /backup/relatorio.txt
```

O journal é mantido em memória (lista de Strings) e exibido com o comando `log`.

---

## Parte 3 — Implementação em Java

### Classes

| Classe | Responsabilidade |
|--------|-----------------|
| `FileNode` | Classe abstrata base; armazena nome e flag `isDirectory` |
| `FileEntry` | Representa um arquivo; estende `FileNode` |
| `Directory` | Representa um diretório; estende `FileNode`; contém mapa de filhos |
| `Journal` | Gerencia o log de operações (write-ahead logging) |
| `FileSystemSimulator` | Implementa as 7 operações + resolução de caminhos |
| `Main` | Shell REPL; lê comandos do usuário e chama o simulador |

### Classe `FileNode` (abstrata)

```java
abstract class FileNode {
    String name;
    boolean isDirectory;

    FileNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
}
```

### Classe `Directory`

```java
class Directory extends FileNode {
    Map<String, FileNode> children = new LinkedHashMap<>();

    Directory(String name) {
        super(name, true);
    }
}
```

### Classe `Journal`

```java
class Journal {
    private List<String> log = new ArrayList<>();

    void record(String operation) {
        log.add("[" + LocalDateTime.now().format(FMT) + "] " + operation);
    }

    void printLog() {
        log.forEach(System.out::println);
    }
}
```

### Exemplo de operação — `createDirectory`

```java
void createDirectory(String path) {
    journal.record("MKDIR: " + path);      // 1. registra no journal (WAL)
    Directory parent = resolveParent(path); // 2. localiza o diretório pai
    String name = lastName(path);           // 3. extrai o nome final
    parent.children.put(name, new Directory(name)); // 4. insere na árvore
    System.out.println("Diretório criado: " + path);
}
```

---

## Parte 4 — Instalação e Execução

### Requisitos

- **Java 11** ou superior
- Terminal (Linux, macOS ou Windows com PowerShell/CMD)

Verificar instalação:

```bash
java -version
```

### Clonar o Repositório

```bash
git clone https://github.com/gsaraiva2109/filesystem.git
cd filesystem
```

### Compilar

```bash
mkdir out
javac src/*.java -d out
```

### Executar

```bash
java -cp out Main
```

### Comandos Disponíveis

| Comando | Descrição | Exemplo |
|---------|-----------|---------|
| `mkdir <caminho>` | Cria diretório | `mkdir /documentos` |
| `rmdir <caminho>` | Remove diretório | `rmdir /documentos` |
| `mvdir <caminho> <novo-nome>` | Renomeia diretório | `mvdir /documentos docs` |
| `touch <caminho>` | Cria arquivo vazio | `touch /docs/texto.txt` |
| `cp <origem> <destino>` | Copia arquivo | `cp /docs/a.txt /docs/b.txt` |
| `rm <caminho>` | Remove arquivo | `rm /docs/texto.txt` |
| `mv <caminho> <novo-nome>` | Renomeia arquivo | `mv /docs/a.txt b.txt` |
| `ls [caminho]` | Lista diretório | `ls /docs` |
| `log` | Exibe journal | `log` |
| `exit` | Encerra o simulador | `exit` |

### Exemplo de Sessão

```
=== Simulador de Sistema de Arquivos ===
> mkdir /docs
Diretório criado: /docs
> touch /docs/relatorio.txt
Arquivo criado: /docs/relatorio.txt
> cp /docs/relatorio.txt /docs/backup.txt
Arquivo copiado: /docs/relatorio.txt -> /docs/backup.txt
> ls /docs
[FILE] relatorio.txt
[FILE] backup.txt
> rm /docs/relatorio.txt
Arquivo removido: /docs/relatorio.txt
> log
[2026-05-31T10:00:00] MKDIR: /docs
[2026-05-31T10:00:01] TOUCH: /docs/relatorio.txt
[2026-05-31T10:00:02] COPY: /docs/relatorio.txt -> /docs/backup.txt
[2026-05-31T10:00:03] LS: /docs
[2026-05-31T10:00:04] RM: /docs/relatorio.txt
> exit
Encerrando simulador.
```

---

## Resultados Esperados

O simulador demonstra na prática:

1. Como um sistema de arquivos organiza dados em estrutura de árvore.
2. Como operações de manipulação (criar, copiar, mover, remover) alteram essa árvore.
3. Como o **journaling WAL** registra cada operação antes de executá-la, garantindo rastreabilidade.

Com isso, é possível compreender os princípios fundamentais que regem sistemas de arquivos reais como ext4 e NTFS.

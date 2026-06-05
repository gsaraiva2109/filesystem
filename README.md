# Simulador de Sistema de Arquivos

**Link GitHub:** *https://github.com/gsaraiva2109/filesystem*

---

## Autores

- Gabriel Saraiva Siqueira
- Gustavo Chaves Macedo

> Trabalho realizado em dupla, conforme solicitado no enunciado.

---

## Resumo

Este trabalho propõe o desenvolvimento de um simulador de sistema de arquivos em Java, com suporte a journaling, para representar em memória a organização hierárquica de arquivos e diretórios e executar operações básicas de um sistema operacional.

---

## Introdução

O gerenciamento eficiente de arquivos é essencial para o funcionamento dos sistemas operacionais. Um sistema de arquivos organiza, armazena e recupera dados em dispositivos de armazenamento, permitindo a manipulação de arquivos e diretórios de forma estruturada.

Neste projeto, foi desenvolvido um simulador que representa essa organização em memória e permite executar comandos típicos de um sistema de arquivos, incluindo criação, remoção, renomeação, cópia, listagem, navegação entre diretórios, persistência e registro de operações.

---

## Objetivo

Desenvolver um simulador de sistema de arquivos em Java que implemente funcionalidades básicas de manipulação de arquivos e diretórios, com suporte a journaling para registrar operações e auxiliar na integridade e rastreabilidade das ações executadas.

---

## Atenção aos detalhes do simulador

**Linguagem de programação:** Java

**Operações implementadas:**

- Copiar arquivos
- Apagar arquivos
- Renomear arquivos
- Criar diretórios
- Apagar diretórios
- Renomear diretórios
- Listar arquivos de um diretório

**Funcionalidades adicionais do simulador:**

- Navegação entre diretórios com `cd`
- Visualização da árvore com `tree`
- Desfazer última operação com `undo`
- Salvamento do estado com `save`
- Registro de operações com `log`

---

## Metodologia

O simulador foi desenvolvido em linguagem Java e recebe comandos por meio de um shell interativo. Cada comando executa uma operação no sistema de arquivos em memória, utilizando chamadas de métodos com os parâmetros correspondentes.

As ações realizadas são registradas em um journal, permitindo rastrear as alterações executadas. O sistema também conta com persistência por meio de salvamento em arquivo.

---

## Parte 1: Introdução ao Sistema de Arquivos com Journaling

### Descrição do sistema de arquivos

Um sistema de arquivos é responsável por organizar e gerenciar dados em dispositivos de armazenamento. Ele define a estrutura hierárquica de arquivos e diretórios, além de regras para acesso, armazenamento e recuperação.

### Journaling

Journaling é uma técnica usada para registrar operações antes que elas sejam efetivamente aplicadas. Isso ajuda a manter a consistência do sistema e a rastrear mudanças feitas durante a execução.

---

## Parte 2: Arquitetura do Simulador

### Estrutura de dados

O simulador representa o sistema de arquivos como uma árvore em memória. A estrutura principal é formada pelas classes:

- `FileNode`
- `FileEntry`
- `Directory`

O diretório raiz contém os demais diretórios e arquivos, organizados hierarquicamente.

### Journaling

O journaling é gerenciado pela classe `Journal`, que armazena as operações executadas em ordem cronológica. Cada operação é registrada antes de ser aplicada na estrutura de dados.

---

## Parte 3: Implementação em Java

### Classe `FileSystemSimulator`

Responsável por implementar as operações do simulador, como:

- criação e remoção de diretórios
- criação e remoção de arquivos
- renomeação
- cópia
- listagem
- navegação
- persistência
- desfazer operações

### Classe `Journal`

Gerencia o histórico das operações executadas no simulador, armazenando registros em formato de log.

### Classes `FileNode`, `FileEntry` e `Directory`

- `FileNode`: classe base abstrata
- `FileEntry`: representa arquivos
- `Directory`: representa diretórios e seus filhos

---

## Parte 4: Instalação e funcionamento

### Requisitos

- Java 11 ou superior

### Execução

Após compilar o projeto, execute a classe `Main` para iniciar o shell interativo.

### Comandos disponíveis

| Comando | Descrição |
|---------|-----------|
| `mkdir <caminho>` | Cria diretório |
| `rmdir <caminho>` | Remove diretório |
| `renamedir <caminho> <novo-nome>` | Renomeia diretório |
| `touch <caminho>` | Cria arquivo vazio |
| `cp <origem> <destino>` | Copia arquivo |
| `rm <caminho>` | Remove arquivo |
| `rename <caminho> <novo-nome>` | Renomeia arquivo |
| `ls [caminho]` | Lista diretório |
| `cd <caminho>` | Altera diretório atual |
| `tree [caminho]` | Exibe a árvore de diretórios |
| `undo` | Desfaz a última operação registrada |
| `save` | Salva o estado atual do sistema |
| `log` | Exibe o journal |
| `exit` | Encerra o simulador |

### Exemplo de sessão

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
> rename /docs/relatorio.txt relatorio_v2.txt
Arquivo renomeado: relatorio.txt -> relatorio_v2.txt
> rm /docs/backup.txt
Arquivo removido: /docs/backup.txt
> log
[2026-05-31T10:00:00] MKDIR: /docs
[2026-05-31T10:00:01] TOUCH: /docs/relatorio.txt
[2026-05-31T10:00:02] COPY: /docs/relatorio.txt -> /docs/backup.txt
[2026-05-31T10:00:03] LS: /docs
[2026-05-31T10:00:04] MV: /docs/relatorio.txt -> relatorio_v2.txt
[2026-05-31T10:00:05] RM: /docs/backup.txt
> exit
Encerrando simulador.
```

---

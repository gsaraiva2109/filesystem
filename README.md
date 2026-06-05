# Simulador de Sistema de Arquivos

**Link GitHub:** *https://github.com/gsaraiva2109/filesystem*

---

## Autores
- Gabriel Saraiva Siqueira
- Gustavo Chaves Macedo

---

## Metodologia
O simulador foi desenvolvido em linguagem Java. Ele funciona através de chamadas de métodos com os devidos parâmetros. Foram implementados os comandos fundamentais de um sistema operacional.

---

## Introdução ao Sistema de Arquivos com Journaling

### Descrição do sistema de arquivos
Um sistema de arquivos é o componente do sistema operacional responsável por organizar, armazenar e recuperar dados em dispositivos, com o objetivo de gerenciar o espaço disponível, fornecer uma estrutura lógica como pastas e arquivos para o usuário, e garantir que as informações persistam de forma organizada.

### Journaling
Journaling é uma técnica de combate a falhas que registra as mudanças em um "diário" (log) ao serem aplicadas ao sistema de arquivos principal. O propósito é garantir a consistência dos dados em caso de interrupções.

**Tipos de Journaling:**
- **Write-Ahead Logging (WAL):** As alterações são gravadas no log antes de serem aplicadas. É o método utilizado neste simulador. Permite a rastreabilidade e reversão de ações.
- **Log-Structured File Systems:** Trata todo o sistema de arquivos como um log contínuo, onde novos dados são sempre anexados ao final, e o sistema é recriado de acordo com os logs a cada operação.
- **Metadata Journaling:** Registra apenas mudanças nos metadados. Oferece um equilíbrio entre performance e segurança.

---

## Parte 2: Arquitetura do Simulador

### Estrutura de Dados
O simulador utiliza uma estrutura de árvore para representar a hierarquia de arquivos. Cada nó da árvore é representado por objetos (Directory ou FileEntry) que extendem de uma classe base (FileNode), permitindo que diretórios contenham tanto arquivos quanto outros subdiretórios.

### Journaling
A implementação do journaling ocorre através de uma classe dedicada que mantém um histórico cronológico de operações. Cada operação de escrita (criar, renomear, copiar, remover) gera um registro de log contendo a data/hora, o tipo da operação e os caminhos envolvidos na operação. Esse log é lido pela função de "desfazer" (undo) para identificar e reverter a última ação lógica do sistema de arquivos.

### Sobre
O Filesystem e Journal não são visíveis a nível do sistema, todo o sistema de arquivos fica em um arquivo, o filesystem.dat e o journaling.dat.

O comando `save` deve ser utilizado para salvar o sistema (filesystem e journal), o carregamento é automático ao iniciar o projeto.

---

## Parte 3: Implementação em Java

### Classe "FileSystemSimulator"
É o núcleo do projeto. Implementa a lógica de negócio para todas as operações do sistema de arquivos, gerencia o diretório atual do usuário (onde o cd está) e coordena a interação com o Journal.

### Classes File e Directory
Representam as entidades básicas do sistema:
- **Directory:** Representa pastas, contendo um mapa com seus nós filhos (FileNode, que são arquivos ou outras pastas).
- **FileEntry:** Representa os arquivos individuais, armazenando seu nome.
- **FileNode:** Classe base abstrata que define as propriedades comuns a arquivos e diretórios.

### Classe Journal
Gerencia o log de operações. Possui métodos para registrar novas ações, recuperar a última entrada para operação de undo e imprimir o histórico (Journal) completo (`log`).

---

## Parte 4: Instalação e funcionamento

### Recursos utilizados
- **Linguagem:** Java 11
- **Bibliotecas Externas:** Nenhuma.
- **Persistência:** Serialização (utilizando classe java Serialization) de objetos para salvar o estado em `data/filesystem.dat`.

### Orientações sobre a execução
Recomendado utilizar uma IDE
1. Certifique-se de ter o JDK 11 ou superior instalado.
2. Compile os arquivos fonte na pasta `src/`.
3. Execute a classe `Main`.
4. Utilize o shell com os comandos abaixo:

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
| `undo` | Desfaz a última operação |
| `save` | Salva o estado atual |
| `log` | Exibe o journal |
| `exit` | Encerra o simulador |

---

## Resultados Esperados

Espera-se que esse simulador forneça uma experiência clara do funcionamento interno de um sistema de arquivos. Através da implementação do Journaling (tipo Write-Ahead Logging), o usuário deve ser capaz de:
- Manipular uma hierarquia complexa de arquivos e pastas via linha de comando.
- Visualizar o registro histórico de todas as alterações feitas no sistema.
- Reverter operações indesejadas utilizando a função `undo`, demonstrando a utilidade do log para a integridade dos dados.
- Persistir o estado do simulador em disco e recuperá-lo em execuções futuras.

---

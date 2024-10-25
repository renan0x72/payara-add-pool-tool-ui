# Add Pool to Payara JavaFX

Este projeto é uma aplicação JavaFX que permite adicionar um pool ao servidor Payara de forma simples e intuitiva. Com uma interface gráfica amigável, você pode configurar rapidamente as opções necessárias para criar um novo pool de conexão.

## Funcionalidades

- **Interface Gráfica**: Desenvolvida com JavaFX, a aplicação oferece uma interface fácil de usar para configuração de pools no Payara.
- **Configuração Simples**: Permite a entrada de informações essenciais como caminho do Payara, porta do servidor, IP do cliente, usuário, senha, nome do banco de dados e ID do pool.
- **Registro de Resultados**: Mostra a saída do resultado em um `TextArea`, facilitando a visualização do que foi realizado.

## Estrutura da Interface

A interface é composta por:

- **Campo para caminho do Payara**: Onde você insere o caminho do seu diretório Payara.
- **Campo para porta do servidor Payara**: Define a porta do servidor, com um valor padrão de `4848`.
- **Campo para IP e porta**: Configurações do cliente, onde você pode especificar o IP e a porta.
- **Campo para usuário e senha**: Informações de autenticação para o servidor Payara.
- **Escolha de banco de dados**: Um campo para definir o banco de dados a ser utilizado.
- **Campo para ID do pool**: Identificador do pool que você deseja criar.
- **Botões de ação**: Permite adicionar o pool e limpar os logs de saída.

## Requisitos

Para compilar e executar esta aplicação, você precisará dos seguintes requisitos:

- Java 11 ou superior
- Maven

## Compilação

Para compilar o projeto, use o seguinte comando:

```bash
mvn clean install -U
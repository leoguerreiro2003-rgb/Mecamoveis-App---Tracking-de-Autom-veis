[ReadMe.txt](https://github.com/user-attachments/files/30056188/ReadMe.txt)
===========================================
              MecaMóveis
      Sistema de Tracking de Automóveis
===========================================

Autores:
- Leandro Dionisio
- Leandro Pinto 
- Filipe Viegas

Curso:
CTeSP em Tecnologias e Programação de Sistemas de Informação

Descrição
---------
O MecaMóveis é uma aplicação Android desenvolvida em Kotlin utilizando
Jetpack Compose. A aplicação foi criada para facilitar a comunicação entre
clientes e oficinas mecânicas, permitindo acompanhar o estado das
reparações dos veículos, gerir orçamentos e consultar o histórico de
intervenções.

Funcionalidades
---------------
Área do Cliente
- Criar conta.
- Efetuar login.
- Registar veículos.
- Editar informações dos veículos.
- Consultar histórico de reparações.
- Consultar histórico de orçamentos.
- Acompanhar o estado das reparações.

Área do Mecânico
----------------
- Efetuar login.
- Gerir clientes.
- Gerir veículos.
- Registar novas reparações.
- Atualizar o estado das reparações.
- Criar e gerir orçamentos.
- Consultar histórico de reparações.

Tecnologias Utilizadas
----------------------
- Kotlin
- Android Studio
- Jetpack Compose
- Room Database
- Navigation Compose
- Material Design 3
- Google Mobile Ads (AdMob)

Estrutura do Projeto
--------------------
app/
 ├── data/
 │   ├── Base de dados (Room)
 │   └── DAO's
 │
 ├── model/
 │   └── Classes de dados
 │
 ├── view/
 │   ├── Cliente
 │   ├── Mecânico
 │   └── Autenticação
 │
 ├── ui/
 │   └── Tema da aplicação
 │
 └── MainActivity.kt

Requisitos
-----------
- Android Studio (versão recente)
- Android SDK
- Gradle
- Dispositivo Android ou Emulador

Como Executar
-------------
1. Abrir o projeto no Android Studio.
2. Aguardar que o Gradle sincronize.
3. Executar a aplicação num emulador ou dispositivo Android.
4. Testar as funcionalidades disponíveis.

Objetivo do Projeto
-------------------
Este projeto foi desenvolvido em contexto académico com o objetivo de
aplicar conhecimentos de desenvolvimento Android, bases de dados locais,
interfaces modernas em Jetpack Compose e gestão de informação relacionada
com oficinas automóveis.

Observações
-----------
- A aplicação utiliza uma base de dados local (Room).
- O projeto encontra-se organizado segundo uma arquitetura modular,
separando modelos, base de dados, interface e navegação.
- Foi utilizada uma chave de testes do Google AdMob para desenvolvimento.

===========================================
Fim do documento
===========================================

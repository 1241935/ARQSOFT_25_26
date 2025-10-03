# Library Manager System Report- P1

## Introdução
O presente documento descreve a situação atual e as perspetivas futuras de um Sistema de Gestão de Biblioteca, uma solução desenvolvida para atualizar e tornar mais eficientes os processos internos da biblioteca. O objetivo central é proporcionar uma experiência mais ágil e satisfatória tanto para os colaboradores como para os utilizadores, potenciando a eficácia das operações por meio da digitalização.
## Contexto
No projeto anterior, foi desenvolvido um serviço backend orientado a REST para Gestão de Bibliotecas. Esta aplicação fornece endpoints REST para gerir:

- Livros
- Géneros
- Autores
- Leitores
- Empréstimos

## Problema
O serviço de Gestão de Biblioteca não suporta:

- Extensibilidade
- Configurabilidade
- Confiabilidade

## Objetivos
Os objetivos deste projeto (P1) são:

### Desenvolver uma solução para:

- Persistir dados em diferentes modelos de dados (e.g., relacional, documento) e SGBDs: 
  - (i) SQL + Redis 
  - (ii) MongoDB+Redis
- Devolver o ISBN de um livro pelo título do mesmo através de diferentes sistemas externos:
  - (i) ISBNdb
  - (ii) Google Books API
- Gerar IDs de várias entidades em diferentes formatos de acordo com especificações variadas
- As alternativas anteriores devem ser aplicadas em tempo de setup por meio de configuração

### Melhorar os testes funcionais:

- Testes de Caixa-opaca com SUT = classes
- Testes de Caixa-transparente com SUT = classes de domínio
- Testes de mutação com SUT = classes
- Testes de Caixa-opaca funcional com SUT = controlador + serviço + {domínio, repositório, gateways}
- Testes de Caixa-opaca funcional com SUT = sistema

### Documentar:

- System as Is (reverse engineering de design)
- Requisitos, especialmente ASR (Requisitos Arquiteturais de Software)
- Alternativas de design arquitetónico e racionais
- Táticas
- Arquiteturas de referência
- Padrões

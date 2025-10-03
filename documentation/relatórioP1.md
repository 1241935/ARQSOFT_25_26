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
- Procurar o ISBN de um livro pelo título do mesmo através de diferentes sistemas externos:
  - (i) ISBNdb
  - (ii) Google Books API
- Gerar IDs de várias entidades em diferentes formatos de acordo com especificações variadas
- As alternativas anteriores devem ser aplicadas em tempo de setup por meio de configuração

### Documentar:

- System as Is (reverse engineering de design)
- Requisitos, especialmente ASR (Requisitos Arquiteturais de Software)
- Alternativas de design arquitetónico e racionais
- Táticas
- Arquiteturas de referência
- Padrões

### Melhorar os testes funcionais:

- Testes de Caixa-opaca com SUT = classes
- Testes de Caixa-transparente com SUT = classes de domínio
- Testes de mutação com SUT = classes
- Testes de Caixa-opaca funcional com SUT = controlador + serviço + {domínio, repositório, gateways}
- Testes de Caixa-opaca funcional com SUT = sistema

## Acerca dos requisitos

##### Quality attribute scenarios
- [Scenario 1 - Persistência de Dados](QAS/PersistingData-QAS.md)
- [Scenario 2 - Pesquisa de ISBNs através de APIs externas](QAS/SearchISBN-QAS.md)
- [Scenario 3 - Geração de IDs em diferentes formatos](QAS/IdGeneration-QAS.md)

## System As Is

### Vista física
![Vista física](Images/SAI_-_VF.svg)

### Vista lógica
#### Nivel 1
![Vista lógica Nivel 1](Images/SAI_-_VL1.svg)

#### Nivel 2
![Vista lógica Nivel 2](Images/SAI_-_VL2.svg)

#### Nivel 3
![Vista lógica Nivel 3](Images/SAI_-_VL3.svg)

### Vista de implementação

#### Nivel 1
![Vista de implementação Nivel 1](Images/SAI_-_VI1.svg)

#### Nivel 2
![Vista de implementação Nivel 2](Images/SAI_-_VI2.svg)

#### Nivel 3
![Vista de implementação Nivel 3](Images/SAI_-_VI3.svg)

#### Nivel 4
![Vista de implementação Nivel 4](Images/SAI_-_VI4.svg)
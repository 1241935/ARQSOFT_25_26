# Technical Memo - Configuração da Base de Dados

## Problema:
Necessidade de criar um sistema que suporte múltiplos sistemas de gerencia de bases de dados (SGBDs) de forma flexível e configurável, permitindo mudanças com mínimo impacto no código existente.

## Como resolver este problema:
Implementar uma arquitetura que permita a troca de SGBDs através de configurações, utilizando táticas de modificabilidade para garantir flexibilidade e manutenibilidade do sistema.

## Resumo da solução:
Desenvolver uma camada de abstração da base de dados altamente configurável, aplicando táticas específicas de modificabilidade para garantir a independência entre a lógica de negócios e a implementação do banco de dados.

## Fatores críticos:
- Diferentes clientes possuem SGBDs já estabelecidos nas suas infraestruturas
- Performance e consistência devem ser independentes do SGBD
- Manutenibilidade do código deve ser preservada

## Solução:
Aplicando as táticas de modificabilidade:

1. **Encapsular** (Reduce Coupling): Isolar a lógica de persistência num módulo separado, permitindo que diferentes tipos de persistência sejam facilmente adicionados ou modificados sem afetar outras partes do sistema.

2. **Utilizar um intermediário** (Use an Intermediary): Implementar um padrão de design como Strategy ou Bridge para abstrair a escolha do tipo de persistência, facilitando a troca de estratégias sem alterar o código cliente.

3. **Abstrair serviços comuns** (Abstract Common Services): Criar interfaces ou classes abstratas para os serviços de persistência, permitindo que diferentes tipos de persistência sejam aplicados com facilidade e consistência.

4. **Adiar a vinculação** (Defer Binding): Utilizar injeção de dependência ou configuração dinâmica para permitir que o tipo de persistência seja selecionado em tempo de execução, conforme a necessidade.

## Motivação:
Proporcionar flexibilidade e escalabilidade para suportar diferentes necessidades de persistência de dados, sem comprometer o desempenho ou a modularidade do sistema.

## Alternativas:
- Implementar uma solução híbrida com um banco de dados principal e caches distribuídos, permitindo otimização de performance mas aumentando a complexidade do sistema.

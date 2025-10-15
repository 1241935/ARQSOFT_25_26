# Technical Memo - Procura de ISBN por Título via APIs Externas

## Problema:
Necessidade de criar um mecanismo flexível e resiliente que permita obter o ISBN de um livro através do seu título,
utilizando diferentes APIs externas (ISBNdb, Google Books API e Open Library Search API). O sistema deve garantir precisão,
disponibilidade e performance, lidando com latência variável e eventuais falhas das APIs externas.

## Como resolver este problema:
Implementar uma arquitetura que suporte integração com múltiplas APIs externas através de um serviço unificado,
aplicando táticas de modificabilidade e disponibilidade para garantir resiliência, flexibilidade e baixo acoplamento.

## Resumo da solução:
Desenvolver um serviço de lookup de ISBN configurável que orquestre consultas a diferentes APIs externas,
agregando e validando os resultados para retornar o ISBN correto com base no título. O serviço deve suportar
fallbacks, caching e seleção dinâmica de fontes, assegurando precisão e desempenho.

## Fatores críticos:
- As APIs externas possuem diferentes formatos de resposta, políticas de autenticação e limites de requisições (rate limits).
- A disponibilidade e latência variam entre fornecedores.
- O sistema deve consolidar resultados de forma confiável, escolhendo o ISBN mais consistente.
- A performance deve ser adequada mesmo sob falhas parciais de uma ou mais APIs.
- O design deve permitir adicionar novas APIs com mínimo impacto no código existente.

## Solução:
Aplicando as táticas de modificabilidade e disponibilidade:

1. **Encapsular** (Reduce Coupling):
    - Isolar a lógica de comunicação com cada API em módulos independentes.
    - Cada integração deve implementar uma interface comum
    - Permitir adicionar novas integrações sem alterar o core do sistema.

2. **Utilizar um intermediário** (Use an Intermediary):
    - Implementar um serviço intermediário que orquestra as chamadas às APIs externas.
    - Aplicar o padrão Strategy para selecionar dinamicamente as fontes disponíveis.
    - Centralizar a lógica de fallback e de agregação de resultados.

3. **Abstrair serviços comuns** (Abstract Common Services):
    - Definir um contrato comum para todos os fornecedores de ISBN 
    - Normalizar respostas e formatos de dados (ex.: JSON → DTO comum).
    - Implementar mecanismos de cache para evitar chamadas redundantes.

4. **Adiar a vinculação** (Defer Binding):
    - Permitir configuração dinâmica das APIs ativas através de ficheiro de configuração

5. **Tolerância a falhas e disponibilidade**:
    - Implementar timeouts e circuit breakers para cada API externa.
    - Executar chamadas em paralelo para reduzir latência total.
    - Aplicar caching local (ex.: Redis)

## Motivação:
Garantir que o sistema oferece respostas rápidas e precisas, mesmo diante de falhas em fornecedores externos,
mantendo flexibilidade para incorporar novos serviços ou ajustar comportamentos através de configuração.

## Alternativas:
- Armazenar localmente um catálogo de livros sincronizado periodicamente, melhorando performance mas
  aumentando a complexidade de manutenção e sincronização.

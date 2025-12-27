# Technical Memo - Transição da Arquitetura do Sistema

## Problema:
A arquitetura centralizada atualmente utilizada limita a capacidade de atender aos requisitos de *performance*, *availability*, *scalability* e *elasticity*. 

## Como resolver este problema:
Adotar uma arquitetura descentralizada/distribuída, como *microserviços* ou arquitetura orientada a eventos, que permita maior modularidade, escalabilidade independente de componentes e melhor tolerância a falhas.

## Resumo da solução:
Migrar para uma arquitetura distribuída baseada em *microserviços*, dividindo o sistema em domínios independentes.

## Fatores críticos:
- A compatibilidade com APIs existentes deve ser minimamente impactada.
- A definição de domínios e serviços deve ser clara para evitar dependências rígidas.
- Manutenibilidade do código deve ser preservada.

## Solução:
Aplicando as táticas de modificabilidade:

1. **Encapsular** (Reduce Coupling): Separar o sistema em microserviços independentes, cada um responsável por um agregado de dominio específico, reduzindo o acoplamento bem como a facilidade de manutenção e escalabilidade.

2. **Abstrair serviços comuns** (Abstract Common Services): Implementar comunicação assíncrona através da utilização de eventos e mensagens para comunicação entre serviços onde possível, garantindo que falhas ou lentidão num serviço não impactem os outros.


## Motivação:
Garantir que o sistema atenda aos requisitos de negócio e qualidade, como desempenho, escalabilidade e disponibilidade, enquanto mantém flexibilidade para futuras evoluções e alinhamento estratégico.

## Alternativas:
- Implementar uma arquitetura híbrida (Microserviços + Monolitica), dividindo os componentes mais críticos e de alta carga em microserviços, mantendo as outras partes menos voláteis na estrutura monolítica, permitindo uma transição gradual para microseriços sem que exista um impacto inicial tão significativo.

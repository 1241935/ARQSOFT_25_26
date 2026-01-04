# Sistema de Gestão de Biblioteca — Relatório P2 (G2)

## 1. Introdução
Este relatório descreve a evolução do **Sistema de Gestão de Biblioteca** para uma arquitetura descentralizada e distribuída, composta por múltiplas aplicações independentes. O foco desta fase é assegurar a capacidade de deploy independente de cada serviço e a resiliência do sistema em ambiente Cloud (GCP).

---

## 2. Estratégia de Transição e Padrões de Arquitetura

### 2.1 Strangler Fig Pattern
A migração foi realizada utilizando o **Strangler Fig Pattern**, permitindo que as novas funcionalidades fossem extraídas do monólito original de forma gradual para serviços independentes.

### 2.2 Database per Service e Domain Events
* **Isolamento:** Aplicou-se o padrão **Database per Service**, onde cada microserviço possui a sua própria base de dados persistente, eliminando o acoplamento de dados.
* **Comunicação:** A interação entre serviços é assíncrona, baseada em **Domain Events** e mediada por um **Message Broker**.

### 2.3 Transactional Outbox Pattern
Para garantir a atomicidade entre a base de dados local e a publicação de eventos no Broker, implementou-se o **Transactional Outbox Pattern**. Este padrão assegura que os eventos de domínio são publicados apenas após o sucesso da transação local, garantindo a integridade do sistema distribuído.

---

## 3. Vistas de Arquitetura 

### 3.1 Vista Física (Deployment no GCP)
A infraestrutura foi migrada para o **Google Cloud Platform (GCP)**.
* **Independência:** Cada aplicação corre potencialmente em máquinas distintas para suportar o deploy individual.
* **Parsimónia:** O sistema utiliza hardware de forma parsimoniosa, escalando apenas quando a procura é elevada.


![Diagrama da Vista Física](Images/VF3.png)

---

## 4. Design de Processos: Padrão SAGA

Para o requisito de criar um **Reader** e o respetivo **User** no mesmo pedido, foi implementada uma **Saga por Coreografia**.

### 4.1 Fluxo de Execução
1. **LendingsNReaders:** Cria o Leitor (Status: `PENDING`) e publica o evento `ReaderCreated`.
2. **AuthNUsers:** Consome o evento, cria o Utilizador e publica `UserCreated`.
3. **LendingsNReaders:** Consome a confirmação e ativa o Leitor (Status: `ACTIVE`).

### 4.2 Ação Compensatória
Caso a criação do utilizador falhe, é emitido um evento de erro que despoleta um rollback no serviço de leitores para anular a operação pendente.



---

## 5. Pipeline CI/CD (Jenkins) e Requisitos Funcionais

A pipeline Jenkins da P1 foi evoluída para suportar deploys independentes e os três ambientes: **Dev**, **Staging** e **Prod**.

### 5.1 Requisitos Não-Funcionais Implementados
* **Performance (+25%):** O sistema utiliza autoscaling para responder a picos de carga superior a Y pedidos/período.
* **Zero Downtime (ZDT):** Implementação de Rolling Updates para atualizações sem interrupção de serviço.
* **Automatic Rollback:** Em caso de falha nos *health checks* em produção, o serviço reverte automaticamente para a versão estável anterior.

### 5.2 Fluxo de Aprovação (Serviço A)
Foi configurada uma etapa na pipeline onde o utilizador que despoleta o build recebe uma **notificação por e-mail** com o link do serviço em **Staging**. O deploy em produção requer uma aprovação manual (Accept/Reject).

---

## 6. Análise Crítica e Métricas

| Aspeto | Situação P1 (Monólito) | Situação P2 (Microserviços) |
|:--- |:--- |:--- |
| **Deploy** | Acoplado e manual | Independente e automatizado |
| **Resiliência** | Ponto único de falha | Isolamento e Rollback automático |
| **Infraestrutura** | DEI Server / Local | Google Cloud Platform (GCP) |
| **Integridade** | Transação SQL | Padrão SAGA / Outbox |

---

## 7. Conclusão
A transição para uma arquitetura distribuída permitiu cumprir os objetivos de **deployabilidade independente** e **releasability**. O uso de padrões como SAGA e Outbox, aliado à automação no Jenkins e escalabilidade no GCP, resulta num sistema robusto que cumpre integralmente os requisitos de ODSOFT 2025/2026.
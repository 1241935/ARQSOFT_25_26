# Qualidade Attribute Scenario 1 - Persistência de dados em diferentes data models e SGBDs

| **Elemento**              | **Declaração**                                                                                                                                                                                   |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Atributo de Qualidade** | Scalability, Interoperability                                                                                                                                                                    |
| **Estímulo**              | O sistema precisa permitir a persistência de dados em diferentes data models (ex: relacional e document-oriented) e SGBDs (ex: SQL+Redis e MongoDB+Redis).                                       |
| **Fonte do Estímulo**     | A equipa de desenvolvimento identificou a necessidade de suportar múltiplos data models e SGBDs para garantir flexibilidade e integração abrangendo uma maior diversidade de sistemas.           |
| **Ambiente**              | [Por completar...]                                                                                                                                                                               |
| **Artefato**              | [Por completar...]                                                                                                                                                                               |
| **Resposta**              | O sistema deve permitir a persistência de dados de forma transparente e consistente em diferentes data models e SGBDs, garantindo flexibilidade de integração e mantendo um desempenho adequado. |
| **Medição da Resposta**   | 95% das operações de leitura e escrita devem ser concluídas em menos de 500 milissegundos, garantindo a consistência dos dados em 99.9% das transações, com uma taxa de erro inferior a 0.1%.    |

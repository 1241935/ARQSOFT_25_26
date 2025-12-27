# Cenário de Atributo de Qualidade 1 - Transição da Arquitetura do Sistema

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Atributo de Qualidade** | Modifiability |
| **Estímulo** | O sistema precisa ser migrado para uma arquitetura de microserviços para melhorar escalabilidade, manutenção e independência de componentes. |
| **Fonte do Estímulo** | A equipa de desenvolvimento identificou limitações na escalabilidade e agilidade do sistema atual devido à complexidade de modificar ou expandir funcionalidades numa arquitetura monolítica. |
| **Ambiente** | O ambiente de desenvolvimento e produção em que o sistema opera, com componentes novos sendo implementados em microserviços enquanto o sistema legado continua em execução. |
| **Artefato** | A base de código existente, os serviços monolíticos atuais, e a nova infraestrutura de microserviços planeada. |
| **Resposta** | O sistema deve ser modularizado em serviços independentes que se comuniquem por meio de APIs bem definidas. As funcionalidades devem ser facilmente modificáveis e implantáveis individualmente, minimizando impactos noutras partes do sistema. |
| **Medição da Resposta** | 1. |

# Technical Memo - Criação de Book, Author e Genre no mesmo processo

## Problema:
O processo atual exige que o librarian crie separadamente um Book, Author e Genre, resultando em redudância, ineficiência e aumento do risco de inconsistências de dados.

## Como resolver este problema:
Implementar um processo unificado que permita criar um Book, Author e Genre numa única operação, garantindo que as entidades sejam vinculadas e registadas corretamente no sistema.

## Resumo da solução:
Criar um endpoint ou serviço que aceite um pedido único com os dados referentes a Book, Author e Genre, validando e persistindo as informações de forma transacional, garantindo a consistência entre as entidades.

## Fatores críticos:
- Os dados devem ser validados por forma a seguirem os padrões estabelecidos.
- Garantir que todas as operações sejam realizadas numa única transação para evitar inconsistências no caso de falhas.
- A solução deve ser extensível para outros casos de uso semelhantes.
- O endpoint deve ser eficiente para lidar com grandes volumes de pedidos.

## Solução:
Aplicando as táticas de modificabilidade:

1. **Encapsular** (Reduce Coupling): Encapsulamento da lógica de criação e validação das entidades (Book, Author e Genre) num único serviço, o que se traduz no tratamento do processo de criação internamente, sem expor complexidade para o cliente.

2. **Transacionalidade** (Ensure Consistency): Visar a consistência dos dados, garantindo que ou todas as entidades são criadas com sucesso ou, em caso de alguma falha, nenhuma delas seja criada.


## Motivação:
Melhorar a eficiência do processo de criação de Book, Author e Genre, minimizando a intervenção do Librarian. Desta forma, garante-se a consistência de dados no sistema, facilitando também a manutenção e expansão da funcionalidade no futuro.

## Alternativas:
- Automatizar a chamada de múltiplos endpoints através da criação de um serviço automatizado capaz de efetuar os pedidos aos endpoints necessários para a criação das três entidades, sem integrá-los numa única operação.

<table>
  <tr>
    <th>Fator</th>
    <th>Descrição do Cenário</th>
    <th>Tipos de Variação</th>
    <th>Impacto no Negócio</th>
    <th>Ocorrência</th>
    <th>Severidade</th>
    <th>Prioridade</th>
  </tr>
  <tr>
    <td>Persistência poliglota</td>
    <td>O sistema deve suportar diferentes modelos de dados e SGBD conforme configurado.</td>
    <td>SQL + Redis ou MongoBD + Redis</td>
    <td>Melhora a resiliência e flexibilidade na peristência dos dados.</td>
    <td>Alta</td>
    <td>3 - Alta</td>
    <td>Alta</td>
  </tr>
  <tr>
    <td>Procura externa de ISBN</td>
    <td>O sistema deve permitir a integração com múltiplos sistemas externos de busca de ISBN.</td>
    <td> ISBNdb ou Google Books API</td>
    <td>Amplia a busca de dados e contribui positivamente para na disponibilidade e interoperabilidade com fontes externas.</td>
    <td>Média</td>
    <td>2 - Média</td>
    <td>Média</td>
  </tr>
  <tr>
    <td>Gerador de IDs</td>
    <td>O sistema deve ser flexível na geração de idenetificadores únicos.</td>
    <td>Base65 de um número aleatório de 6 dígitos ou TimeStamp com o hexadecimal de um número aleatório de 6 dígitos</td>
    <td>Garante unicidade e rastreabilidade de dos dados</td>
    <td>Baixa</td>
    <td>4 - Crítica</td>
    <td>Alta</td>
  </tr>
  <tr>
</table>

- **Frequência/Probabilidade**: Com base na tabela de referência, avaliada como Baixa, Média ou Alta.
- **Risco/Gravidade**: Conforme a tabela de risco de severidade (1 a 4).
- **Prioridade**: Determinada com base na combinação de frequência/probabilidade e gravidade (quanto maior, maior a prioridade).
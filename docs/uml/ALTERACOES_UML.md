# IFlower - Alterações de UML (versão do levantamento de 18/09 → versão alinhada ao código)

Este documento diz **exatamente o que adicionar (➕), alterar (✏️) e remover (➖)** em cada diagrama, com o motivo.
O código do backend já está implementado conforme os diagramas desta pasta, então **UML e código passam a contar a mesma história**.

- Diagramas de referência prontos: `docs/uml/img/*.png`
- Fontes editáveis (texto PlantUML): `docs/uml/src/*.puml`
- Para refazer no draw.io: use os PNGs como guia de posição e rótulos.

| Diagrama | Arquivo de referência | Situação |
|---|---|---|
| Classes (domínio) | `img/01-classes.png` | ✏️ muda bastante |
| Classes - detalhe OCP | `img/02-classes-ocp-pagamento.png` | ➕ novo |
| Casos de uso | `img/03-casos-de-uso.png` | ✏️ muda bastante |
| Atividades - Realizar Pedido | `img/04-atividade-realizar-pedido.png` | ✏️ |
| Atividades - Acompanhar Pedido | `img/05-atividade-acompanhar-pedido.png` | ✏️ corrige erros |
| Atividades - Cadastrar Produto | `img/06-atividade-cadastrar-produto.png` | ✏️ corrige erros |
| Atividades - Realizar Pagamento | `img/07-atividade-realizar-pagamento.png` | ➕ novo |
| Estados - Pedido | `img/08-estados-pedido.png` | ➕ novo (opcional, recomendado) |

---

## 1. Diagrama de classes

### 1.1 Princípio que explica quase todas as mudanças
No diagrama antigo, as classes de domínio tinham métodos como `cadastrar()`, `fazerLogin()`, `finalizarCompra()` e `excluir()`.
Isso mistura **dados + regras do objeto** com **persistência e orquestração**, e contradiz o SRP que vocês já aplicaram em `Entrega`.
No novo desenho, a entidade guarda só o que é dela; os casos de uso são executados por classes de **serviço** (`UsuarioService`, `PedidoService`...), mostradas no relatório como camada, não no diagrama de domínio.

### 1.2 Classes novas (➕)
| Classe | Por que existe |
|---|---|
| `Loja` | `Vendedor` tinha `cnpj`, `loja`, `descricao`. A loja é o centro do sistema (perfil, busca, avaliação, pedidos) e precisa de identidade própria. |
| `Endereco` (objeto de valor) | `endereco: String` não permite buscar lojas por cidade (RF004) nem registrar o destino da entrega. Reutilizado por `Usuario`, `Loja` e `Entrega`. |
| `ItemCarrinho` | `Carrinho` tinha `Array<Produto>`, sem quantidade nem personalização. |
| `ItemPedido` | Idem para `Pedido`; além disso guarda o **preço no momento da compra** (histórico não muda se o produto mudar de preço). |
| `PagamentoBase`, `PagamentoPix`, `PagamentoCartao`, `PagamentoBoleto` | Hierarquia da atividade de OCP. |
| Enums: `Papel`, `StatusPedido`, `StatusPagamento`, `TipoRetirada`, `TipoPagamento` | Substituem `String` livres; impedem valores inválidos e documentam o fluxo. |

### 1.3 Alterações por classe existente (✏️/➖)
| Classe | O que mudar | Por quê |
|---|---|---|
| **Usuario** (abstrata) | ✏️ `id: int` → `Long`; ✏️ `senha` → `senhaHash`; ✏️ `endereco: String` → `Endereco`; ➕ `ativo: boolean`; ➖ `cadastrar()`, `fazerLogin()`, `excluir()`; ✏️ `atualizarDados()` passa a receber `(nome, telefone, endereco)`; ➕ `desativar()`, `getPapel()` | Senha nunca é guardada em texto (RNF005). "Excluir" virou exclusão lógica para não quebrar pedidos antigos. Cadastro e login são do `UsuarioService`/`AuthService`. |
| **Cliente** | Mantém `cpf`. ➖ `gerenciarDados()`, `adicionarAoCarrinho()`, `finalizarCompra()`, `acompanharPedido()`, `visualizarInformacoesPedido()`, `selecionarEntregaRetirada()`, `avaliarVendedor()`, `consultarHistoricoPedido()` | São casos de uso, não comportamento do objeto. Ver tabela 1.5 para saber onde cada um foi parar. |
| **Vendedor** | ➖ `cnpj`, `loja: String`, `descricao` (vão para `Loja`); ➖ `cadastrarProduto()`, `gerenciarProduto()`, `visualizarPedidos()`; ➕ associação `1 — 1` com `Loja`; ➕ `vincularLoja()` | Separa a pessoa (vendedor) do negócio (loja). |
| **Produto** | ✏️ `preco: double` → `BigDecimal`; ✏️ `personalizacao: String` → `permitePersonalizacao: boolean`; ➕ `ativo`; ➖ `cadastrar()`, `atualizar()` (sem parâmetros), `excluir()`; ➕ `atualizar(...)`, `desativar()`; ➕ associação com `Loja` | `double` gera erro de arredondamento em dinheiro. O **texto** da personalização é escolhido pelo cliente na compra, então fica em `ItemCarrinho`/`ItemPedido`; o produto só informa se aceita. |
| **Carrinho** | ➖ `idCliente: int` e `produtos: Array<Produto>`; ➕ associação `0..1` com `Cliente`, composição `1 — 0..*` com `ItemCarrinho`, associação `0..1` com `Loja`; métodos finais: `adicionarItem(produto, qtd, personalizacao)`, `alterarQuantidade()`, `removerItem()`, `limpar()`, `calcularTotal()` | Regra de negócio: **o carrinho só tem produtos de uma loja** (cada pedido pertence a uma loja). |
| **Pedido** | ✏️ `status: String` → `StatusPedido`; ✏️ `valorTotal` → `BigDecimal`; ✏️ `dataPedido` → `LocalDateTime`; ➖ `produtos: Array<Produto>` → composição com `ItemPedido`; ➖ `calcularItem()`, `removerItem()`, `finalizar()`, `acompanhar()`; ➕ associações com `Loja`, `Entrega` (1—1) e `PagamentoBase` (1—0..*); métodos finais: `adicionarItem()`, `calcularTotal()`, `confirmarPagamento()`, `atualizarStatus()`, `cancelar()` | Pedido finalizado não tem item removido. `acompanhar` e `finalizar` são casos de uso (serviço). A transição de status é validada dentro de `Pedido` (regra do objeto). |
| **Entrega** | ✏️ `pedidoId: int` → associação com `Pedido`; ✏️ `dataEntrega: Date` → `LocalDate`; ✏️ `horario: String` → `LocalTime`; ✏️ `tipoRetirada: String` → `TipoRetirada`; ➕ `enderecoEntrega: Endereco` (nulo na retirada), `selecionada`, `cancelada`; mantém `selecionarEntrega()` e `cancelar()` | Reflete o código da atividade de SRP, evoluído para entidade persistente. |
| **Pagamento** | ➖ classe simples com `method(type): type`. ➕ `<<interface>> Pagamento` (`processarPagamento(valor)`, `getTipo()`) ← `PagamentoBase` (`id`, `valor`, `status`, `dataPagamento`) ← `PagamentoPix` (`chavePix`), `PagamentoCartao` (`ultimosDigitos`), `PagamentoBoleto` (`codigoBarras`, `confirmarCompensacao()`) | É o que a atividade de OCP entregou. Detalhe de segurança: só os 4 últimos dígitos do cartão são guardados. |
| **Avaliacao** | ➖ `idCliente: int`; ➕ associações com `Cliente` e **`Loja`**; ✏️ `data: Date` → `dataAvaliacao: LocalDateTime`; ➖ `cadastrar()`, `excluir()`; mantém `editar(nota, comentario)` | Antes não havia como saber *o que* estava sendo avaliado. |

### 1.4 Associações e multiplicidades (conferir todas no diagrama)
```
Usuario ◁── Cliente | Vendedor                  (generalização)
Vendedor  1 ─── 1       Loja                    possui
Loja      1 ─── 0..*    Produto                 oferece
Cliente   1 ─── 0..1    Carrinho                tem
Carrinho  1 ◆── 0..*    ItemCarrinho            (composição)
ItemCarrinho 0..* ──▶ 1 Produto
Carrinho  0..* ──▶ 0..1 Loja                    loja atual
Cliente   1 ─── 0..*    Pedido                  realiza
Loja      1 ─── 0..*    Pedido                  recebe
Pedido    1 ◆── 1..*    ItemPedido              (composição)
ItemPedido 0..* ──▶ 1   Produto
Pedido    1 ◆── 1       Entrega                 (composição)
Pedido    1 ◇── 0..*    PagamentoBase           (agregação)
Cliente   1 ─── 0..*    Avaliacao               escreve
Avaliacao 0..* ──▶ 1    Loja                    avalia
Usuario / Loja / Entrega ◆── 0..1 Endereco      (objeto de valor)
```

### 1.5 Para onde foi cada método removido (útil para defender na apresentação)
| Método que saiu do diagrama | Onde passa a existir (caso de uso) |
|---|---|
| `Usuario.cadastrar()`, `Cliente`/`Vendedor` cadastro | `UsuarioService.cadastrarCliente()` / `cadastrarVendedor()` |
| `Usuario.fazerLogin()` | `AuthService.login()` |
| `Usuario.excluir()` | `UsuarioService.desativar()` |
| `Cliente.gerenciarDados()` | `UsuarioService.atualizarPerfil()` |
| `Cliente.adicionarAoCarrinho()` | `CarrinhoService.adicionarItem()` |
| `Cliente.finalizarCompra()`, `Pedido.finalizar()` | `PedidoService.criar()` |
| `Cliente.acompanharPedido()`, `visualizarInformacoesPedido()`, `Pedido.acompanhar()` | `PedidoService.buscar()` |
| `Cliente.selecionarEntregaRetirada()` | dados de entrega em `PedidoService.criar()` + `Entrega.selecionarEntrega()` |
| `Cliente.avaliarVendedor()`, `Avaliacao.cadastrar()/excluir()` | `AvaliacaoService.criar()` / `excluir()` |
| `Cliente.consultarHistoricoPedido()`, `Vendedor.visualizarPedidos()` | `PedidoService.listar()` |
| `Vendedor.cadastrarProduto()` / `gerenciarProduto()`, `Produto.cadastrar()/excluir()` | `ProdutoService.criar()` / `atualizar()` / `excluir()` |

### 1.6 Diagrama novo: detalhe de OCP (`img/02-classes-ocp-pagamento.png`)
Mostra `PagamentoService → PagamentoCriador ← (Pix | Cartao | Boleto)Criador`. É o diagrama que prova o princípio Aberto/Fechado: **uma nova forma de pagamento não altera `PagamentoService`**.

---

## 2. Diagrama de casos de uso

| # | Ação | Detalhe | Por quê |
|---|---|---|---|
| 1 | ✏️ | Criar o ator abstrato **Usuário**; `Cliente` e `Vendedor` o especializam. Ligar *Cadastrar usuário*, *Fazer login* e *Gerenciar dados da conta* ao Usuário. | No diagrama antigo, três linhas cruzavam todo o desenho até dois atores. A generalização espelha o diagrama de classes e limpa a leitura. |
| 2 | ✏️ | *Gerenciar usuário* → **Gerenciar dados da conta** (editar e desativar) | Nome fiel ao RF005. |
| 3 | ✏️ | *Consultar vendedores próximos* → **Consultar lojas próximas**; *Pesquisar vendedor por nome* → **Pesquisar loja por nome**; *Avaliar vendedor* → **Avaliar loja** | O cliente busca e avalia a loja (classe `Loja`), não a pessoa. Padronizar também nos requisitos. |
| 4 | ➖ | Remover **Realizar pedido** (e o `<<include>>` de *Finalizar compra*) | É o mesmo objetivo de *Finalizar compra*; incluir um no outro era redundante. |
| 5 | ➕ | **Selecionar entrega ou retirada, data e horário** (`<<include>>` de *Finalizar compra*) | RF011 não aparecia no diagrama. |
| 6 | ➕ | **Realizar pagamento** (`<<include>>` de *Finalizar compra*) | RF019. O fluxo de compra não termina sem pagamento. *Nota:* no código são duas chamadas (criar pedido, depois pagar), mas para o usuário é um fluxo contínuo; por isso `include`. |
| 7 | ➖ | Remover *Selecionar produto* e *Selecionar informações do produto* | São passos de tela, não objetivos do ator, e a seta entre eles não tinha estereótipo. |
| 8 | ➕ | **Visualizar produtos da loja** e **Adicionar produto ao carrinho** (ambos do Cliente) | Substituem os dois removidos acima. |
| 9 | ✏️ | Manter **Gerenciar carrinho** (alterar quantidade, remover, limpar) | RF018. |
| 10 | ➕ | **Cancelar pedido** (Cliente) | RF017/RF021; cancela também a entrega. |
| 11 | ➕ | **Gerenciar avaliação** (editar/excluir) além de *Avaliar loja* | RF022. |
| 12 | ✏️ | *Consultar pedidos* → **Consultar pedidos da loja** | Deixa claro o escopo do vendedor (RF013). |
| 13 | ➕ | **Gerenciar perfil da loja** (Vendedor) | RF020; estava nos objetivos do projeto e faltava. |
| 14 | ➕ | **Confirmar pagamento de boleto** (Vendedor) | RF024; simula a compensação bancária. |
| 15 | ✏️ | Corrigir grafia: "Gerênciar" → **Gerenciar**; acentuar "informações", "Atualizar" etc. | Acabamento (ABNT). |

**Relacionamentos `<<include>>`/`<<extend>>` finais:** apenas *Finalizar compra* inclui *Selecionar entrega...* e *Realizar pagamento*. Os demais casos de uso são independentes.

---

## 3. Diagramas de atividades

### 3.1 Realizar Pedido (`img/04-...`)
| Ação | Detalhe | Por quê |
|---|---|---|
| ✏️ | A validação passa a checar **carrinho não vazio, produtos ainda ativos e data/horário** (não pode ser passado e deve estar dentro do funcionamento da loja) | São as regras implementadas em `PedidoService`. |
| ✏️ | "Registra o pedido" → "Registra o pedido (**AGUARDANDO_PAGAMENTO**) e a entrega" | O pedido nasce nesse estado. |
| ➕ | **Esvazia o carrinho** | O carrinho vira pedido. |
| ➕ | **Exibe o resumo do pedido (checkout)** | RF012. |
| ➕ | Cliente **escolhe a forma de pagamento**; Sistema **processa o pagamento**; decisão **"pagamento aprovado?"** (sim → CONFIRMADO; não/boleto → continua AGUARDANDO_PAGAMENTO) | O diagrama antigo registrava e confirmava a compra sem pagamento. |
| ✏️ | O erro de validação volta ao início do preenchimento (loop completo) | No antigo, a seta pulava direto para "Finaliza a compra", sem permitir corrigir. |

### 3.2 Acompanhar Pedido (`img/05-...`): corrige erros do original
| Ação | Detalhe | Por quê |
|---|---|---|
| ✏️ | Ordem: **buscar → decidir "encontrado?" → só então verificar status** | No antigo, o status era verificado antes de saber se o pedido existia. |
| ✏️ | "Exibe mensagem de erro" passa para a raia do **Sistema** | Estava na raia do Cliente. |
| ➖ | Remover o nó "Tenta Novamente" e a seta duplicada do erro | Era uma ação sem significado e havia duas setas saindo do mesmo erro. Substituído por um loop com a decisão. |
| ✏️ | "**Retoma** Informações" → "**Retorna** status, itens, entrega e pagamento" | Erro de digitação e conteúdo completo (RF010/RF012). |
| ✏️ | A busca considera só pedidos **do próprio cliente** | Regra de segurança implementada (403 para pedido alheio). |

### 3.3 Cadastrar Produto (`img/06-...`): corrige erros do original
| Ação | Detalhe | Por quê |
|---|---|---|
| ✏️ | Decisão **"Pedido Encontrado?" → "Dados válidos?"** | O rótulo foi copiado do outro diagrama; está errado nesse contexto. |
| ✏️ | Campos: nome, foto, descrição, preço **e "permite personalização"** | RF008 cita personalização e o diagrama a omitia. |
| ✏️ | "Salva o produto no sistema" → "Salva o produto **na loja do vendedor**" | Todo produto pertence a uma loja. |

### 3.4 Realizar Pagamento (➕ novo, `img/07-...`)
Mostra as três ramificações (Pix exige chave; cartão exige 4 últimos dígitos; boleto gera código), o resultado **aprovado** (pedido → CONFIRMADO) e o caminho **boleto pendente → loja confirma compensação**. É o diagrama de atividades que demonstra o OCP.

### 3.5 Estados do Pedido (➕ novo e opcional, `img/08-...`)
Não é exigido pelo enunciado, mas é o diagrama mais fácil de apresentar para explicar `RF010` e `RF015`: mostra as transições válidas (ex.: retirada nunca vai para "saiu para entrega"; cliente só cancela até CONFIRMADO).

---

## 4. Checklist para o documento ABNT
- [ ] Inserir cada figura com **legenda e fonte** ("Fonte: os autores, 2026") e referência no texto.
- [ ] Trocar todos os diagramas antigos pelos novos (ou pelos redesenhados a partir deles).
- [ ] No capítulo de metodologia, incluir a tabela 1.5 (de onde vieram os métodos) e o diagrama de OCP como evidência de SOLID.
- [ ] Citar no texto que Pix/cartão/boleto são **simulados** (limitação declarada, não falha oculta).
- [ ] Atualizar `docs/requisitos/Requisitos_Atualizados.md` no documento (tabelas de RF/RNF, regras de negócio, matriz de rastreabilidade).

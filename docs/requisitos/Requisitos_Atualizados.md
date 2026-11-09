# IFlower - Requisitos atualizados (alinhados ao backend implementado)

Legenda de situação em relação ao levantamento de 18/09: **Mantido** · **Alterado** · **Novo** · **Removido**.

## 1. Visão geral do sistema (texto pronto para a Seção 1 do documento)

O IFlower é uma plataforma de venda e acompanhamento de pedidos de floriculturas. O objetivo é aproximar clientes e floriculturas em um canal digital, já que esse tipo de comércio ainda é pouco presente no ambiente online. Os **usuários** são de dois perfis: o **cliente**, que busca lojas, monta um carrinho, escolhe entrega ou retirada, paga e acompanha o pedido; e o **vendedor**, dono de uma floricultura, que mantém o perfil da loja e o catálogo, recebe pedidos e atualiza seus estados. A **necessidade** que justifica o sistema é a dificuldade de encomendar flores com data e horário definidos e de saber em que etapa está o pedido. O **impacto esperado** é maior alcance e organização para as lojas e conveniência e transparência para o cliente. O sucesso da solução é medido pela capacidade de concluir uma compra em poucos passos (RNF001) e de acompanhar o pedido até a entrega.

## 2. Requisitos funcionais

| ID | Descrição | Prioridade | Situação |
|---|---|---|---|
| RF001 | O sistema deve permitir o cadastro de **cliente** com nome, email, senha, telefone, endereço e CPF válido. Email e CPF não podem se repetir. | Alta | Alterado (+ senha, CPF) |
| RF002 | O sistema deve permitir o login com email e senha, devolvendo um token de acesso com expiração. | Alta | Alterado (detalhado) |
| RF003 | O sistema deve permitir que o cliente avalie uma loja (nota de 1 a 5 e comentário), **somente após ter recebido um pedido dela**, uma avaliação por loja. | Média | Alterado ("pode" → "deve" + regras) |
| RF004 | O sistema deve listar as **lojas próximas** ao cliente (mesma cidade/UF do endereço cadastrado). | Alta | Alterado (critério definido) |
| RF005 | O sistema deve permitir que o usuário consulte e edite seus dados (nome, telefone, endereço) e desative a conta. | Alta | Alterado |
| RF006 | O sistema deve permitir adicionar produtos ao carrinho, com quantidade e, quando o produto permitir, texto de personalização. | Alta | Alterado |
| RF007 | O sistema deve permitir a pesquisa de **lojas** por nome (busca parcial), com filtro opcional de cidade e UF. | Média | Alterado (vendedor → loja) |
| RF008 | O sistema deve permitir que o vendedor cadastre produto com nome, foto, descrição, preço e indicação de **se aceita personalização**. | Alta | Alterado |
| RF009 | O sistema deve permitir que o cliente finalize a compra, transformando o carrinho em pedido. | Alta | Mantido (absorve o antigo RF017) |
| RF010 | O sistema deve permitir que o cliente acompanhe o status do seu pedido. | Alta | Mantido |
| RF011 | O sistema deve permitir escolher entre **entrega** e **retirada na loja**, com data e horário (não podem estar no passado e devem respeitar o horário de funcionamento da loja). | Alta | Alterado (+ validações) |
| RF012 | O sistema deve exibir todas as informações do pedido (itens, valores, entrega, pagamento e total) no checkout. | Alta | Alterado (+ pagamento) |
| RF013 | O sistema deve permitir que o vendedor consulte os pedidos da sua loja, com filtro por status. | Alta | Mantido |
| RF014 | O sistema deve permitir que o cliente veja seu histórico de pedidos. | Média | Mantido |
| RF015 | O sistema deve permitir que o vendedor atualize o status do pedido, respeitando o fluxo válido de estados. | Alta | Alterado |
| RF016 | O sistema deve permitir que o vendedor gerencie seus produtos (listar, editar e excluir; a exclusão é lógica). | Alta | Alterado |
| RF017 | O sistema deve permitir que o cliente **cancele o pedido** enquanto ele estiver aguardando pagamento ou confirmado. | Alta | Alterado (substitui o antigo "realizar pedidos", duplicado do RF009) |
| RF018 | O sistema deve permitir gerenciar o carrinho (alterar quantidade, remover item, esvaziar e ver o total). O carrinho só aceita produtos de uma loja. | Alta | Novo |
| RF019 | O sistema deve permitir o pagamento do pedido por **Pix, cartão ou boleto** e registrar o resultado. | Alta | Novo |
| RF020 | O sistema deve permitir que o vendedor gerencie o perfil da loja (nome, descrição, endereço e horário de funcionamento). | Média | Novo |
| RF021 | O sistema deve cancelar a entrega associada quando o pedido for cancelado. | Média | Novo |
| RF022 | O sistema deve permitir que o cliente edite e exclua sua avaliação. | Baixa | Novo |
| RF023 | O sistema deve permitir o cadastro de **vendedor** com CNPJ válido e dados da loja. | Alta | Novo |
| RF024 | O sistema deve permitir que o vendedor confirme a compensação de um boleto. | Baixa | Novo (simulação) |
| RF025 | O sistema deve permitir visualizar a loja (com nota média) e o catálogo de produtos ativos. | Alta | Novo |

**Removido:** o RF "notificar mudança de status" sugerido anteriormente **não foi implementado**; se constar no documento, apresentar como trabalho futuro.

## 3. Requisitos não funcionais

| ID | Descrição | Categoria | Situação |
|---|---|---|---|
| RNF001 | O cliente pode realizar uma compra em até 5 cliques. | Usabilidade | Mantido (verificado no app) |
| RNF002 | Todos os botões devem conter rótulo descritivo. | Acessibilidade | Alterado (categoria corrigida) |
| RNF003 | A busca de lojas deve responder em até 2 segundos (índices por cidade/UF no banco). | Desempenho | Mantido |
| RNF004 | O aplicativo deve funcionar em Android e iOS (React Native); versão web é opcional. | Portabilidade | Alterado (categoria e escopo) |
| RNF005 | Senhas armazenadas com BCrypt; rotas protegidas por token JWT e por perfil (cliente/vendedor). | Segurança | Novo |
| RNF006 | Validação de dados de entrada (email, CPF, CNPJ, CEP, quantidades, notas). | Integridade | Novo |
| RNF007 | Criação do pedido, pagamento e mudança de status devem ocorrer em transação única. | Consistência | Novo |
| RNF008 | O backend deve seguir arquitetura em camadas (MVC) e princípios SOLID. | Manutenibilidade | Novo |

## 4. Regras de negócio
| ID | Regra |
|---|---|
| RN01 | O carrinho só pode conter produtos de **uma única loja**. |
| RN02 | Produto só aceita personalização se `permitePersonalizacao = true`; itens iguais com a mesma personalização são somados. |
| RN03 | O pedido guarda o **preço do produto no momento da compra**. |
| RN04 | Fluxo de status: AGUARDANDO_PAGAMENTO → CONFIRMADO → EM_PREPARO → (SAIU_PARA_ENTREGA ou PRONTO_PARA_RETIRADA) → ENTREGUE; CANCELADO a partir de AGUARDANDO_PAGAMENTO, CONFIRMADO e EM_PREPARO. |
| RN05 | A confirmação do pedido acontece **automaticamente** quando o pagamento é aprovado; o vendedor não confirma manualmente. |
| RN06 | Retirada na loja não pode "sair para entrega", e entrega não pode ficar "pronta para retirada". |
| RN07 | O cliente só cancela até CONFIRMADO; o vendedor pode cancelar até EM_PREPARO. Cancelar o pedido cancela a entrega. |
| RN08 | Data e horário da entrega não podem estar no passado e devem estar dentro do horário da loja (quando definido). |
| RN09 | Só pode avaliar a loja quem tem pedido ENTREGUE nela; uma avaliação por cliente e loja. |
| RN10 | Produtos e usuários têm **exclusão lógica**, preservando o histórico. |
| RN11 | Um pedido só aceita novo pagamento se não houver outro aprovado ou pendente. Pix e cartão são aprovados na hora (simulação); boleto fica pendente até a confirmação. |
| RN12 | Um vendedor possui exatamente uma loja. |
| RN13 | Cliente só enxerga seus pedidos; vendedor só os da sua loja. |

## 5. Glossário
**Loja:** estabelecimento do vendedor (CNPJ, endereço, horário). **Pedido:** compra finalizada a partir do carrinho. **Entrega:** agendamento (data, horário, tipo) do pedido; pode ser retirada na loja. **Personalização:** texto livre (ex.: mensagem de cartão) escolhido pelo cliente em produtos que a permitem. **Exclusão lógica:** marcar como inativo em vez de apagar.

## 6. Matriz de rastreabilidade (RF → caso de uso → API → classes → tabelas)
| RF | Caso de uso | Endpoint | Classes principais | Tabelas |
|---|---|---|---|---|
| 001, 023 | Cadastrar usuário | `POST /api/clientes`, `/api/vendedores` | `UsuarioService`, `Cliente`, `Vendedor`, `Loja` | usuario, cliente, vendedor, loja |
| 002 | Fazer login | `POST /api/auth/login` | `AuthService`, `JwtService` | usuario |
| 005 | Gerenciar dados da conta | `GET/PUT/DELETE /api/usuarios/me` | `UsuarioService`, `Usuario` | usuario |
| 004, 007, 025 | Consultar/pesquisar loja, ver produtos | `GET /api/lojas`, `/proximas`, `/{id}`, `/{id}/produtos` | `LojaService`, `ProdutoService` | loja, produto, avaliacao |
| 006, 018 | Adicionar ao carrinho / Gerenciar carrinho | `/api/carrinho/**` | `CarrinhoService`, `Carrinho`, `ItemCarrinho` | carrinho, item_carrinho |
| 009, 011, 012 | Finalizar compra | `POST /api/pedidos` | `PedidoService`, `Pedido`, `ItemPedido`, `Entrega` | pedido, item_pedido, entrega |
| 019 | Realizar pagamento | `POST /api/pedidos/{id}/pagamento` | `PagamentoService`, `PagamentoCriador`, `PagamentoBase` | pagamento |
| 024 | Confirmar pagamento de boleto | `POST /api/pagamentos/{id}/confirmar` | `PagamentoBoleto` | pagamento |
| 010, 014 | Acompanhar pedido / histórico | `GET /api/pedidos`, `/{id}` | `PedidoService` | pedido |
| 013 | Consultar pedidos da loja | `GET /api/pedidos?status=` | `PedidoService` | pedido |
| 015 | Atualizar status | `PATCH /api/pedidos/{id}/status` | `Pedido.atualizarStatus`, `StatusPedido` | pedido |
| 017, 021 | Cancelar pedido | `POST /api/pedidos/{id}/cancelar` | `Pedido.cancelar`, `Entrega.cancelar` | pedido, entrega |
| 008, 016 | Cadastrar / gerenciar produtos | `/api/produtos/**` | `ProdutoService`, `Produto` | produto |
| 020 | Gerenciar perfil da loja | `GET/PUT /api/lojas/me` | `LojaService`, `Loja` | loja |
| 003, 022 | Avaliar loja / gerenciar avaliação | `/api/lojas/{id}/avaliacoes`, `/api/avaliacoes/{id}` | `AvaliacaoService`, `Avaliacao` | avaliacao |

## 7. Especificação dos casos de uso principais
Formato: **Ator · Pré-condição · Fluxo principal · Fluxos alternativos · Pós-condição.**

**UC01 - Cadastrar usuário (RF001, RF023)**
Ator: Usuário (visitante). Pré: não possuir conta com o mesmo email/CPF/CNPJ.
Fluxo: 1) escolhe cliente ou vendedor; 2) informa dados (vendedor também CNPJ e dados da loja); 3) sistema valida formato e documentos; 4) grava com senha criptografada; 5) confirma o cadastro.
Alternativos: A1) dados inválidos → mensagem por campo; A2) email/CPF/CNPJ já cadastrado → erro de conflito.
Pós: usuário cadastrado (e loja criada, no caso de vendedor).

**UC02 - Fazer login (RF002)**
Ator: Usuário. Pré: conta ativa.
Fluxo: 1) informa email e senha; 2) sistema valida; 3) devolve token com expiração; 4) app guarda o token.
Alternativo: A1) credenciais inválidas → mesma mensagem para email inexistente e senha errada.
Pós: usuário autenticado.

**UC09 - Finalizar compra (RF009, RF011, RF012)**
Ator: Cliente. Pré: autenticado; carrinho com itens.
Fluxo: 1) revisa itens e quantidades; 2) escolhe entrega ou retirada, data e horário (inclui UC10); 3) confirma; 4) sistema valida e registra o pedido AGUARDANDO_PAGAMENTO com a entrega; 5) esvazia o carrinho; 6) exibe o resumo; 7) segue para o pagamento (inclui UC11).
Alternativos: A1) carrinho vazio; A2) produto inativo; A3) data/horário no passado ou fora do funcionamento; A4) entrega sem endereço → erro de regra de negócio, o cliente corrige.
Pós: pedido criado, carrinho vazio.

**UC11 - Realizar pagamento (RF019)**
Ator: Cliente. Pré: pedido AGUARDANDO_PAGAMENTO sem pagamento aprovado ou pendente.
Fluxo: 1) escolhe Pix, cartão ou boleto; 2) informa o dado exigido (chave Pix / 4 últimos dígitos / nada); 3) sistema processa; 4) pagamento aprovado → pedido CONFIRMADO.
Alternativos: A1) dado obrigatório ausente; A2) boleto → pagamento PENDENTE e o pedido segue aguardando até UC22.
Pós: pagamento registrado.

**UC12 - Acompanhar pedido (RF010)**
Ator: Cliente. Pré: autenticado.
Fluxo: 1) acessa os pedidos; 2) seleciona um; 3) sistema exibe status, itens, entrega e pagamento.
Alternativos: A1) pedido inexistente → erro; A2) pedido de outro cliente → acesso negado.

**UC14 - Cancelar pedido (RF017, RF021)**
Ator: Cliente. Pré: pedido AGUARDANDO_PAGAMENTO ou CONFIRMADO.
Fluxo: 1) solicita o cancelamento; 2) sistema cancela pedido e entrega.
Alternativo: A1) status posterior (ex.: EM_PREPARO) → não permitido.
Pós: pedido CANCELADO.

**UC17 - Cadastrar produto (RF008)**
Ator: Vendedor. Pré: autenticado, loja cadastrada.
Fluxo: 1) informa nome, foto, descrição, preço e se permite personalização; 2) sistema valida; 3) salva na loja; 4) confirma.
Alternativo: A1) dados inválidos (ex.: preço ≤ 0) → mensagem e nova tentativa.

**UC20 - Atualizar status do pedido (RF015)**
Ator: Vendedor. Pré: pedido da sua loja.
Fluxo: 1) seleciona o pedido; 2) escolhe o próximo status; 3) sistema valida a transição (RN04, RN06) e atualiza.
Alternativos: A1) transição inválida; A2) tentar CONFIRMADO manualmente (automático pelo pagamento); A3) pedido de outra loja → acesso negado.

**UC22 - Confirmar pagamento de boleto (RF024)**
Ator: Vendedor. Pré: pagamento BOLETO PENDENTE de pedido da sua loja.
Fluxo: 1) seleciona o pagamento; 2) confirma a compensação; 3) sistema aprova o pagamento e atualiza o pedido para CONFIRMADO.

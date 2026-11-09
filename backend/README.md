# IFlower - Backend

API REST em Java 17 + Spring Boot 3.3 (Spring Web, Spring Data JPA, Spring Security + JWT, Bean Validation) com PostgreSQL.

## Como rodar
```bash
docker compose up -d          # na raiz: sobe o PostgreSQL e executa database/01_schema.sql e 02_seed.sql
cd backend
mvn spring-boot:run           # http://localhost:8080
mvn test                      # testes unitários (não precisam de banco)
```
Variáveis opcionais: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET` (mín. 32 caracteres).
Usuários de teste (senha `Senha@123`): `maria@iflower.com` e `joao@iflower.com` (clientes);
`ana@iflower.com`, `carlos@iflower.com` e `beatriz@iflower.com` (vendedores).
Roteiro completo de chamadas em [`requests.http`](requests.http).

## Camadas (MVC)
| Camada | Pacote | Responsabilidade |
|---|---|---|
| Controller | `controller` | HTTP: recebe a requisição, valida (`@Valid`), delega ao service |
| Service | `service` | Regras de negócio, transações, autorização por posse do recurso |
| Repository | `repository` | Acesso a dados (Spring Data JPA) |
| Model | `model` | Entidades com as regras do próprio objeto |
| DTO | `dto` | Contratos de entrada e saída (records); a entidade nunca vai direto para o JSON |
| Cross-cutting | `security`, `exception`, `validation`, `util`, `config` | JWT, erros padronizados, CPF/CNPJ, CORS |

## Endpoints
Legenda: 🌐 público · 👤 cliente · 🏪 vendedor · 🔑 qualquer autenticado

| Método e rota | Acesso | RF | Descrição |
|---|---|---|---|
| `POST /api/clientes` | 🌐 | 001 | Cadastro de cliente |
| `POST /api/vendedores` | 🌐 | 001 | Cadastro de vendedor + loja |
| `POST /api/auth/login` | 🌐 | 002 | Login, devolve o token JWT |
| `GET/PUT/DELETE /api/usuarios/me` | 🔑 | 005 | Ver, editar e desativar a própria conta |
| `GET /api/lojas?nome=&cidade=&uf=` | 🌐 | 007 | Busca de lojas |
| `GET /api/lojas/proximas` | 👤 | 004 | Lojas da cidade do cliente |
| `GET /api/lojas/{id}` · `/{id}/produtos` | 🌐 | | Loja (com nota média) e seu catálogo |
| `GET/PUT /api/lojas/me` | 🏪 | 020 | Perfil da própria loja |
| `POST /api/produtos` · `PUT/DELETE /api/produtos/{id}` · `GET /api/produtos/me` | 🏪 | 008, 016 | Gerenciar produtos |
| `GET /api/produtos/{id}` | 🌐 | | Detalhe do produto |
| `GET/DELETE /api/carrinho` · `POST /api/carrinho/itens` · `PUT/DELETE /api/carrinho/itens/{id}` | 👤 | 006, 018 | Carrinho |
| `POST /api/pedidos` | 👤 | 009, 011, 012, 017 | Finaliza a compra (carrinho → pedido + entrega) |
| `GET /api/pedidos?status=` · `GET /api/pedidos/{id}` | 🔑 | 010, 013, 014 | Histórico (cliente) / pedidos da loja (vendedor) |
| `PATCH /api/pedidos/{id}/status` | 🏪 | 015 | Atualiza o status |
| `POST /api/pedidos/{id}/cancelar` | 👤 | 021 | Cancela pedido e entrega |
| `POST /api/pedidos/{id}/pagamento` | 👤 | 019 | Paga (PIX, CARTAO ou BOLETO) |
| `POST /api/pagamentos/{id}/confirmar` | 🏪 | 019 | Confirma a compensação de um boleto (simulação) |
| `POST/GET /api/lojas/{id}/avaliacoes` · `PUT/DELETE /api/avaliacoes/{id}` | 👤 / 🌐 | 003, 022 | Avaliações |

Erros seguem sempre o formato `{ timestamp, status, mensagem, campos? }`:
400 validação · 401 sem login · 403 sem permissão · 404 não encontrado · 409 conflito · 422 regra de negócio.

## Regras de negócio implementadas
- O carrinho aceita produtos de **uma única loja**.
- Personalização só em produtos que a permitem; itens iguais com a mesma personalização são somados.
- O pedido guarda o **preço do momento** da compra (`item_pedido.preco_unitario`).
- Fluxo de status: `AGUARDANDO_PAGAMENTO → CONFIRMADO → EM_PREPARO → (SAIU_PARA_ENTREGA | PRONTO_PARA_RETIRADA) → ENTREGUE`; `CANCELADO` a partir dos três primeiros.
  A confirmação é automática quando o pagamento é aprovado. Retirada na loja nunca "sai para entrega" e vice-versa.
- O cliente só cancela até `CONFIRMADO`; cancelar o pedido cancela a entrega.
- Data/horário de entrega não podem estar no passado e respeitam o horário de funcionamento da loja.
- Só avalia a loja quem já teve um pedido `ENTREGUE` nela; uma avaliação por cliente/loja (editável).
- Produtos e usuários têm **exclusão lógica**, preservando o histórico.
- Criação do pedido, pagamento e mudança de status ocorrem em **uma transação** (RNF007).

## SOLID no código
- **SRP:** `Entrega` só controla o próprio estado; persistência, orquestração e autorização ficam nos services.
- **OCP:** `service/pagamento/PagamentoCriador`. Para uma nova forma de pagamento (ex.: débito):
  1. adicionar o valor em `TipoPagamento`; 2. criar a entidade `PagamentoDebito extends PagamentoBase`;
  3. criar `DebitoCriador implements PagamentoCriador` (`@Component`). **`PagamentoService` não muda.**

## Simplificações assumidas (a validar com o professor/grupo)
- Pix, cartão e boleto são **simulados** (sem gateway). Pix/cartão aprovam na hora; boleto fica pendente até `/confirmar`.
- "Lojas próximas" = mesma cidade/UF do cliente (sem geolocalização).
- Um vendedor possui **uma** loja.
- Reembolso ao cancelar um pedido já pago não é tratado.

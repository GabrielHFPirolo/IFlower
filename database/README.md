# Banco de dados (PostgreSQL)

Scripts executados automaticamente na primeira subida do container
(`docker compose up -d`), em ordem alfabética:

| Arquivo         | Conteúdo                                   |
|-----------------|--------------------------------------------|
| `01_schema.sql` | DDL das tabelas, chaves e restrições       |
| `02_seed.sql`   | Dados de exemplo para teste e apresentação |

Para recriar do zero: `docker compose down -v && docker compose up -d`.
O DER (imagem ou .drawio) fica em `database/der/`.

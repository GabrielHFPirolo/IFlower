# IFlower

Serviço de venda e acompanhamento de pedidos de floriculturas.
Projeto final de POO2 (EC46A).

**Integrantes:** Arthur Vaz Budny · Gabriel Henrique Ferreira Pirolo · Pedro Ian Basílio Machado

## Tecnologias
- **Backend:** Java 17, Spring Boot 3 (arquitetura em camadas / MVC)
- **Frontend:** React Native, TypeScript (Expo)
- **Banco:** PostgreSQL 16

## Estrutura
```
iflower/
├── backend/    API REST (Spring Boot)
├── mobile/     App React Native
├── database/   Scripts SQL e DER
├── docs/       Requisitos, UML, documento ABNT, apresentação
└── docker-compose.yml
```

## Como rodar
```bash
docker compose up -d                  # sobe o PostgreSQL
cd backend && mvn spring-boot:run     # API em http://localhost:8080
cd mobile && npm install && npx expo start
```

## Arquitetura do backend (MVC em camadas)
```
com.iflower
├── controller/   recebe requisições HTTP
├── service/      regras de negócio
├── repository/   acesso a dados (Spring Data JPA)
├── model/        entidades de domínio (M)
│   ├── pagamento/  OCP: Pagamento, PagamentoBase, Pix, Cartão, Boleto
│   └── enums/
├── dto/          objetos de entrada e saída da API
├── exception/    exceções e tratamento global
├── config/       configurações (CORS, segurança)
└── util/
```
No MVC com API REST, a **View** é o app React Native; o backend entrega JSON via DTOs.

## Princípios SOLID já aplicados
- **SRP:** `model/Entrega.java`
- **OCP:** `model/pagamento/*`

## Fluxo de trabalho Git
- `main`: somente código estável (entregas)
- `develop`: integração
- `feature/<area>-<descricao>`: uma por tarefa (ex.: `feature/backend-cadastro-usuario`)
- Commits em português, no imperativo: `Adiciona cadastro de usuário`
- Pull Request para `develop` com revisão de outro integrante

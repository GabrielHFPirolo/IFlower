# IFlower Mobile (React Native + TypeScript)

Criar o projeto Expo dentro desta pasta (uma vez, por um integrante):

```bash
cd mobile
npx create-expo-app@latest . --template blank-typescript
```

Depois organizar o código em `src/`:

| Pasta          | Responsabilidade                         |
|----------------|------------------------------------------|
| `screens/`     | Telas (View)                             |
| `components/`  | Componentes reutilizáveis                |
| `services/`    | Chamadas à API REST do backend           |
| `navigation/`  | Rotas e navegação                        |
| `hooks/`       | Hooks customizados                       |
| `types/`       | Interfaces TypeScript (espelham os DTOs) |
| `theme/`       | Cores, fontes e espaçamentos             |

A URL base da API fica em um único lugar (`src/services/api.ts`).

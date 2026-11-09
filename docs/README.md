# Documentação

| Pasta / arquivo | Conteúdo |
|---|---|
| `uml/ALTERACOES_UML.md` | **Lista definitiva do que adicionar, alterar e remover em cada diagrama, com o porquê** |
| `uml/img/` | Diagramas de referência já corrigidos (PNG) |
| `uml/src/` | Fontes editáveis dos diagramas (PlantUML) |
| `requisitos/Requisitos_Atualizados.md` | RF/RNF finais, regras de negócio, glossário, matriz de rastreabilidade, especificação dos casos de uso e texto da Visão Geral |
| `requisitos/` | Levantamento da fase 2 (PDF original) |
| `abnt/` | Modelo .docx e documento final em ABNT |
| `apresentacao/` | Slides e roteiro (12/11, 13/11 e 19/11) |

Para regenerar as imagens: `plantuml -tpng -charset UTF-8 -o ../img src/*.puml` (dentro de `docs/uml`).

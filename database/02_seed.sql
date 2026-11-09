-- =====================================================================
-- IFlower - dados de exemplo (senha de TODOS os usuários: Senha@123)
-- Hash gerado com pgcrypto (bcrypt $2a$), compatível com o BCryptPasswordEncoder do Spring.
-- =====================================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO usuario (id, nome, email, senha_hash, telefone, cep, logradouro, numero, bairro, cidade, uf) VALUES
 (1, 'Maria Souza',   'maria@iflower.com',   crypt('Senha@123', gen_salt('bf', 10)), '41999990001', '80010000', 'Rua XV de Novembro', '100', 'Centro',    'Curitiba', 'PR'),
 (2, 'João Pereira',  'joao@iflower.com',    crypt('Senha@123', gen_salt('bf', 10)), '41999990002', '80420000', 'Av. Batel',          '500', 'Batel',     'Curitiba', 'PR'),
 (3, 'Ana Lima',      'ana@iflower.com',     crypt('Senha@123', gen_salt('bf', 10)), '41999990003', '80060000', 'Rua Mateus Leme',   '250', 'São Francisco', 'Curitiba', 'PR'),
 (4, 'Carlos Mendes', 'carlos@iflower.com',  crypt('Senha@123', gen_salt('bf', 10)), '43999990004', '86010000', 'Av. Higienópolis',  '900', 'Centro',    'Londrina', 'PR'),
 (5, 'Beatriz Rocha', 'beatriz@iflower.com', crypt('Senha@123', gen_salt('bf', 10)), '41999990005', '80250000', 'Rua Bom Jesus',     '75',  'Água Verde','Curitiba', 'PR');

INSERT INTO cliente (id, cpf) VALUES (1, '52998224725'), (2, '11144477735');
INSERT INTO vendedor (id) VALUES (3), (4), (5);

INSERT INTO loja (id, vendedor_id, nome, descricao, cnpj, cep, logradouro, numero, bairro, cidade, uf, abertura, fechamento) VALUES
 (1, 3, 'Floricultura Primavera', 'Buquês e arranjos para todas as ocasiões.', '11222333000181', '80060000', 'Rua Mateus Leme',  '250', 'São Francisco', 'Curitiba', 'PR', '08:00', '18:00'),
 (2, 4, 'Jardim das Rosas',       'Especialistas em rosas e presentes.',       '11444777000161', '86010000', 'Av. Higienópolis', '900', 'Centro',        'Londrina', 'PR', '09:00', '19:00'),
 (3, 5, 'Casa das Orquídeas',     'Orquídeas, vasos e plantas ornamentais.',   '45723174000110', '80250000', 'Rua Bom Jesus',    '75',  'Água Verde',    'Curitiba', 'PR', '08:30', '17:30');

INSERT INTO produto (id, loja_id, nome, foto, descricao, preco, permite_personalizacao) VALUES
 (1, 1, 'Buquê de Rosas Vermelhas', 'https://exemplo.com/fotos/buque-rosas.jpg', '12 rosas vermelhas com folhagem.', 129.90, TRUE),
 (2, 1, 'Cesta de Café da Manhã Floral', 'https://exemplo.com/fotos/cesta.jpg', 'Cesta com flores, frutas e pães.', 189.90, TRUE),
 (3, 1, 'Girassóis no Vaso', 'https://exemplo.com/fotos/girassois.jpg', 'Seis girassóis em vaso de cerâmica.', 99.90, FALSE),
 (4, 2, 'Rosas Brancas (24 un.)', 'https://exemplo.com/fotos/rosas-brancas.jpg', 'Duas dúzias de rosas brancas.', 249.90, TRUE),
 (5, 2, 'Arranjo Rosa e Lírio', 'https://exemplo.com/fotos/rosa-lirio.jpg', 'Arranjo misto em base de vidro.', 159.90, FALSE),
 (6, 3, 'Orquídea Phalaenopsis', 'https://exemplo.com/fotos/orquidea.jpg', 'Orquídea branca em vaso decorado.', 119.90, TRUE),
 (7, 3, 'Kit Suculentas', 'https://exemplo.com/fotos/suculentas.jpg', 'Três suculentas em vasos de cimento.', 69.90, FALSE);

-- Pedido já ENTREGUE (permite testar avaliação de loja logo de início)
INSERT INTO pedido (id, cliente_id, loja_id, data_pedido, status, valor_total) VALUES
 (1, 1, 1, '2026-10-20 10:30:00', 'ENTREGUE', 129.90);
INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, personalizacao) VALUES
 (1, 1, 1, 1, 129.90, 'Cartão: Feliz aniversário!');
INSERT INTO entrega (id, pedido_id, data_entrega, horario, tipo_retirada, cep, logradouro, numero, bairro, cidade, uf, selecionada, cancelada) VALUES
 (1, 1, '2026-10-21', '14:00', 'ENTREGA', '80010000', 'Rua XV de Novembro', '100', 'Centro', 'Curitiba', 'PR', TRUE, FALSE);
INSERT INTO pagamento (id, pedido_id, tipo, valor, status, data_pagamento, chave_pix) VALUES
 (1, 1, 'PIX', 129.90, 'APROVADO', '2026-10-20 10:31:00', 'maria@iflower.com');
INSERT INTO avaliacao (id, cliente_id, loja_id, nota, comentario, data_avaliacao) VALUES
 (1, 1, 1, 5, 'Flores lindas e entrega no horário!', '2026-10-22 09:00:00');

-- Sincroniza as sequências (ids foram inseridos explicitamente)
SELECT setval(pg_get_serial_sequence('usuario', 'id'),   (SELECT MAX(id) FROM usuario));
SELECT setval(pg_get_serial_sequence('loja', 'id'),      (SELECT MAX(id) FROM loja));
SELECT setval(pg_get_serial_sequence('produto', 'id'),   (SELECT MAX(id) FROM produto));
SELECT setval(pg_get_serial_sequence('pedido', 'id'),    (SELECT MAX(id) FROM pedido));
SELECT setval(pg_get_serial_sequence('item_pedido', 'id'), (SELECT MAX(id) FROM item_pedido));
SELECT setval(pg_get_serial_sequence('entrega', 'id'),   (SELECT MAX(id) FROM entrega));
SELECT setval(pg_get_serial_sequence('pagamento', 'id'), (SELECT MAX(id) FROM pagamento));
SELECT setval(pg_get_serial_sequence('avaliacao', 'id'), (SELECT MAX(id) FROM avaliacao));

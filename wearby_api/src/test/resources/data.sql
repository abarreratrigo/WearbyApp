MERGE INTO categoria (id, nombre) KEY(id)
    VALUES (1, 'Camiseta'), (2, 'Pantalón'), (3, 'Zapatos');

MERGE INTO color (id, nombre) KEY(id)
    VALUES (1, 'Blanco'), (2, 'Negro');

MERGE INTO estilo (id, nombre) KEY(id)
    VALUES (1, 'Casual'), (2, 'Formal');

MERGE INTO formalidad (id, nombre) KEY(id)
    VALUES (1, 'Informal'), (2, 'Formal');

MERGE INTO temporada (id, nombre) KEY(id)
    VALUES (1, 'Verano'), (2, 'Invierno'), (5, 'Todo el año');

MERGE INTO usuario (id, nombre, email, contrasena, rol, activo) KEY(id)
    VALUES (1, 'Test Usuario', 'test@wearby.com',
    '$2a$10$9srtWUpzhUgZwO5jlNeJ1uAb84Ow/2hMBlN54KRIWWNXxWLkj9AjK',
    'usuario', true);

MERGE INTO usuario (id, nombre, email, contrasena, rol, activo) KEY(id)
    VALUES (2, 'Administrador', 'admin@wearby.com',
    '$2a$10$9srtWUpzhUgZwO5jlNeJ1uAb84Ow/2hMBlN54KRIWWNXxWLkj9AjK',
    'admin', true);

MERGE INTO prenda (id, id_usuario, nombre, imagen_url, id_categoria, id_color,
    id_estilo, id_formalidad, id_temporada, notas, favorito) KEY(id)
    VALUES (1, 1, 'Camiseta blanca', 'uploads/test.jpg', 1, 1, 1, 1, 5, null, false);

MERGE INTO prenda (id, id_usuario, nombre, imagen_url, id_categoria, id_color,
    id_estilo, id_formalidad, id_temporada, notas, favorito) KEY(id)
    VALUES (2, 1, 'Pantalón negro', 'uploads/test2.jpg', 2, 2, 1, 1, 5, null, true);
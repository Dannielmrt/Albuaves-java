INSERT INTO birds (bird_id, common_name, scientific_name, description, img_url)
VALUES
  (1, 'Martinete', 'Nycticorax nycticorax', 'Ave nocturna de plumaje blanco y negro, común en humedales.', './imgs/aves//martinete.jpg'),
  (2, 'Garza real', 'Ardea cinerea', 'Grande y elegante, con plumaje gris y pico largo.', './imgs/aves//garza_real.jpg'),
  (3, 'Flamenco común', 'Phoenicopterus roseus', 'Ave rosada de patas largas, típica de zonas salinas.', './imgs/aves//flamenco_comun.jpg'),
  (4, 'Ánade real', 'Anas platyrhynchos', 'Pato común, macho con cabeza verde y pico amarillo.', './imgs/aves//anade_real.jpg'),
  (5, 'Charran común', 'Sterna hirundo', 'Ave marina de vuelo ágil, pico rojo y negro.', './imgs/aves//charran_comun.jpg'),
  (6, 'Somormujo lavanco', 'Podiceps cristatus', 'Elegante nadador con cresta y cuello largo.', './imgs/aves//somormujo_lavanco.jpg'),
  (7, 'Calamón común', 'Porphyrio porphyrio', 'Ave de plumaje azul intenso, pico y patas rojas.', './imgs/aves//calamon_comun.jpg'),
  (8, 'Avetoro común', 'Botaurus stellaris', 'Ave esquiva de plumaje pardo, similar a un búho.', './imgs/aves//avetoro_comun.jpg'),
  (9, 'Pato colorado', 'Netta rufina', 'Macho con cabeza roja y pico rojo brillante.', './imgs/aves//pato_colorado.jpg'),
  (10, 'Aguilucho lagunero', 'Circus aeruginosus', 'Rapaz de alas largas, habita en zonas húmedas.', './imgs/aves//aguilucho_lagunero.jpg');

INSERT INTO sighting (sighting_id, bird_id, date, time, location, comments, img_url)
VALUES
  (1, 3, '2025-10-15', '09:30:00', 'Laguna de la Albufera', 'Grupo de 12 flamencos alimentándose.','img/martinete.jpg'),
  (2, 1, '2025-10-16', '18:00:00', 'Ribera del lago', 'Martinete posado en un junco al atardecer.','img/garza_real.jpg'),
  (3, 5, '2025-10-17', '11:15:00', 'Isla de El Palmar', 'Charranes pescando en grupo.','img/flamenco_comun.jpg');

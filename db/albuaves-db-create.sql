-- Crear tabla aves
CREATE TABLE birds (
  bird_id INTEGER PRIMARY KEY AUTOINCREMENT,
  common_name TEXT NOT NULL,
  scientific_name TEXT NOT NULL,
  description TEXT,
  img_url TEXT
);

-- Crear tabla avistamientos
CREATE TABLE sighting (
  sighting_id INTEGER PRIMARY KEY AUTOINCREMENT,
  bird_id INTEGER NOT NULL,
  date DATE NOT NULL,
  time TIME NOT NULL,
  location TEXT NOT NULL,
  comments TEXT,  
  img_url TEXT,
  FOREIGN KEY (bird_id) REFERENCES birds(bird_id)
);

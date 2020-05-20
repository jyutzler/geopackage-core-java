CREATE TABLE 'gpkgext_semantic_annotations' (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  type TEXT NOT NULL,
  title TEXT NOT NULL,
  description TEXT,
  uri TEXT NOT NULL
);

CREATE TABLE 'gpkgext_sa_reference' (
  table_name TEXT NOT NULL,
  key_column_name TEXT,
  key_value INTEGER,
  sa_id INTEGER NOT NULL,
  CONSTRAINT sa_id_fk FOREIGN KEY (sa_id) REFERENCES gpkgext_semantic_annotations(id)
);

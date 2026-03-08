ALTER TABLE artist DROP COLUMN created_by;
ALTER TABLE artist DROP COLUMN updated_by;
ALTER TABLE artist ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE artist ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE artist SET created_by = 3, updated_by = 3;

ALTER TABLE author DROP COLUMN created_by;
ALTER TABLE author DROP COLUMN updated_by;
ALTER TABLE author ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE author ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE author SET created_by = 3, updated_by = 3;

ALTER TABLE book DROP COLUMN created_by;
ALTER TABLE book DROP COLUMN updated_by;
ALTER TABLE book ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE book ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE book SET created_by = 3, updated_by = 3;

ALTER TABLE diary DROP COLUMN created_by;
ALTER TABLE diary DROP COLUMN updated_by;
ALTER TABLE diary ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE diary ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE diary SET created_by = 3, updated_by = 3;

ALTER TABLE movie DROP COLUMN created_by;
ALTER TABLE movie DROP COLUMN updated_by;
ALTER TABLE movie ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE movie ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE movie SET created_by = 3, updated_by = 3;

ALTER TABLE music DROP COLUMN created_by;
ALTER TABLE music DROP COLUMN updated_by;
ALTER TABLE music ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE music ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE music SET created_by = 3, updated_by = 3;

ALTER TABLE task DROP COLUMN created_by;
ALTER TABLE task DROP COLUMN updated_by;
ALTER TABLE task ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE task ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE task SET created_by = 3, updated_by = 3;

ALTER TABLE training DROP COLUMN created_by;
ALTER TABLE training DROP COLUMN updated_by;
ALTER TABLE training ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE training ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE training SET created_by = 3, updated_by = 3;

ALTER TABLE training_menu DROP COLUMN created_by;
ALTER TABLE training_menu DROP COLUMN updated_by;
ALTER TABLE training_menu ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE training_menu ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE training_menu SET created_by = 3, updated_by = 3;

ALTER TABLE user DROP COLUMN created_by;
ALTER TABLE user DROP COLUMN updated_by;
ALTER TABLE user ADD COLUMN created_by INT NOT NULL DEFAULT 0;
ALTER TABLE user ADD COLUMN updated_by INT NOT NULL DEFAULT 0;
UPDATE user SET created_by = 3, updated_by = 3;

CREATE SEQUENCE common_seq START 100000;

CREATE TABLE cities (
  ref  TEXT PRIMARY KEY,
  name TEXT NOT NULL
);

ALTER TABLE users ADD COLUMN city_ref TEXT REFERENCES cities (ref) ON UPDATE CASCADE;

CREATE TABLE projects (
  id          INTEGER  PRIMARY KEY DEFAULT nextval('common_seq'),
  name        TEXT     UNIQUE  NOT NULL ,
  description TEXT             NOT NULL
);

CREATE TYPE group_type AS ENUM ('REGISTERING','CURRENT', 'FINISHED');

CREATE TABLE groups (
  id          INTEGER      PRIMARY KEY DEFAULT nextval('common_seq'),
  name        TEXT         UNIQUE  NOT NULL ,
  type        group_type           NOT NULL ,
  project_id  INTEGER              NOT NULL REFERENCES projects(id)
);

CREATE TABLE user_group(
  id_user   INTEGER   NOT NULL REFERENCES users(id),
  id_group  INTEGER   NOT NULL REFERENCES groups(id)
);


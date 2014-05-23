source booksdb-schema.sql;


insert into users values('alicia', MD5('alicia'), 'Alicia Pérez', 'alicia@acme.com');
insert into user_roles values ('alicia', 'registered');

insert into users values('maria', MD5('maria'), 'Maria Elizalde', 'maria@acme.com');
insert into user_roles values ('maria', 'registered');

insert into users values('pepito', MD5('alicia'), 'Pepito Grillo', 'pepito@acme.com');
insert into user_roles values ('pepito', 'registered');

insert into users values('blas', MD5('blas'), 'Blas López', 'blas@acme.com');
insert into user_roles values ('blas', 'registered');

insert into users values('victor', MD5('victor'), 'Victor Ramirez', 'victor@acme.com');
insert into user_roles values ('victor', 'registered');

insert into users values('valen', MD5('valen'), 'Valentina Atalla', 'valen@acme.com');
insert into user_roles values ('valen', 'registered');

insert into users values('edith', MD5('edith'), 'Edith Galmes', 'edith@acme.com');
insert into user_roles values ('edith', 'admin');



insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ('Juego de Tronos I', 'George R.R. Martin', 'English', 'First Edition','2000-03-12', '2001-03-12', 'GIGAMESH');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 'Juego de Tronos II', 'George R.R. Martin', 'English', 'First Edition','2004-03-09','2004-03-29', 'GIGAMESH');
insert into book ( tittle, author, language, edition, dateedition, dateprint, editorial)values ( 'Marina', 'Carlos Ruiz Zafón', 'Castellano', 'Novena Edición', '1999-04-05','2000-04-15', 'Edebé');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 'La Sombra del Viento', 'Carlos Ruiz Zafón', 'Castellano', 'Tercera Edición', '2013-01-09','2013-04-05', 'Edebé');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 'Me lo dijo mi almohada', 'Elena Dreser', 'Castellano', 'First Edition', '2004-02-06', '2004-06-13', 'Editorial Progreso');
insert into book ( tittle, author, language, edition, dateedition, dateprint, editorial)values ( 'Maldito Kharma', 'David Safier', 'Castellano', 'Segunda Edición', '2003-10-02', '2003-11-23', 'Seix Barral');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 't1', 'a1', 'Castellano','First Edition','2013-10-17', '2014-01-17', 'e1');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 't2', 'a2','Castellano','First Edition','2000-01-04','2001-01-05','e2');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 't3', 'a3','Castellano','First Edition','2009-11-06','2009-12-12', 'e3');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 't4', 'a4','Castellano','First Edition','2007-08-09','2008-08-09', 'e5');
insert into book (tittle, author, language, edition, dateedition, dateprint, editorial)values ( 't5', 'a5','Castellano', 'First Edition','2002-07-12','2002-10-10', 'e1');

insert into review (username,  text, idbook) values ('valen',  'Este libro es muy divertido', 2);
insert into review (username,  text, idbook) values ('maria', 'El mejor libro que he leido', 3);
insert into review (username,  text, idbook) values ('victor',  'Entretenido', 3);
insert into review (username,  text, idbook) values ('blas',  'No me ha gustado nada', 4);
insert into review ( username,  text, idbook) values ('alicia',  'Interesante', 1);
insert into review ( username,  text, idbook) values ('alicia', 'emocionante', 5);
insert into review (username,  text, idbook) values ('alicia',  'horrible', 6);
insert into review (username, text, idbook) values ('alicia',  'recomendado', 7);
insert into review (username,  text, idbook) values ('valen',  'Interesante', 8);
insert into review ( username,  text, idbook) values ('victor',  'Interesante', 9);
insert into review ( username,  text, idbook) values ('alicia',  'Interesante', 10);
insert into review ( username,  text, idbook) values ('victor',  'Interesante', 11);



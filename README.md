# Gestion-Usuarios

create database if not exists adea;
use adea;
create table if not exists usuario(
	login varchar(20) not null primary key,
	password varchar(44) not null,
	nombre varchar(50) not null,
	cliente float not null,
	email varchar(50),
	fecha_alta date not null default current_date,
	fecha_baja date,
	status char(1) not null default 'A',
	intentos float not null default 0,
	fecha_revocado date,
	fecha_vigencia date,
	no_acceso integer,
	apellido_paterno varchar(50),
	apellido_materno varchar(50),
	area decimal(5, 0),
	fecha_modificacion date not null default current_date
);
drop table usuario;
delete from usuario;
alter table usuario add constraint unique_cliente unique (cliente);
INSERT INTO usuario (login, password, nombre, cliente, email, fecha_alta, fecha_baja, status, intentos, fecha_revocado, fecha_vigencia, no_acceso, apellido_paterno, apellido_materno, area, fecha_modificacion)
VALUES
('usuario1', '5sPaWyBmNNfz81htdH/9s2tcZ1dXs4DGpf5cVwxxQ0k=', 'Nombre1', 1001, 'usuario1@example.com', '2024-01-01', NULL, 'A', 0, NULL, '2025-01-01', 10, 'Paterno1', 'Materno1', 100, '2024-08-21');

 

CREATE TABLE if not exists public.vote (
	id bigserial NOT NULL,
	id_session bigserial NOT NULL,
	id_associate bigserial NOT NULL,
	vote boolean,
	PRIMARY KEY (id)
);

CREATE TABLE if not exists public.session_status(
	id bigserial not null,
	id_session bigserial NOT NULL,
	session_status boolean NOT NULL,
	primary key (id)
);

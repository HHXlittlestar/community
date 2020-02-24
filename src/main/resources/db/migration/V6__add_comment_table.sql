create table comment
(
	id bigint,
	parent_id bigint not null,
	type int not null,
	commentator int not null,
	gmt_creat bigint not null,
	gmt_modify bigint not null,
	like_count bigint default 0,
	content varchar(1024) not null,
	constraint comment_pk
		primary key (id)
);
create table question
(
	id int auto_increment,
	title varchar(50),
	description text,
	gmt_creat bigint,
	gmt_modify bigint,
	creator int,
	comment_count int default 0,
	view_count int default 0,
	like_count int default 0,
	tag varchar(256),
	constraint question_pk
		primary key (id)
);
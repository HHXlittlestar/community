create table user
(
	id int auto_increment,
	name varchar(10),
	account_id varchar(100),
	token varchar(36),
	gmt_creat bigint,
	gmt_modify bigint,
	constraint user_pk
		primary key (id)
);
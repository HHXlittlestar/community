create table notification
(
	id bigint auto_increment,
	notifier bigint not null,
	receiver bigint not null,
	outer_id bigint not null,
	type int not null,
	gmt_creat bigint not null,
	status int default 0 not null,
	constraint notification_pk
		primary key (id)
);

comment on column notification.notifier is '通知的人';

comment on column notification.receiver is '接收消息的人';

comment on column notification.outer_id is '问题id或者回复id';

comment on column notification.status is '已读或者未读的状态';
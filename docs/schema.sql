-- 用户表
create table im_user (
    uid int auto_increment primary key,
    username varchar(50) not null,
    password varchar(128) not null,
    email varchar(50) default null,
    avatar varchar(500) not null
);

-- 消息内容表
create table im_msg_content (
    mid int auto_increment primary key,
    content varchar(1000) not null,
    sender_id int not null,
    recipient_id int not null,
    msg_type int not null,
    create_time timestamp not null
);

-- 消息索引表
create table im_msg_relation (
    owner_uid int not null,
    other_uid int not null,
    mid int not null,
    type int not null,
    create_time timestamp not null,
    primary key (`owner_uid`, `mid`)
);
create index `idx_owneruid_otheruid_msgid` on im_msg_relation (`owner_uid`, `other_uid`, `mid`);

-- 联系人列表
create table im_msg_contact (
    owner_uid int not null,
    other_uid int not null,
    mid int not null ,
    type int not null ,
    create_time timestamp not null ,
    primary key (`owner_uid`, `other_uid`)
);
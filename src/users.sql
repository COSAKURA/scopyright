create table users
(
    id         bigint auto_increment comment '主键，自增'
        primary key,
    username   varchar(255)                          null comment '用户名',
    role       enum ('creator', 'holder', 'auditor') not null comment '角色：内容创作者 ，版权持有者，审核机构',
    password   varchar(255)                          not null comment '密码（加密存储）',
    salt       varchar(255)                          not null comment '密码盐值',
    email      varchar(255)                          not null comment '电子邮件地址，唯一且不能为空',
    created_at timestamp default CURRENT_TIMESTAMP   null comment '创建时间',
    updated_at timestamp default CURRENT_TIMESTAMP   null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint email
        unique (email),
    constraint username
        unique (username)
);
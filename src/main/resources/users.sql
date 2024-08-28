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

INSERT INTO copyright.users (id, username, role, password, salt, email, created_at, updated_at) VALUES (1, 'admin', 'auditor', 'b05ceefb9d1f22376abe3662f8e7e8ccf492dc0dd5b4697986b5fb7b18d34f79', 'v0ApikIXfYT/eKpI4v+trQ==', '847@qq.com', '2024-08-20 15:29:15', '2024-08-20 15:31:14');
INSERT INTO copyright.users (id, username, role, password, salt, email, created_at, updated_at) VALUES (2, '刘嘉美', 'creator', 'ac1ac3b3d2bfdafb8ffca3cad4818306048b51746c87a41d89da6bebcbe1c7e2', 'iuHZ2uRYDwXYee+wZOh9Lg==', '250@qq.com', '2024-08-20 15:33:05', '2024-08-20 15:33:05');
INSERT INTO copyright.users (id, username, role, password, salt, email, created_at, updated_at) VALUES (3, '愉喜福', 'creator', '2cff9cbf20722c719932e8273b59ffeaa43bab48ab6194e7115c0f0e702f9af2', 'wjFd6AYfzVREEQgodnixQA==', '206453375@qq.com', '2024-08-20 15:39:15', '2024-08-20 15:39:15');

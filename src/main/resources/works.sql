create table works
(
    id                bigint auto_increment comment '主键，自增'
        primary key,
    title             varchar(255)                             not null comment '作品标题',
    description       text                                     null comment '作品描述',
    creator_id        bigint                                   not null comment '创作者ID，外键',
    status            enum ('pending', 'approved', 'rejected') null comment '状态（待审核, 成功, 拒绝）',
    copyright_number  varchar(255)                             null comment '版权编号',
    copyright_applied tinyint(1) default 0                     null comment '申请版权状态（false,true）',
    created_at        timestamp  default CURRENT_TIMESTAMP     null comment '创建时间',
    updated_at        timestamp  default CURRENT_TIMESTAMP     null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint works_ibfk_1
        foreign key (creator_id) references users (id)
);

create index creator_id
    on works (creator_id);

INSERT INTO copyright.works (id, title, description, creator_id, status, copyright_number, copyright_applied, created_at, updated_at) VALUES (1, '水浒传', '哈哈哈哈', 2, 'approved', '3d1b7518-7347-487e-b0a3-7ff0418de747', 1, '2024-08-20 15:34:27', '2024-08-20 15:35:22');
INSERT INTO copyright.works (id, title, description, creator_id, status, copyright_number, copyright_applied, created_at, updated_at) VALUES (2, '三国', '哈哈哈哈', 2, 'pending', null, 1, '2024-08-20 15:36:29', '2024-08-20 15:36:51');
INSERT INTO copyright.works (id, title, description, creator_id, status, copyright_number, copyright_applied, created_at, updated_at) VALUES (3, '芸汐传奇', '她，来自二十一世纪...', 3, 'approved', 'ff51dfa8-3d49-4955-beff-5a023a702ee9', 1, '2024-08-20 15:44:10', '2024-08-20 15:44:40');

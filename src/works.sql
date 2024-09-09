create table works
(
    id                bigint auto_increment comment '主键，自增'
        primary key,
    title             varchar(255)                         not null comment '作品标题',
    file_content      longblob                             null comment '作品内容',
    description       text                                 null comment '作品描述',
    creator_id        bigint                               not null comment '创作者ID，外键',
    status            varchar(100)                         null comment '状态（待审核, 成功, 拒绝）''pending'', ''approved'', ''rejected''',
    copyright_number  varchar(255)                         null comment '版权编号',
    copyright_applied tinyint(1) default 0                 null comment '申请版权状态（false,true）',
    created_at        timestamp  default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at        timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint works_ibfk_1
        foreign key (creator_id) references users (id)
);

create index creator_id
    on works (creator_id);
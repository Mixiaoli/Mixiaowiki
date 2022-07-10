#user表
drop table if exists `test`;
create table `test`(
   `id` bigint not null comment 'id',
   `name` varchar(50) comment  '名称',
   `password` varchar(50) comment '密码',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='测试';
insert into test(id, name, password) values (1,'cf',123456);


#demo user测试表
drop table if exists `demo`;
create table `demo`(
   `id` bigint not null comment 'id',
   `name` varchar(50) comment  '名称',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='测试';
insert into demo(id, name) values (1,'cs');


#电子书表
drop table if exists `ebook`;
create table `ebook`(
    `id` bigint not null comment  'id',
    `name` varchar(50) comment '名称',
    `category1_id` bigint comment  '分类1',
    `category2_id` bigint comment  '分类2',
    `description` varchar(200) comment  '描述',
    `cover` varchar(200) comment  '封面',
    `doc_count` int comment '文档数',
    `view_count` int comment '阅读数',
    `vote_count` int comment '点赞数',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='电子书';
#电子书初始化数据
insert into `ebook` (id,name,description) values (1,'Spring boot','零基础入门Java开发');
insert into `ebook` (id,name,description) values (2,'Vue入门','零基础入门Vue开发');
insert into `ebook` (id,name,description) values (3,'Pthon入门','零基础入门Python开发');
insert into `ebook` (id,name,description) values (4,'Mysql入门','零基础入门Mysql开发');
insert into `ebook` (id,name,description) values (5,'Oracle入门','零基础入门Oracle开发学习');
#分类
drop table if exists `category`;
create table `category`(
    `id` bigint not null comment  'id',
    `parent` bigint not null  default  0 comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='分类';

insert into `category` (id, parent, name, sort) values(100,000,'前端开发',100);
insert into `category` (id, parent, name, sort) values(101,100,'vue',101);
insert into `category` (id, parent, name, sort) values(102,100,'csss',102);
insert into `category` (id, parent, name, sort) values(200,000,'Html',200);
insert into `category` (id, parent, name, sort) values(201,200,'java',201);
insert into `category` (id, parent, name, sort) values(202,200,'python',202);
insert into `category` (id, parent, name, sort) values(300,000,'数据库',300);
insert into `category` (id, parent, name, sort) values(301,300,'mysql',301);
insert into `category` (id, parent, name, sort) values(302,300,'服务器',302);
insert into `category` (id, parent, name, sort) values(400,000,'开发工具',400);
insert into `category` (id, parent, name, sort) values(401,400,'其他',401);
insert into `category` (id, parent, name, sort) values(402,400,'集成开发',402);
#文档表
drop table if exists `doc`;
create table `doc`(
    `id` bigint not null comment 'id',
    `ebook_id` bigint not null default  0 comment '电子书id',
    `parent` bigint not null default 0 comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    `view_count` int default 0 comment '阅读数',
    `vote_count` int default 0 comment '点赞数',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='文档';

insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (1,1,0,'文档1',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (2,1,1,'文档1.1',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (3,1,0,'文档2',1,0,0);
insert into `doc` (id,ebook_id,parent,name,sort,view_count,vote_count) values (4,1,3,'文档2.1',1,0,0);

#文档内容表
drop table if exists `content`;
create table `content`(
    `id` bigint not null comment '文档id',
    `content` mediumtext not null comment '内容',
    primary key (`id`)
)engine=innodb default charset =utf8mb4 comment='文档内容';

#用户表 unique key 唯一键 id不能重复 限制唯一
drop table if exists `user`;
create table `user`(
    `id` bigint not null  comment 'ID',
    `login_name` varchar(50) not null comment '登陆名',
    `name` varchar(50) comment '昵称',
    `password` char(32) not null comment '密码',
    primary key (`id`),
    unique key `login_name`(`login_name`)
)engine=innodb default charset =utf8mb4 comment='用户';

insert into `user` (id, login_name, name, password) values (1,'test','测试','test');
create database if not exists online_oj;

use online_oj;


drop table if exists problem;
-- 创建题目表，id、标题、难度、描述、初始代码模板、测试用例
create table problem(
    id int primary key auto_increment,
    title varchar(50),
    level varchar(50),
    description varchar(4096),
    templatecode varchar(4096),
    testCode varchar(4096),
    testExample int
);

-- 创建用户表
-- 0 是普通用户， 1 是管理员
drop table if exists user;
create table user(
    id int primary key auto_increment,
    username varchar(8) unique not null,
    password varchar(12) not null,
    photo varchar(50),
    introduce varchar(50),
    submitcount int default 0,
    passcount int default 0,
    passproblemcount int default 0,
    status int
);
-- 论坛的评论
drop table review;
create table review(
    id int primary key auto_increment,
    username varchar(8) not null,
    title varchar(20),
    content mediumtext not null,
    time datetime not null,
    underId int not null default 0,
    -- 指向哪个评论
    sendTo int not null default 0
);

-- 题目的评论
drop table problemreview;
create table problemreview(
    id int primary key auto_increment,
    username varchar(8) not null,
    content mediumtext not null,
    time datetime not null,
    problemId int not null,
    underId int not null default 0,
    -- 指向哪个评论
    sendTo int not null default 0
);

-- 题解的评论表
drop table if exists settlereview;
create table settlereview(
    id int primary key auto_increment,
    username varchar(8) not null,
    content mediumtext not null,
    time datetime not null,
    settleId int not null,
    underId int not null default 0,
    sendTo int not null default 0
);


-- 提交记录表
-- status  0--解答正确  1--编译出错  2--运行出错  3--解答错误
-- content 提交的代码
-- errorReason 错误信息
-- passExample 通过的测试用例数
-- finalInput 最后一次的输入数据
drop table record;
create table record(
    id int primary key auto_increment,
    userId int not null,
    problemId int not null,
    status int not null,
    time datetime not null,
    content mediumtext,
    errorReason varchar(1024),
    passExample int,
    finalInput varchar(1024)
);

-- 题解表
drop table settle;
create table settle(
    id int primary key auto_increment,
    username varchar(8) not null,
    title varchar(20) not null,
    content mediumtext not null,
    time datetime not null,
    problemId int
);




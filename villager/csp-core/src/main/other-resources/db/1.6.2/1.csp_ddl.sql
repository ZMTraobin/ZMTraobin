
-- 企业微信配置
alter table csp_ljh_corp_agent  add AGENT_NO varchar(100) COMMENT '应用编号' after CORP_ID;
create unique index csp_ljh_corp_agent_n1  on csp_ljh_corp_agent(AGENT_NO);


alter table csp_ljh_mgt_user_community  add SOURCE_MGT_USER_ID varchar(100) COMMENT '源系统员工id' after MGT_USER_ID;
create  index   mgt_user_community_n1  on csp_ljh_mgt_user_community(DEL_FLAG,SOURCE_SYSTEM,SOURCE_SYSTEM_COMMUNITY_ID,MGT_USER_ID);
create  index   mgt_user_community_n2  on csp_ljh_mgt_user_community(DEL_FLAG,COMMUNITY_ID);

drop table if exists csp_umeng_app;
/*==============================================================*/
/* Table: csp_umeng_app                                         */
/*==============================================================*/
create table csp_umeng_app
(
   ID                   bigint not null auto_increment comment '主键',
   APP_KEY              varchar(50) not null comment 'app标识',
   PLATFORM             varchar(50) comment '平台',
   POPULAR              varchar(50) comment '流行度',
   NAME                 varchar(50) comment '名称',
   GROUP_NAME           varchar(50) comment '分组名称',
   USE_GAME_SDK         varchar(50) comment '是否使用游戏SDK统计',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (ID)
)
ENGINE= InnoDB DEFAULT CHARSET= utf8;
alter table csp_umeng_app comment '友盟app信息';

drop table if exists csp_ljh_kpi_user;
/*==============================================================*/
/* Table: csp_ljh_kpi_user                                      */
/*==============================================================*/
create table csp_ljh_kpi_user
(
   ID                   bigint not null auto_increment comment '主键',
   APP_KEY              varchar(50) not null comment 'app标识',
   KPI_DATE             date comment '日期',
   INSTALLATIONS        bigint comment '累计总用户数',
   NEW_USERS            bigint comment '新增用户数',
   ACTIVE_USERS         bigint comment '活跃用户数',
   LAUNCHES             bigint comment '启动次数',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (ID)
)
ENGINE= InnoDB DEFAULT CHARSET= utf8;
alter table csp_ljh_kpi_user comment '乐家慧用户指标分析';

drop table if exists csp_ljh_kpi_duration;
/*==============================================================*/
/* Table: csp_ljh_kpi_duration                                  */
/*==============================================================*/
create table csp_ljh_kpi_duration
(
   ID                   bigint not null auto_increment comment '主键',
   APP_KEY              varchar(50) not null comment 'app标识',
   KPI_DATE             date comment '日期',
   PERIOD_TYPE          varchar(50) comment 'daily :日使用时长; daily_per_launch :单次使用时长',
   DIMENSION            varchar(100) comment '维度',
   NUM                  bigint comment '数值',
   AVERAGE              varchar(50) comment '平均值',
   PERCENT              decimal(10,2) comment '占比',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (ID)
)
ENGINE= InnoDB DEFAULT CHARSET= utf8;
alter table csp_ljh_kpi_duration comment '乐家慧用户参与度分析';

-- 相关索引
create  index   ljh_kpi_user_n1  on csp_ljh_kpi_user(APP_KEY,KPI_DATE);
create  index   ljh_kpi_user_n2 on csp_ljh_kpi_user(KPI_DATE);

create  index   csp_umeng_app_n1 on csp_umeng_app(APP_KEY);
create  index   csp_umeng_app_n2 on csp_umeng_app(GROUP_NAME);

-- 统计视图
create or replace view umeng_kpi_user_v as
select
  b.GROUP_NAME as groupName,
  a.KPI_DATE as kpiDate,
  sum(a.INSTALLATIONS) as installations,
  sum(a.NEW_USERS) as newUsers,
  sum(a.ACTIVE_USERS) as activeUsers,
  sum(a.LAUNCHES) as launches
  FROM csp_ljh_kpi_user a ,csp_umeng_app b
  where a.APP_KEY=b.APP_KEY
  group by b.GROUP_NAME,a.KPI_DATE
  ;
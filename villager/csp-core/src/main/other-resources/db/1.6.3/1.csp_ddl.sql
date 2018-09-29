 drop table if exists csp_ljh_kpi_event_group;

/*==============================================================*/
/* Table: csp_ljh_kpi_event_group                               */
/*==============================================================*/
create table csp_ljh_kpi_event_group
(
   ID                   bigint not null auto_increment comment '主键',
   APP_KEY              varchar(50) not null comment 'app标识',
   KPI_DATE             date comment '日期',
   GROUP_ID             varchar(50) comment '事件id',
   NAME                 varchar(100) comment '事件编码',
   DISPLAY_NAME         varchar(100) comment '事件描述',
   NUM                  bigint comment '数值',
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
alter table csp_ljh_kpi_event_group comment '乐家慧功能使用分析';

create  index   kpi_event_group_n1  on csp_ljh_kpi_event_group(APP_KEY,KPI_DATE,NAME,DISPLAY_NAME);
drop index csp_umeng_app_n1 on csp_umeng_app ;
create  index   csp_umeng_app_n1 on csp_umeng_app(APP_KEY,GROUP_NAME);

create or replace view umeng_kpi_event_group_v as
select  b.GROUP_NAME,a.KPI_DATE,d.DESCRIPTION as DISPLAY_NAME, sum(a.NUM) as NUM
 from csp_ljh_kpi_event_group a ,csp_umeng_app b,sys_code_b c , sys_code_value_b d
where a.APP_KEY=b.APP_KEY  and a.NAME=d.VALUE and c.CODE='CSP.UMENG.KPI.EVENT.GROUP' and c.code_id=d.code_id
group by b.GROUP_NAME,a.KPI_DATE,d.DESCRIPTION
;


 drop table if exists csp_ljh_kpi_result;

/*==============================================================*/
/* Table: csp_ljh_kpi_result                                    */
/*==============================================================*/
create table csp_ljh_kpi_result
(
   ID                   bigint not null auto_increment comment '主键',
   APP_NAME             varchar(50) not null comment 'app名称',
   KPI_DATE             date not null comment '日期',
   PAGE_VIEW            bigint default 0 comment '浏览量（PV）',
   UNIQUE_VISITOR       bigint default 0 comment '访客数（UV）',
   INTERNET_PROTOCOL_NUM bigint default 0 comment 'IP数',
   OUT_PERCENT          decimal(18,2) default 0 comment '跳出率',
   ACCESS_SECONDS_AVERAGE decimal(18,2) default 0 comment '平均访问时长（单位：秒）',
   PAGE_VIEW_AVERAGE    decimal(18,2) default 0 comment '平均访问页数',
   NEW_VISITOR_PERCENT  decimal(18,2) default 0 comment '新访客比率',
   OLD_VISITOR_PERCENT  decimal(18,2) default 0 comment '老访客比率',
   NEW_VISITOR_PAGE_VIEW bigint default 0 comment '新访客浏览量',
   OLD_VISITOR_PAGE_VIEW bigint default 0 comment '老访客浏览量',
   NEW_VISITOR          bigint default 0 comment '新访客数',
   OLD_VISITOR          bigint default 0 comment '老访客数',
   NEW_VISITOR_OUT_PERCENT decimal(18,2) default 0 comment '新访客跳出率',
   OLD_VISITOR_OUT_PERCENT decimal(18,2) default 0 comment '老访客跳出率 ',
   NEW_VISITOR_ACCESS_SECONDS_AVERAGE decimal(18,2) default 0 comment '新访客平均访问时长（单位：秒）',
   OLD_VISITOR_ACCESS_SECONDS_AVERAGE decimal(18,2) default 0 comment '老访客平均访问时长（单位：秒）',
   NEW_VISITOR_PAGE_VIEW_AVERAGE decimal(18,2) default 0 comment '新访客平均访问页数',
   OLD_VISITOR_PAGE_VIEW_AVERAGE decimal(18,2) default 0 comment '老访客平均访问页数',
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
alter table csp_ljh_kpi_result comment '乐家慧指标结果';


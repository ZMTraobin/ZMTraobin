CREATE TABLE
    csp_ljh_classify_tag
    (
        ID CHAR(32) NOT NULL COMMENT '主键',
        NAME VARCHAR(100) NOT NULL COMMENT '名称',
        MODEL VARCHAR(100) NOT NULL COMMENT '所属模块',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1,
        REQUEST_ID bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

CREATE TABLE `csp_ljh_neighbor_topic` (
  `ID` char(32) NOT NULL COMMENT '主键',
  `DEL_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `TOPIC_CONTENT` varchar(5000) DEFAULT NULL COMMENT '话题内容',
  `PUBLISH_TIME` datetime DEFAULT NULL COMMENT '发布时间',
  `PUBLISH_ID` char(32) DEFAULT NULL COMMENT '发布者ID',
  `COMMUNITY_ID` char(32) DEFAULT NULL COMMENT '话题小区ID',
  `TYPE_ID` char(32) DEFAULT NULL COMMENT '分类ID',
  `TOPIC_ADDRESS` varchar(200) DEFAULT NULL COMMENT '地理位置',
  `LONGITUDE` double DEFAULT NULL COMMENT '地理位置经度',
  `LATITUDE` double DEFAULT NULL COMMENT '地理位置纬度',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邻里话题表';

CREATE TABLE `csp_ljh_neighbor_topic_comment` (
  `ID` char(32) NOT NULL COMMENT '主键',
  `DEL_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `TOPIC_ID` char(32) DEFAULT NULL COMMENT '话题ID',
  `USER_ID` char(32) DEFAULT NULL COMMENT '评论者ID',
  `CONTENT` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `REPLY_ID` char(32) DEFAULT NULL COMMENT '被回复者ID',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邻里话题评论表';

CREATE TABLE `csp_ljh_neighbor_topic_image` (
  `ID` char(32) NOT NULL COMMENT '主键',
  `DEL_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `TOPIC_ID` char(32) DEFAULT NULL COMMENT '话题ID',
  `URL` varchar(1000) DEFAULT NULL COMMENT '图片路径',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邻里话题图片表';

CREATE TABLE `csp_ljh_neighbor_topic_praise` (
  `ID` char(32) NOT NULL COMMENT '主键',
  `DEL_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `TOPIC_ID` char(32) DEFAULT NULL COMMENT '话题ID',
  `USER_ID` char(32) DEFAULT NULL COMMENT '点赞ID',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邻里话题点赞表';

CREATE  VIEW `neighborhood_topic_time_view` AS select `n`.`ID` AS `ID`,((unix_timestamp(now()) - unix_timestamp(`n`.`PUBLISH_TIME`)) / 60) AS `TIMES`,(to_days(cast(now() as date)) - to_days(cast(`n`.`PUBLISH_TIME` as date))) AS `DAYS`,(year(now()) - year(`n`.`PUBLISH_TIME`)) AS `YEARS` from `csp_ljh_neighbor_topic` `n`;

CREATE  VIEW `neighborhood_topic_v` AS select `n`.`ID` AS `ID`,`n`.`TOPIC_CONTENT` AS `TOPIC_CONTENT`,`n`.`PUBLISH_TIME` AS `PUBLISH_TIME`,(case when ((`tv`.`TIMES` < 1) and (`tv`.`DAYS` = 0)) then '刚刚' when ((`tv`.`TIMES` < 1) and (`tv`.`DAYS` = 1)) then '昨天' when ((`tv`.`TIMES` < 60) and (`tv`.`DAYS` = 0)) then concat(round(`tv`.`TIMES`,0),'分钟前') when ((`tv`.`TIMES` >= 1) and (`tv`.`DAYS` = 1)) then concat('昨天',convert(date_format(`n`.`PUBLISH_TIME`,'%H:%i') using utf8)) when ((`tv`.`TIMES` >= 60) and (`tv`.`DAYS` = 0)) then convert(date_format(`n`.`PUBLISH_TIME`,'%H:%i') using utf8) when ((`tv`.`DAYS` >= 2) and (`tv`.`YEARS` = 0)) then convert(date_format(`n`.`PUBLISH_TIME`,'%m-%d %H:%i') using utf8) when ((`tv`.`DAYS` >= 2) and (`tv`.`YEARS` >= 1)) then convert(date_format(`n`.`PUBLISH_TIME`,'%m-%d %H:%i') using utf8) end) AS `SHOW_TIME`,`n`.`PUBLISH_ID` AS `PUBLISH_ID`,`a`.`NICK_NAME` AS `PUBLISHER`,`n`.`TOPIC_ADDRESS` AS `TOPIC_ADDRESS`,`c`.`LONGITUDE` AS `LONGITUDE`,`c`.`LATITUDE` AS `LATITUDE`,`n`.`COMMUNITY_ID` AS `COMMUNITY_ID`,`c`.`COMMUNITY_NAME` AS `COMMUNITY_NAME`,`c`.`COMPANY_ID` AS `COMPANY_ID`,`c`.`COMPANY_NAME` AS `COMPANY_NAME`,`n`.`TYPE_ID` AS `TYPE_ID`,`t`.`NAME` AS `TYPE_NAME`,count(`tc`.`ID`) AS `COMMENT_NUM`,`a`.`USER_ICON` AS `USER_ICON` from (((((`csp_ljh_neighbor_topic` `n` left join `neighborhood_topic_time_view` `tv` on((`tv`.`ID` = `n`.`ID`))) left join `csp_ljh_app_user` `a` on((`n`.`PUBLISH_ID` = `a`.`ID`))) left join `csp_ljh_base_community` `c` on((`n`.`COMMUNITY_ID` = `c`.`ID`))) left join `csp_ljh_classify_tag` `t` on((`n`.`TYPE_ID` = `t`.`ID`))) left join `csp_ljh_neighbor_topic_comment` `tc` on(((`n`.`ID` = `tc`.`TOPIC_ID`) and (`tc`.`DEL_FLAG` = '0')))) where (`n`.`DEL_FLAG` = '0') group by `n`.`ID` order by `n`.`PUBLISH_TIME` desc;

CREATE  VIEW `neighborhood_topic_view` AS select count(`tp`.`ID`) AS `PRAISE_NUM`,`w`.`ID` AS `ID`,`w`.`TOPIC_CONTENT` AS `TOPIC_CONTENT`,`w`.`PUBLISH_TIME` AS `PUBLISH_TIME`,`w`.`SHOW_TIME` AS `SHOW_TIME`,`w`.`PUBLISH_ID` AS `PUBLISH_ID`,`w`.`PUBLISHER` AS `PUBLISHER`,`w`.`TOPIC_ADDRESS` AS `TOPIC_ADDRESS`,`w`.`LONGITUDE` AS `LONGITUDE`,`w`.`LATITUDE` AS `LATITUDE`,`w`.`COMMUNITY_ID` AS `COMMUNITY_ID`,`w`.`COMMUNITY_NAME` AS `COMMUNITY_NAME`,`w`.`COMPANY_ID` AS `COMPANY_ID`,`w`.`COMPANY_NAME` AS `COMPANY_NAME`,`w`.`TYPE_ID` AS `TYPE_ID`,`w`.`TYPE_NAME` AS `TYPE_NAME`,`w`.`COMMENT_NUM` AS `COMMENT_NUM`,`w`.`USER_ICON` AS `USER_ICON` from (`neighborhood_topic_v` `w` left join `csp_ljh_neighbor_topic_praise` `tp` on(((`w`.`ID` = `tp`.`TOPIC_ID`) and (`tp`.`DEL_FLAG` = '0')))) group by `w`.`ID` order by `w`.`PUBLISH_TIME` desc;

CREATE  VIEW `neighborhood_topic_comment_view` AS select `t`.`ID` AS `ID`,`t`.`TOPIC_ID` AS `TOPIC_ID`,`t`.`USER_ID` AS `USER_ID`,`a`.`NICK_NAME` AS `USER_NAME`,`a`.`USER_ICON` AS `USER_ICON`,`t`.`CREATION_DATE` AS `CREATE_TIME`,`t`.`REPLY_ID` AS `REPLY_ID`,`u`.`NICK_NAME` AS `REPLYER`,`t`.`CONTENT` AS `CONTENT` from ((`csp_ljh_neighbor_topic_comment` `t` left join `csp_ljh_app_user` `a` on((`t`.`USER_ID` = `a`.`ID`))) left join `csp_ljh_app_user` `u` on((`t`.`REPLY_ID` = `u`.`ID`))) where (`t`.`DEL_FLAG` = '0');

alter table csp_ljh_base_community add  `UNABLE_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '启用标记（0：正常；1：停用;）' after `DEL_FLAG`;

-- 新增用户卡信息表
CREATE TABLE
    csp_app_user_card
    (
        CARD_ID bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        APP_USER_ID VARCHAR(32) NOT NULL COMMENT '用户ID',
        ID_NO VARCHAR(50) NOT NULL COMMENT '证件号',
        ID_TYPE VARCHAR(50) DEFAULT 'I' NOT NULL COMMENT
        '证件类型（I:身份证;T:临时身份证;P:护照;H:港澳通行证;W:台胞通行证;Z:其他证件;）',
        CARD_NO VARCHAR(1000) NOT NULL COMMENT '银行卡号',
        CARD_TYPE VARCHAR(50) DEFAULT 'DEBIT' NOT NULL COMMENT
        '卡类型（DEBIT:借记卡;CREDIT:贷记卡;PREPAID:预付卡;）',
        NAME VARCHAR(50) NOT NULL COMMENT '姓名',
        BANK_MOBILE VARCHAR(20) NOT NULL COMMENT '银行预留手机号',
        BANK_CODE VARCHAR(30) COMMENT '银行编码',
        ORDER_SQ int DEFAULT 1 COMMENT '序号',
        DEFAULT_FLAG VARCHAR(30) DEFAULT 'N' COMMENT '是否默认',
        STATUS VARCHAR(20) DEFAULT 'N' NOT NULL COMMENT '状态（绑定:Y;解绑:N;审核中:P）',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1 COMMENT '记录版本号',
        REQUEST_ID bigint DEFAULT -1 COMMENT '请求ID',
        PROGRAM_ID bigint DEFAULT -1 COMMENT '线程ID',
        CREATED_BY bigint DEFAULT -1 COMMENT '创建人',
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        LAST_UPDATED_BY bigint DEFAULT -1 COMMENT '最后更新人',
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
        LAST_UPDATE_LOGIN bigint DEFAULT -1 COMMENT '最后登陆人',
        PRIMARY KEY (CARD_ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户卡信息';

    alter table csp_ljh_base_community add  `COMMUNITY_CODE` VARCHAR(50) DEFAULT NULL COMMENT '项目编码' after `COMMUNITY_NAME`;




CREATE TABLE `csp_ljh_integral_flowing` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `APP_ID` varchar(200) DEFAULT NULL COMMENT 'APPID',
  `SERVICE_ID` varchar(100) DEFAULT NULL COMMENT '业务板块id',
  `UID` varchar(200) DEFAULT NULL COMMENT '用户id',
  `PHONE` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `PURCHASE_AMOUNT` int(11) DEFAULT NULL COMMENT '消费金额',
  `TYPE` varchar(10) DEFAULT NULL COMMENT '场景类型',
  `OUT_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '订单号',
  `AUTO_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '生成订单号',
  `DESCRIPTION` varchar(128) DEFAULT NULL COMMENT '描述',
  `ATTACH` varchar(128) DEFAULT NULL COMMENT '附加数据',
  `TIMESTAMPS` varchar(20) DEFAULT NULL COMMENT '时间戳',
  `CREDITS` int(11) DEFAULT NULL COMMENT '扣除积分数量',
  `STATUS` varchar(10) DEFAULT NULL COMMENT '接口调用状态，标记成功或者失败',
  `SIGN` varchar(32) DEFAULT NULL COMMENT '签名',
  `INTEGRAL_PARAME` longtext COMMENT '参数url',
  `INTEGRAL_TYPE` varchar(200) DEFAULT NULL COMMENT '操作积分类型',
  `INTEGRAL_URL` varchar(300) DEFAULT NULL COMMENT '接口url',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8 COMMENT='积分接口调用记录表';

CREATE TABLE `csp_ljh_integral_record` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `RULE_ID` bigint(20) NOT NULL COMMENT '规则主键',
  `EVENT_ID` varchar(50) DEFAULT NULL COMMENT '业务ID（例如：订单、互动话题）',
  `STATUS` varchar(20) NOT NULL DEFAULT 'todo' COMMENT '状态（待处理：todo；成功：success；失败：fail；）',
  `REMARK` varchar(1000) DEFAULT NULL COMMENT '备注',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1' COMMENT '记录版本号',
  `REQUEST_ID` bigint(20) DEFAULT '-1' COMMENT '请求ID',
  `PROGRAM_ID` bigint(20) DEFAULT '-1' COMMENT '线程ID',
  `CREATED_BY` bigint(20) DEFAULT '-1' COMMENT '创建人',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1' COMMENT '最后更新人',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1' COMMENT '最后登陆人',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分调用记录表';

CREATE TABLE `csp_ljh_integral_rule` (
  `RULE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则主键',
  `RULE_CODE` varchar(50) NOT NULL COMMENT '规则编号',
  `ACTION_TYPE` varchar(100) NOT NULL COMMENT '规则类型（消费类适用类互动类传播类营销类行为）',
  `EVENT_CODE` varchar(50) NOT NULL COMMENT '触发事件（登录订购门禁）',
  `NUM` bigint(20) NOT NULL DEFAULT '0' COMMENT '单次积分数',
  `CAPPING_FACTOR` varchar(50) NOT NULL COMMENT '封顶因子（次、不封顶、次/日、次/周、次/月、次/年）',
  `CAPPING_TIMES` bigint(20) NOT NULL DEFAULT '0' COMMENT '封顶次数',
  `BILLING_CYCLE` varchar(50) NOT NULL DEFAULT 'now' COMMENT '结算周期（now:实时结算）',
  `STATUS` varchar(20) NOT NULL DEFAULT 'disable' COMMENT '状态（生效:enable;失效:disable;审核中:processing）',
  `EFFECTIVE_START_DATE` datetime NOT NULL COMMENT '生效开始日期',
  `EFFECTIVE_END_DATE` datetime NOT NULL COMMENT '生效结束日期',
  `REMARK` varchar(1000) DEFAULT NULL COMMENT '备注',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1' COMMENT '记录版本号',
  `REQUEST_ID` bigint(20) DEFAULT '-1' COMMENT '请求ID',
  `PROGRAM_ID` bigint(20) DEFAULT '-1' COMMENT '线程ID',
  `CREATED_BY` bigint(20) DEFAULT '-1' COMMENT '创建人',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1' COMMENT '最后更新人',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1' COMMENT '最后登陆人',
  PRIMARY KEY (`RULE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='积分规则配置表';




    
 alter table csp_app_adv add GROUP_IDENTIFYING bigint(2) DEFAULT NULL COMMENT '三栏广告分组标识';

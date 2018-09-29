DROP TABLE IF EXISTS `csp_ljh_neighbor_topic_reply`;
CREATE TABLE `csp_ljh_neighbor_topic_reply` (
  `ID` char(32) NOT NULL COMMENT '主键',
  `DEL_FLAG` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `REPLY_ID` char(32) DEFAULT NULL COMMENT '被回复记录ID',
  `USER_ID` char(32) DEFAULT NULL COMMENT '回复者ID',
  `CONTENT` varchar(1000) COLLATE utf8mb4_general_ci COMMENT '回复内容',
  `COMMENT_ID` char(32) DEFAULT NULL COMMENT '评论ID',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='邻里评论回复表'
;

-- 新建索引
  create index NEIGHBORHOOD_TOPIC_IMAGE_N1 ON  csp_ljh_neighbor_topic_image(topic_id);
  create index NEIGHBOR_TOPIC_PRAISE_N1 ON  CSP_LJH_NEIGHBOR_TOPIC_PRAISE(topic_id,user_id);
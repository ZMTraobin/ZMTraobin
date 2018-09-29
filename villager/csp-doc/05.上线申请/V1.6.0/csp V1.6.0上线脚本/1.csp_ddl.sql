

alter table csp_order_form  add merchant_no varchar(100) COMMENT '商户编号' after description;

alter table  csp_ljh_sys_log  modify USER_AGENT VARCHAR(600) COMMENT '用户代理' ;
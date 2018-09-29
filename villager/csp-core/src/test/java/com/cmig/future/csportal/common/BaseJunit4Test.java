package com.cmig.future.csportal.common;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:58 2018/2/2.
 * @Modified by zhangtao on 10:58 2018/2/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={"classpath:/spring/applicationContext*.xml,classpath:/spring/spring-jedis.xml"})//加载spring配置文件
public class BaseJunit4Test {
}

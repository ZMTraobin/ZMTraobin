package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Date;

/**
 * LjhKpiEventGroupServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 2, 2018</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={
		"classpath*:/spring/applicationContext*.xml",
		"classpath*:/spring/spring-jedis.xml"
})//加载spring配置文件
public class LjhKpiEventGroupServiceImplTest {

	@Autowired
	private ILjhKpiEventGroupService ljhKpiEventGroupService;

	@BeforeClass
	public static void init() throws NamingException {
		ClassPathXmlApplicationContext app =new ClassPathXmlApplicationContext("classpath:InitJndi.xml");
		DataSource jndiDataSource =(DataSource) app.getBean("jndiDataSource");
		SimpleNamingContextBuilder builder =new SimpleNamingContextBuilder();
		builder.bind("java:comp/env/jdbc/hap_dev", jndiDataSource);
		builder.activate();
	}

	@Before
	public void before() throws Exception {

	}

	@After
	public void after() throws Exception {

	}

	/**
	 * Method: synEventGroupKpi(String date)
	 */
	@Test
	@Transactional   //标明此方法需使用事务
	@Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
	public void testSynEventGroupKpi() throws Exception {
		String yestday = DateUtils.formatDate(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
		ljhKpiEventGroupService.synEventGroupKpi(yestday);
	}


}

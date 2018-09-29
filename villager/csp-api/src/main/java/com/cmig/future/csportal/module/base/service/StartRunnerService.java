package com.cmig.future.csportal.module.base.service;

import com.cmig.future.csportal.module.sys.queue.TaskQueueDaemonThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:52 2017/12/20.
 * @Modified by zhangtao on 14:52 2017/12/20.
 */
@Service
@Lazy(false)
public class StartRunnerService {

	private static final Logger logger= LoggerFactory.getLogger(StartRunnerService.class);

	StartRunnerService(){
		logger.debug("构造方法");
	}

	@PostConstruct
	private void init(){
		logger.debug("容器启动后加载任务队列守护任务");
		TaskQueueDaemonThread.getInstance().init();
	}

	@PreDestroy
	private void destory(){
		logger.debug("容器停止后销毁");
	}
}

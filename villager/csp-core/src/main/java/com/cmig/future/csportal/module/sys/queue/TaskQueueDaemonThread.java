package com.cmig.future.csportal.module.sys.queue;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:16 2017/12/20.
 * @Modified by zhangtao on 11:16 2017/12/20.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * [任务调度系统]
 * <br>
 * [后台守护线程不断的执行检测工作]
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 2015年11月23日14:19:40
 */
public class TaskQueueDaemonThread {

	private static final Logger LOG = LoggerFactory.getLogger(TaskQueueDaemonThread.class);

	private TaskQueueDaemonThread() {
	}

	private static class LazyHolder {
		private static TaskQueueDaemonThread taskQueueDaemonThread = new TaskQueueDaemonThread();
	}

	public static TaskQueueDaemonThread getInstance() {
		return LazyHolder.taskQueueDaemonThread;
	}

	Executor executor = Executors.newFixedThreadPool(20);
	/**
	 * 守护线程
	 */
	private Thread daemonThread;

	/**
	 * 初始化守护线程
	 */
	public void init() {
		daemonThread = new Thread(new Runnable() {
			@Override
			public void run() {
				execute();
			}
		});
		daemonThread.setDaemon(true);
		daemonThread.setName("Task Queue Daemon Thread");
		daemonThread.start();
	}

	private void execute() {
		LOG.debug("start");
		while (true) {
			try {
				//从延迟队列中取值,如果没有对象过期则队列一直等待，
				Task task = delayQueue.take();
				if (task != null) {
					//修改问题的状态
					Runnable runnable = task.getRunnable();
					if (runnable == null) {
						continue;
					}
					executor.execute(runnable);
					LOG.info("[time {} at runnable {} leftSize {} ",System.currentTimeMillis(),runnable, delayQueue.size());
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		LOG.debug("end");
	}

	/**
	 * 创建一个最初为空的新 DelayQueue
	 */
	private DelayQueue<Task> delayQueue = new DelayQueue<>();

	/**
	 * 添加任务，
	 * time 延迟时间(毫秒)
	 * task 任务
	 * 用户为问题设置延迟时间
	 */
	public void put(long time, Runnable runnable) {
		//转换成ns
		long nanoTime = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS);
		//创建一个任务
		Task task = new Task(nanoTime, runnable);
		//将任务放在延迟的队列中
		delayQueue.put(task);
	}

	/**
	 * 结束订单
	 * @param runnable
	 */
	public boolean remove(Runnable runnable){
		//创建一个任务
		Task task = new Task(100000000, runnable);
		return delayQueue.remove(task);
	}
}
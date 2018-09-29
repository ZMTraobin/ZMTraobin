package com.cmig.future.csportal.module.sys.queue;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:15 2017/12/20.
 * @Modified by zhangtao on 11:15 2017/12/20.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * [任务调度系统]
 * <br>
 * [队列中要执行的任务]
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 2015年11月22日19:46:39
 */
public class Task<T extends Runnable> implements Delayed {

	private static final Logger logger= LoggerFactory.getLogger(Task.class);
	/**
	 * 到期时间
	 */
	private final long time;

	/**
	 * 问题对象
	 */
	private final T runnable;

	public Task(long timeout, T runnable) {
		this.time = System.nanoTime() + timeout;
		this.runnable = runnable;
	}

	/**
	 * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		logger.debug("delay {} 秒", TimeUnit.SECONDS.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS));
		return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	@Override
	public int compareTo(Delayed other) {
		// TODO Auto-generated method stub
		if (other == this) // compare zero ONLY if same object
			return 0;
		if (other instanceof Task) {
			Task x = (Task) other;
			long diff = time - x.time;
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else
				return 1;
		}
		long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
		return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
	}

	public T getRunnable() {
		return this.runnable;
	}



	@Override
	public int hashCode() {
		return runnable.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Task) {
			return object.hashCode() == hashCode() ? true : false;
		}
		return false;
	}


}
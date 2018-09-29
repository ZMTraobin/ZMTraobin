package com.cmig.future.csportal.module.sys.queue;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:27 2017/12/22.
 * @Modified by zhangtao on 14:27 2017/12/22.
 */
public class Test {

	public static void main(String[] args) {

		Runnable r=new Runnable() {
			@Override
			public void run() {
				System.out.println("test");
			}
		};

		Task a=new Task(100000, r);

		Task b=new Task(200000, r);

		System.out.println(a.equals(b));
	}
}

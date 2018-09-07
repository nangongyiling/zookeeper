package com.zp.test.zookeeper.demo.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements Runnable{

	private static OrderCodeGenerator ong = new OrderCodeGenerator();
	
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	//同时并发的线程数
	private static final int NUM = 10;
	
	private static CountDownLatch cdl = new CountDownLatch(NUM);
	
	private Lock lock = new ImproveLock();
	
	//创建订单接口
	public void createOrder() {
		String orderCode = null;
		lock.lock();
		try {
			//获取订单编号
			orderCode = ong.getOrderCode();
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			lock.unlock();
		}
		
		logger.info("id:===========>"+orderCode);
	}
	public void run() {
		try {
			cdl.await();
		} catch (Exception e) {
			// TODO: handle exception
		}
		createOrder();
	}

	public static void main(String[] args) {
		for(int i=0;i<NUM;i++) {
			new Thread(new OrderServiceImpl()).start();
			cdl.countDown();
	}
		
	}
}

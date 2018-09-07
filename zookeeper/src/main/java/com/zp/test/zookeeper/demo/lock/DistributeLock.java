package com.zp.test.zookeeper.demo.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class DistributeLock implements Lock{

	private static final String ZK_IP_PORT="192.168.13.129:2181";
	private static final String LOCK_NODE="/lock";
	
	private ZkClient client = new ZkClient(ZK_IP_PORT);
	
	private CountDownLatch cdl = null;
	public void lock() {
		if(tryLock()) {
			return;
		}
		waitForLock();
		lock();
	}

	//阻塞时的实现
	private void waitForLock() {
		//给节点加监听
		IZkDataListener listener = new IZkDataListener() {
			
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("--------get data delete event----------");
				if(cdl!=null) {
					cdl.countDown();
				}
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
		client.subscribeDataChanges(LOCK_NODE, listener);
		if(client.exists(LOCK_NODE)) {
			
			try {
				cdl = new CountDownLatch(1);
				cdl.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		client.subscribeDataChanges(LOCK_NODE, listener);
	}
	
	public void lockInterruptibly() throws InterruptedException {
		
	}

	public boolean tryLock() {
		try {
			client.createPersistent(LOCK_NODE);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	public void unlock() {
		client.delete(LOCK_NODE);
	}

	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}

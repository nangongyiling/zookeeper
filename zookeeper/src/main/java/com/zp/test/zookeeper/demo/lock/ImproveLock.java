package com.zp.test.zookeeper.demo.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class ImproveLock implements Lock{

	private static final String ZK_IP_PORT="192.168.13.129:2181";
	private static final String LOCK_NODE="/LOCK";
	
	private ZkClient client = new ZkClient(ZK_IP_PORT,1000,1000,new SerializableSerializer());
	private CountDownLatch cdl = null;
	private String beforePath;//前一个节点
	private String currentPath;//当前节点
	
	//判断目录是否存在，没有创建一个
	public ImproveLock() {
		if(!client.exists(LOCK_NODE)) {
			client.createPersistent(LOCK_NODE);
		}
	}

	public void lock() {
		if(tryLock()) {
			System.out.println(Thread.currentThread().getName()+"  获得了分布式锁");
			
		}else {
			waitForLock();
			lock();
		}
	}

	private void waitForLock() {
		IZkDataListener listener = new IZkDataListener() {
			
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(Thread.currentThread().getName()+":捕获到datadelete事件！------");
				if(cdl!=null) {
					cdl.countDown();
				}
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
		
		//给排在前面的节点增加数据删除的watcher
		client.subscribeDataChanges(beforePath, listener);
		if(client.exists(beforePath)) {
			cdl = new CountDownLatch(1);
			try {
				cdl.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		client.subscribeDataChanges(beforePath, listener);
	}
	
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	public boolean tryLock() {
		//如果当前临时节点为空，则表示第一次加锁
		if(currentPath == null || currentPath.length() == 0) {
			currentPath = client.createEphemeralSequential(LOCK_NODE+"/","lock");
			System.out.println("--------------"+currentPath);
		}
		
		//获取所有临时节点并排序，临时节点名称为自增长的字符串：0000000400
		List<String> childrens = client.getChildren(LOCK_NODE);
		Collections.sort(childrens);
		if(currentPath.equals(LOCK_NODE+"/"+childrens.get(0))) {
			//如果当前节点排名第一位
			return true;
		}else {
			//如果当前节点不是第一位，取前面的节点名称赋值。
			int wz = Collections.binarySearch(childrens, currentPath.substring(6));
			beforePath = LOCK_NODE+"/"+childrens.get(wz-1);
		}
		return false;
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	public void unlock() {
		client.delete(currentPath);
	}

	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}

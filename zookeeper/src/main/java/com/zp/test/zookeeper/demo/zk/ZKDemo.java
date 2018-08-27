package com.zp.test.zookeeper.demo.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZKDemo implements Watcher{

	private static final CountDownLatch cdl = new CountDownLatch(1);
	
	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper("192.168.13.129:2181",5000,new ZKDemo());
		System.out.println(zk.getState());
		try {
			cdl.await();
		} catch (Exception e) {
			System.out.println("ZK Session established");
		}
	}
	
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:"+event);
		if(KeeperState.SyncConnected == event.getState()) {
			cdl.countDown();
		}
	}

}

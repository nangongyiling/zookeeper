package com.zp.test.zookeeper.demo.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZKAsyncDemo implements Watcher{

	private static final CountDownLatch cdl = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("192.168.13.129:2181",5000,new ZKAsyncDemo());
		cdl.await();
		zk.create("/zk-async-", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallback(),
				new String("I am context"));
		zk.create("/zk-async-", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallback(),
				new String("I am context"));
		zk.create("/zk-async-", "789".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallback(),
				new String("I am context"));
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:"+event);
		if(KeeperState.SyncConnected == event.getState()) {
			cdl.countDown();
		}
	}

}

class IStringCallback implements AsyncCallback.StringCallback{

	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println("create path result:["+rc+","+path+","+ctx+",real path name:"+name);
	}
	
}

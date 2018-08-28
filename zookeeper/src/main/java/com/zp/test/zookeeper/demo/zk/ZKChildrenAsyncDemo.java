package com.zp.test.zookeeper.demo.zk;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZKChildrenAsyncDemo implements Watcher {

	private static final CountDownLatch cdl = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("192.168.13.129:2181",5000,new ZKChildrenAsyncDemo());
		cdl.await();
		
		zk.create("/zk-children-async", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.create("/zk-children-async/c1", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	
		zk.getChildren("/zk-children-async", true,new ChildrenCallback(),"ok");

		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			if(EventType.None == event.getType() && event.getPath() == null){
				cdl.countDown();
			}
		} else if(event.getType() == EventType.NodeChildrenChanged){
			try {
				System.out.println("child: "+zk.getChildren(event.getPath(), true));
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class ChildrenCallback implements AsyncCallback.Children2Callback{

	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		System.out.println(
				"Child: " + rc + ", path: " + path + ", ctx: " + ctx + ", children: " + children + ", stat: " + stat);
	
	}
	
}

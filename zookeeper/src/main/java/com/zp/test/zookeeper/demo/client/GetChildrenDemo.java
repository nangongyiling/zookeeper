package com.zp.test.zookeeper.demo.client;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class GetChildrenDemo {

	public static void main(String[] args) throws InterruptedException {
		String path = "/zk-client1";
		ZkClient client = new ZkClient("192.168.13.129:2182",5000);
		client.subscribeChildChanges(path, new IZkChildListener() {

			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println(parentPath+"的子节点发生变化："+currentChilds);
			}
			
		});
		client.createPersistent(path);
		Thread.sleep(1000);
		System.out.println(client.getChildren(path));
		Thread.sleep(1000);
		client.createPersistent(path+"/c1");
		Thread.sleep(1000);
		client.delete(path+"/c1");
		Thread.sleep(Integer.MAX_VALUE);
		
	}
}

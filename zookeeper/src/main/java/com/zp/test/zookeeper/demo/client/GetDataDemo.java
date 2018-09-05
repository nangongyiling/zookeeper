package com.zp.test.zookeeper.demo.client;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class GetDataDemo {

	public static void main(String[] args) throws InterruptedException {
		String path = "/zk-client3";
		ZkClient client = new ZkClient("192.168.13.129:2181",5000);
		client.createEphemeral(path,"123");
		client.createPersistent(path,"123");
		client.subscribeDataChanges(path, new IZkDataListener() {

			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println(dataPath+"  changed:"+data);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath+"  deleted");
			}
			
		});
		
		System.out.println(client.readData(path).toString());
		client.writeData(path, "456");
		Thread.sleep(1000);
		client.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
}

package com.zp.test.zookeeper.demo.client;

import org.I0Itec.zkclient.ZkClient;

public class CreateNodeDemo {

	public static void main(String[] args) {
		ZkClient client = new ZkClient("192.168.13.129:2181,192.168.13.129:2182,192.168.13.129:2183",5000);
		String path = "/zk-client/c2";
		//递归创建节点
		client.createPersistent(path,true);
	}
}

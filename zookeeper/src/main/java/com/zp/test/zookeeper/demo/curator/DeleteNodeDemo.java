package com.zp.test.zookeeper.demo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class DeleteNodeDemo {

	public static void main(String[] args) throws Exception {
		RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.13.129:2181")
				.sessionTimeoutMs(5000).retryPolicy(policy).build();
		client.start();
		String path = "/zk-curator/c3";
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"test".getBytes());
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath(path);
		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion());
	}
}

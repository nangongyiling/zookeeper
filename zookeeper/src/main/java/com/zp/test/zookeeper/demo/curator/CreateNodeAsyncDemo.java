package com.zp.test.zookeeper.demo.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class CreateNodeAsyncDemo {

	static CountDownLatch cdl = new CountDownLatch(2);
	static ExecutorService es = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.13.129:2181")
				.sessionTimeoutMs(5000).retryPolicy(policy).build();
		client.start();
		String path = "/zk-curator/c4";
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {

			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("event code:"+event.getResultCode()+",type:"+event.getType());
				cdl.countDown();
			}
			
		},es).forPath(path,"123".getBytes());
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {

			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("event code:"+event.getResultCode()+",type:"+event.getType());
				cdl.countDown();
			}
			
		}).forPath(path,"123".getBytes());
		
		cdl.countDown();
		es.shutdown();
	}
}

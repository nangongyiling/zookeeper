package com.zp.test.zookeeper.demo.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	private ZookeeperCentralConfigurer zookeeperCentralConfigurer;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, zookeeperCentralConfigurer.getProps());
	}

	public void setZookeeperCentralConfigurer(ZookeeperCentralConfigurer zookeeperCentralConfigurer) {
		this.zookeeperCentralConfigurer = zookeeperCentralConfigurer;
	}
	
}

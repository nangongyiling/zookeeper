配置tomcat服务器配置server.xml，这里把阀门配到Engine容器下，这样作用范围即在整个引擎，你也可以根据作用范围配置Host或Context下。

<Server port="8005"shutdown="SHUTDOWN">

……

<Engine name="Catalina"defaultHost="localhost">

<ValveclassName="org.apache.catalina.valves.PrintIPValve" />

……

</Engine>

这个项目不是webapp项目，所以没有办法测试web请求

<?xml version="1.0" encoding="UTF-8"?>
<web-app id="WebApp_ID" version="2.5"
	xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

	<display-name>zookeeper-demo</display-name>
	<description>Zookeeper Demo Application</description>

	<!--============================================================== -->
	<!-- Context parameters definition -->
	<!--============================================================== -->
	<context-param>
		<param-name>webAppRootKey</param-name>
		<param-value>zookeeper-demo.root</param-value>
	</context-param>
	<context-param>
		<param-name>log4jConfigLocation</param-name>
		<param-value>/WEB-INF/config/log4j.properties</param-value>
	</context-param>
	<context-param>
		<param-name>log4jRefreshInterval</param-name>
		<param-value>60000</param-value>
	</context-param>

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/config/applicationContext.xml</param-value>
	</context-param>

	<servlet>
		<servlet-name>spring</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>/WEB-INF/config/spring-mvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>spring</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>


	<!--============================================================== -->
	<!-- Listener definition -->
	<!--============================================================== -->
	<listener>
		<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
	</listener>

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>


	<!--============================================================== -->
	<!-- Filter definition -->
	<!--============================================================== -->
	<filter>
		<filter-name>characterEncodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>characterEncodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<!--============================================================== -->
	<!-- Web Session definition -->
	<!--============================================================== -->
	<session-config>
		<session-timeout>20</session-timeout>
	</session-config>


	<!--============================================================== -->
	<!-- Redirect page definition -->
	<!--============================================================== -->
	<error-page>
		<error-code>403</error-code>
		<location>/403.jsp</location>
	</error-page>
	<error-page>
		<error-code>404</error-code>
		<location>/404.jsp</location>
	</error-page>
	<error-page>
		<error-code>500</error-code>
		<location>/500.jsp</location>
	</error-page>


	<!--============================================================== -->
	<!-- First page definition -->
	<!--============================================================== -->
	<welcome-file-list>
		<welcome-file>index.htm</welcome-file>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>

</web-app>

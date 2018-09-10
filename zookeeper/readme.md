配置tomcat服务器配置server.xml，这里把阀门配到Engine容器下，这样作用范围即在整个引擎，你也可以根据作用范围配置Host或Context下。

<Server port="8005"shutdown="SHUTDOWN">

……

<Engine name="Catalina"defaultHost="localhost">

<ValveclassName="org.apache.catalina.valves.PrintIPValve" />

……

</Engine>
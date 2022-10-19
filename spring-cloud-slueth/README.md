#spring-cloud-slueth链路id实验范围
1.支持对http请求日志添加traceid
2.跨线程传递traceid
3.MQTT接收消息创建新traceid
4.feignclient服务调用支持链路id传递
5.resttemplate服务调用支持链路id传递
#服务启动方式
两个服务启动
主服务以 -Dserver.port=8080 端口启动
从服务以-Dserver.port=8090 端口启动
#验证过程
1.验证服务启动
```
curl http://localhost:8080/home
curl http://localhost:8090/home
```

2.验证同一个服务接口两次http请求链路id不同 
```
curl http://localhost:8090/home
```

3.验证feign-client跨服务链路id传递
```
curl http://locahost:8080/test-feign-client
```

4.验证rest-template跨服务链路id传递
```
curl http://localhost:8080/test-feign-client
```

5.验证MQTT接收消息新建traceid


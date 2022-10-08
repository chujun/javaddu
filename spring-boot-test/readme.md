# spring boot test添加的类库
## spring-boot-starter-test

## 两大模块
spring-boot-test:
spring-boot-test-autoconfigure:支持测试的自动配置auto-configuration
## 表1 测试依赖库
|库|	说  明|
|---|---|
|JUnit|	包括JUnit 4和JUnit 5|
|Spring Test & Spring Boot Test	|用于Spring Boot测试|
|AssertJ	|流式的断言库|
|Hamcrest|	匹配库|
|Mockito|	Mock框架|
|JSONassert|	为JSON提供断言|
|JsonPath|	为JSON提供XPATH功能|

##spring boot test支持的测试种类
通常情况下，Spring Boot Test 支持的测试种类可以分为以下 3 种：
单元测试：主要用于测试类功能等。
切片测试：介于单元测试与集成测试之间，在特定环境下才能执行。
集成测试：测试一个完整的功能逻辑。

## 核心注解
### @SpringBootTest注解
不会启动一个真实的web服务器，可以对webEnvironment属性进行配置
* MOCK(Default) : Loads a web ApplicationContext and provides a mock web environment. Embedded servers are not started when using this annotation. If a web environment is not available on your classpath, this mode transparently falls back to creating a regular non-web ApplicationContext. It can be used in conjunction with @AutoConfigureMockMvc or @AutoConfigureWebTestClient for mock-based testing of your web application.

* RANDOM_PORT: Loads a WebServerApplicationContext and provides a real web environment. Embedded servers are started and listen on a random port.

* DEFINED_PORT: Loads a WebServerApplicationContext and provides a real web environment. Embedded servers are started and listen on a defined port (from your application.properties) or on the default port of 8080.

* NONE: Loads an ApplicationContext by using SpringApplication but does not provide any web environment (mock or otherwise).
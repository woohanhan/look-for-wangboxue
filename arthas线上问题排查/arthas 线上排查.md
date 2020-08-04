## 获取Spring Bean的成员变量

```
@Service
public class Test{
	private ConcurrentHashMap<Long, String> testMap = new ConcurrentHashMap<>();
	....
}
```

问题:现在需要知道testMap的值是多少



在idea的arthas插件的github中，WangJi92提到了这个问题的解决方案

https://github.com/WangJi92/arthas-idea-plugin/issues/5

与WangJi92的场景不同的是，我们的组件使用了Resteasy。所以param[0]的来源不是

```
org.springframework.web.servlet.DispatcherServlet#doDispatch
```

查看doc可以知道这个param[0]实际上是`HttpServletRequest`

现在需要找到Resteasy处理请求的`HttpServletRequest`

这里有2种方式

1.去看Resteasy的源码，找到处理路径带有这个入参的方法

2.查看指定请求的调用栈，然后直接定位到源码具体位置

```
stack com.test.Test someApi -n 1
Press Q or Ctrl+C to abort.Affect(class count: 1 , method count: 1) cost in 86 ms, listenerId: 12ts=2020-08-04 17:49:52;thread_name=http-nio-8080-exec-24;id=135;is_daemon=true;priority=5;TCCL=org.apache.catalina.loader.ParallelWebappClassLoader@7f38dc47
        at org.jboss.resteasy.plugins.server.servlet.ServletContainerDispatcher.service(ServletContainerDispatcher.java:220)
        at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:56)
        at org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.service(HttpServletDispatcher.java:51)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)
```

随便挑一个`HttpServletDispatcher.java:56`

对应的源码:

```
public void service(String httpMethod, HttpServletRequest request, HttpServletResponse response, boolean handleNotFound) throws IOException, NotFoundException
```

所以排查命令为：

```
watch -x 3 -n 1  org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher service '@org.springframework.web.context.support.WebApplicationContextUtils@getWebApplicationContext(params[1].getServletContext()).getBean("test").testMap'
```


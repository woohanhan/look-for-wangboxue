启动完成后，spring 容器发出来refresh事件，此时DispatcherServlet监听到了该事件:
```
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.refreshEventReceived = true;
		onRefresh(event.getApplicationContext());//接收到refresh事件，准备进行相关操作，注意这里获取到了上下文对象
	}

```
接着，初始化所有的策略(Strategies)，这里主要有9种策略需要初始化:
```
	protected void onRefresh(ApplicationContext context) {
		initStrategies(context);
	}

    protected void initStrategies(ApplicationContext context) {
        initMultipartResolver(context);//用于对multipart请求进行处理
        initLocaleResolver(context);//用于国际化
        initThemeResolver(context);//用于主题
        initHandlerMappings(context);//用于存放多种handlerMapping
        initHandlerAdapters(context);//用于调用handlerMapping（不同的handlerMapping有不同的调用方式，这样做可以屏蔽具体调用方式）
        initHandlerExceptionResolvers(context);//用于处理异常
        initRequestToViewNameTranslator(context);//用于在异常情况下尝试根据请求名去获取对应的视图
        initViewResolvers(context);//用于构造视图
        initFlashMapManager(context);//？
    }
```
由于前后端的分离，视图解析器viewResolver使用的越来越少，ThemeResolver从spring mvc诞生之初使用就不多。我们
重点需要关注HandlerMappings和HandlerAdapters以及HandlerExceptionResolvers这三个策略。

- HandlerMappings：
从spring 的上下文中拿到所有的handlerMapping，并放入到DispatcherServlet对象的handlerMappings列表中,注意到
如果为2个不同类型的HandlerMapping指定同样的名称，那么有一个会被覆盖。注意：如果上下文没有配置，则启用默认的策
略，从"DispatcherServlet.properties" 文件中读取。
```
private void initHandlerMappings(ApplicationContext context) {
    ...
    this.handlerMappings = null;
    Map<String, HandlerMapping> matchingBeans =
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
    this.handlerMappings = new ArrayList<>(matchingBeans.values());
    AnnotationAwareOrderComparator.sort(this.handlerMappings);//为它们排序
    ...
    if (this.handlerMappings == null)//设置默认的HandlerMappings策略
        this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
}
```
- HandlerAdapters：具体过程也HandlerMappings策略一样，这里不再赘述。




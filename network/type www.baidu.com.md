# 1.type www.baidu.com
当输入百度时，浏览器（user-Agent）发送http请求：
```
GET / HTTP/1.1
Host: www.baidu.com
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9
Cookie: BAIDUID=F5B2583BC49026CAD812B04A7295731C:FG=1; BIDUPSID=F5B2583BC49026CAD812B04A7295731C; PSTM=1543893639; BD_UPN=12314753; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; H_PS_PSSID=1422_21118_28132_27751_28139_27509; delPer=0; BD_CK_SAM=1; PSINO=5; H_PS_645EC=d250ZewM5K1ojyZKqn2Rhv4qeDOrz6RaL2MHnUK9LjQ5HWkPbJyUHy2ge%2Bo; BDSVRTM=0; BD_HOME=0
```
```Accept*```代表了user-agent的“expectation”，既然是"expectation"，则origin-server既可以选择满足期望，例如返回一个text/html，也可也完全的不遵守偏要返回
application/json，这会出现406异常。
>但是这并不代表http所期望执行的method一定是没有执行完成的，例如，一个post请求发送了content-type:application/json到origin-server,并且自身描述了希望accept:text/html,
但是对于origin-server而言，它确实可以处理application/json的请求，并执行了一系列的动作（例如存储这些数据到数据库），但是当origin-server准备返回时，他发现自己准备返回的
content-type和实际user-agent的accept不一致，则仍旧会出现406异常。

```q=0.9```决定了```application/xml```在user-agent中“expectation”的权重，其值为0到1，1为默认值。注意到```*/*```这代表不会产生406错误，表示该http接收所有类型的content-type（当然优先期望q=1的类型）。

```Host = www.baidu.com```即是处理该请求的主机。```GET / HTTP/1.1```代表该次请求为GET方法，需要Host下的/下的资源（/下有很多资源，但origin-server并不会全部返回给user-agent）。
注意到，GET总是以host为准，后面则描述想要的资源的相对路径（/*）。

```Connection: keep-alive```user-agent指明想要通过keep-alive的方式建立连接
>这里存在一些兼容性问题，在Http1.1中，如果不指定connection则，默认keep-alive,而在Http1.0中某些proxy server并不知道如何处理keep-alive，这
需要手动去设置```Connection: Close```。

```Cache-Control: no-cache```cache-control指明了在请求-回应的所有链路中，都不允许缓存。
>每个浏览器都实现了cache机制，cache机制可以利用已获取的response信息来进行额外的操作（网络通信缓慢又耗时，如果可以利用已有信息对用户体验是巨大的）
,资源被cache后就引入了新的问题，1.缓存过期时间设置为多大比较好呢？2.缓存失效后重新请求页面？<br>
1.过期时间取决于当前资源的更新频率。2.缓存失效后，业界的做法是使用ETag令牌，在第一次缓存时，origin-server会返回一个ETag令牌，可能是某种文件的哈希值，
当缓存失效时，首先浏览器发送请求到服务器去校验这个令牌，服务器会校验当前令牌是否失效，如果未失效则返回304，浏览器继续使用本地缓存，并更新时间。

```Upgrade-Insecure-Requests: 1```指明user-agent告诉origin-server它支持https来获取资源，举个例子：如果使用访问http://www.baidu.com
由于该字段的存在，origin-server就知道了客户端支持https，那么会返回307并携带重定向Url:https://www.baidu.com ，而且会告诉浏览器后续在该host
的资源都可以使用https去请求，例如百度首页的LOGO页面：<src = "//www.baidu.com/img/bd_logo1.png">没有指明协议名称。

# 2.get response from www.baidu.com
接下来收到了响应：
```
HTTP/1.1 200 OK
Bdpagetype: 1
Bdqid: 0xd574c2490000bc57
Cache-Control: private
Content-Encoding: gzip
Content-Type: text/html
Cxy_all: baidu+7868828c386b7abc1a003a22bc77a881
Date: Fri, 21 Dec 2018 11:13:56 GMT
Expires: Fri, 21 Dec 2018 11:13:38 GMT
Server: BWS/1.1
Set-Cookie: delPer=0; path=/; domain=.baidu.com
Set-Cookie: BDSVRTM=0; path=/
Set-Cookie: BD_HOME=0; path=/
Set-Cookie: H_PS_PSSID=1422_21118_28132_27751_28139_27509; path=/; domain=.baidu.com
Strict-Transport-Security: max-age=172800
Vary: Accept-Encoding
X-Ua-Compatible: IE=Edge,chrome=1
Age: 0
Transfer-Encoding: chunked
Via: HTTPS/1.1 wsg-cn-3.hikvision.com
```
```HTTP/1.1 200 OK```指明了协议|版本号、响应码、以及一个文本（reason-parse）。

```Bdpagetype: 1、Bdqid: 0xd574c2490000bc57```属于百度自定义的response header字段。

```Cache-Control: private```注意到request header也有该字段，而且值为no-cache，实际上cache与否最终是由该值决定的，同时private指明不允许中间proxy进行缓存（现代网站大部分
使用了CDN加速）。

```Date```表明这个资源被返回的时间。```Expires```<font color = red>的意思并不是这个资源何时过期，而是在浏览器缓存后，告诉浏览器在截至到expires指定的时间内不去进行过期检测
（注意，这和以上所说的使用ETag进行检测是不同的事情，使用ETag则在指定时间过期后会尝试过期检测，而如果此时并未达到expires所设置的时间，那么过期检测也不会发生）</font>这
里也许存在某个不合理的地方？

```Server: BWS/1.1```指明了Web容器，这里的BWS是百度自行研发的web服务器。

```Strict-Transport-Security: max-age=172800```表明origin-server告诉浏览器，在接下来的172800秒中，在访问当前host下的资源时，必须使用https，即使用户只是输入了www.baidu.com
>很容易注意到的一点是，即使设置了这个字段，在第一次请求中仍旧是非安全的，设想一个场景，一个刚安装完毕的浏览器（在这之前没有对www.baidu.com 的访问），那么第一次访问如果输入了
www.baidu.com 显然会使用重定向机制来进行后续的安全升级，问题就出现在第一次的请求中，如果这是一个需要输入密码的场景，那么显然可能会导致中间人攻击（这种场景十分常见），一个简单
的解决方式为，对于需要输入账号密码的场景，http不对其服务（当然这会导致体验很差），更可行的办法是，先让其进入预备页面，这样触发了第一次请求进行安全升级到https。

```Vary: Accept-Encoding```：origin-server告诉user-agent，当使用ETag进行过期检测时，需要把Accept-Encoding包含在其中。这里存在一个有趣的问题，假设在你所在的地区有
前置的CDN，你使用chrome去访问一个页面，并缓存下来，同时在下一次请求时，CDN进行了前置校验并告诉你页面未更新请继续使用本地页面，然而，当你使用英文版的chrome去访问这个页面时，你会发现
CDN认为你没有进行缓存，而将你的请求向后转发并获取资源然后CDN缓存在本地，这是因为这两个浏览器的Accept-Encoding不一样导致的（zh-CN与en）。

```X-Ua-Compatible: IE=Edge,chrome=1```：告诉user-agent请使用IE的最高模式进行渲染，或者如果user-agent支持或者就是chrome则使用chrome渲染。
>是的，这就是浏览器多年的战争，这个字段属于非标准规定字段。

```Age: 0```：表明响应该请求所耗费时间，为0则表示请求来自本地缓存。

注意到，在返回的响应头中并未看到Etag，
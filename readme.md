# Limit

接口限制。  
用于对API接口进行访问控制，例如限流、分流等。用于鉴权也可以，只是自由度不高。

## 添加

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>limit-spring-boot-starter</artifactId>
>            <version>2.6.1-beta0.1</version>
>        </dependency>
>    </dependencies>
> ```

3. 启用限制

在任意配置类上使用`@EnableLimit`注解开启接口限制。

## 使用

在需要限流的接口上加上`@Limit`注解就OK。例如：

```java
@RestController
@RequestMapping("/public")
public class PublicController {

    @LogIt(message = "test")
    @Limit(key = "test1", type = DefaultLimitHandler.class)
    @GetMapping("/test1")
    public BaseResult<String> test1() {
        return new OkResult<>();
    }

    @LogIt(message = "test2")
    @Limit(key = "test2", type = RandomLimitHandler.class)
    @GetMapping("/test2")
    public BaseResult<String> test2() {
        return new OkResult<>(test());
    }

    public String test() {
        return "test";
    }
}
```

注解上的参数Key表示了接口Key，某些接口共用相同的计数器的话，就用相同的key。参数type表示了要使用的访问限制器。

## 实现`LimitHandler`

`LimitHandler`只有一个方法需要实现`boolean arrived(String key)`，参数Key就是注解上的key值，返回值表示了接口是否可访问。

```java
@Component
@EnableScheduling
public class DefaultLimitHandler implements LimitHandler {

    /**
     * 单位时间内的可访问次数
     */
    private static final Integer COUNT_PER = 10;

    private final Map<String, Integer> countMap;

    public DefaultLimitHandler() {
        countMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean arrived(String key) {
        int count = getCount(key);
        if (count > 0) {
            consume(key);
            return true;
        }
        return false;
    }

    private int getCount(String key) {
        Integer count = countMap.get(key);
        return count == null ? COUNT_PER : count;
    }

    private void consume(String key) {
        countMap.put(key, getCount(key) - 1);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private synchronized void reset() {
        countMap.replaceAll((k, v) -> COUNT_PER);
    }
}
```

## 实现`NotArrivedHandler`

为了客户端更好的体验，请自行实现`NotArrivedHandler`接口。此接口的目的是在需要限制的接口没有处理类或是被限制时返回客户端信息。

## 配置

访问限制器的配置参数如下：
```yaml
station:
  # 接口限制配置
  limit:
    # 是否开启限制策略
    enable: true
```

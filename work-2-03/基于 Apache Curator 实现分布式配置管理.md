# 作业三：基于 Apache Curator 实现分布式配置管理

1. (必须）基于 Apache Curator 对 Zookeeper 操作，以及对 Spring Environment（PropertySources）整合，实现分布式配置管理

2. （可选）基于 1 的基础上，实现在 Spring 场景下的配置变换监听。
提示：基于 Spring ApplicationEvent 来实现，参考：[nacos-spring-context](https://github.com/nacos-group/nacos-spring-project/tree/develop/nacos-spring-context)

3. （可选）基于 1 的基础上，实现配置元数据管理，如：增加配置媒体类型，Content-Type: text/properties、text/json，增加 Content-Length（流媒体）


spring context environment 初始化
1. 通过继承AbstractApplicationContext 重写initPropertySources
2. BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry 注入ConfigurableEnvironment
3. BeanFactoryPostProcessor.postProcessBeanFactory 注入ConfigurableEnvironment
4. LifecycleProcessor.onRefresh 注入ConfigurableEnvironment
5. 监听 ContextRefreshedEvent 
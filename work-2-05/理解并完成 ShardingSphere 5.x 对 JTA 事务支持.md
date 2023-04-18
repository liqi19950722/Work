# ShardingSphere 5.x 对 JTA 事务支持

根据[文档](https://github.com/apache/shardingsphere/blob/master/docs/document/content/dev-manual/transaction.cn.md)可以得出以下内容：

`ShardingSphereTransactionManager` 分布式事务管理器有下面两个子类 默认为**XA**实现

- `XAShardingSphereTransactionManager`
- `SeataATShardingSphereTransactionManager`

`XATransactionManagerProvider` XA分布式事务管理器

- `AtomikosTransactionManagerProvider` 默认实现
- `NarayanaXATransactionManagerProvider`
- `BitronixXATransactionManagerProvider`

## 加载分布式事务管理器

1. 通过`ShardingSphereServiceLoader`加载`ShardingSphereTransactionManager`  
对`java.util.ServiceLoader`的一种封装

    ```java
    ShardingSphereServiceLoader.getServiceInstances(ShardingSphereTransactionManager.class)
    ```

2. 调用`org.apache.shardingsphere.transaction.ShardingSphereTransactionManagerEngine#init`方法  
`TypedSPILoader.getService(XATransactionManagerProvider.class, providerType);`获取`XATransactionManagerProvider`  
`TypedSPILoader`只是对`ShardingSphereServiceLoader`加入了类型检查  
此处会加载到`AtomikosTransactionManagerProvider`

    ```java
    public void init(final Map<String, DatabaseType> databaseTypes, final Map<String, DataSource> dataSources, final String providerType) {
        dataSources.forEach((key, value) -> TypedSPILoader.getService(DataSourcePrivilegeChecker.class, databaseTypes.get(key).getType()).checkPrivilege(value));
        xaTransactionManagerProvider = TypedSPILoader.getService(XATransactionManagerProvider.class, providerType);
        xaTransactionManagerProvider.init();
        Map<String, ResourceDataSource> resourceDataSources = getResourceDataSources(dataSources);
        resourceDataSources.forEach((key, value) -> cachedDataSources.put(value.getOriginalName(), newXATransactionDataSource(databaseTypes.get(key), value)));
    }
    ```

3. 创建`XATransactionDataSource`  
`TypedSPILoader.getService(XADataSourceDefinition.class, databaseType.getType())).swap(dataSource)` 非 XA 数据源转化为 XA 数据源  
`org.apache.shardingsphere.transaction.xa.spi.XATransactionManagerProvider#registerRecoveryResource` 向XATransactionManagerProvider注册`XADataSource`

    ```java
    public XATransactionDataSource(final DatabaseType databaseType, final String resourceName, final DataSource dataSource, final XATransactionManagerProvider xaTransactionManagerProvider) {
        this.databaseType = databaseType;
        this.resourceName = resourceName;
        this.dataSource = dataSource;
        if (!CONTAINER_DATASOURCE_NAMES.contains(dataSource.getClass().getSimpleName())) {
            xaDataSource = new DataSourceSwapper(TypedSPILoader.getService(XADataSourceDefinition.class, databaseType.getType())).swap(dataSource);
            this.xaTransactionManagerProvider = xaTransactionManagerProvider;
            xaTransactionManagerProvider.registerRecoveryResource(resourceName, xaDataSource);
        }
    }
    ```

`XaTransactionManagerProvider` 对应一种XA实现，通过委派的方式交由具体的实现管理分布式事务比如：`AtomikosTransactionManagerProvider`

```java
public interface XATransactionManagerProvider extends TypedSPI, AutoCloseable {
    TransactionManager getTransactionManager();
}
```

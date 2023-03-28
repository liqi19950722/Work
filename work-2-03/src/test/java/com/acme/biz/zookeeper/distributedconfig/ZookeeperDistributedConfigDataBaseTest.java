package com.acme.biz.zookeeper.distributedconfig;

import com.acme.biz.zookeeper.ZookeeperContainerEnv;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigChangedEvent;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.ZookeeperDistributedConfigDataBase;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.Map;

import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase.DEFAULT_APPLICATION_NAME;
import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase.DEFAULT_CONFIG_NAMESPACE;
import static com.acme.biz.zookeeper.distributedconfig.ZookeeperDistributedConfigDataBaseTest.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class ZookeeperDistributedConfigDataBaseTest extends ZookeeperContainerEnv {

    private CuratorFramework curatorFramework;

    private DistributedConfigDataBase distributedConfigDataBase;

    @BeforeAll
    public static void init() throws Exception {
        zookeeperStart();
        createCuratorFramework();
    }

    @BeforeEach
    public void setUp() {
        curatorFramework = getCuratorFramework();
        distributedConfigDataBase = new ZookeeperDistributedConfigDataBase(curatorFramework);
    }
    // /{prefix}/{application}/{profile}/...
    // user.name  -> /config/application/default/user/name
    // user.age  -> /config/application/default/user/age

    // {"user":{"name":"张三","age":"18"}} -> /config/application/default/user/name /config/application/default/user/age

    @Test
    public void should_put_config_via_zookeeper_based_distributed_config() throws Exception {
        doPutConfigData("user.name", "张三", "/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/user/name");
        doPutConfigData("user.age", 18, "/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/user/age");
    }

    private <T extends Serializable> void doPutConfigData(String key, T value, String path) throws Exception {
        distributedConfigDataBase.putConfig(key, value);
        Stat node = curatorFramework.checkExists().forPath(path);

        assertNotNull(node);
        assertEquals(value.getClass().cast(SerializationUtils.deserialize(curatorFramework.getData().forPath(path))), value);
    }

    @Test
    public void should_load_config_from_zookeeper_based_distributed() {
        distributedConfigDataBase.putConfig(KEY_1, VALUE_1);
        distributedConfigDataBase.putConfig(KEY_2, VALUE_2);

        Map<String, String> config = distributedConfigDataBase.loadConfig();
        assertTrue(config.containsKey(KEY_1));
        assertTrue(config.containsKey(KEY_2));
        assertEquals(config.get(KEY_1), VALUE_1);
        assertEquals(config.get(KEY_2), VALUE_2);
    }

    @Test
    public void should_put_config_when_key_is_same() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            distributedConfigDataBase.putConfig(KEY_1, VALUE_1);
            distributedConfigDataBase.putConfig(KEY_1, VALUE_2);
        });
        String path = "/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/" + KEY_1.replace('.', '/');
        byte[] data = curatorFramework.getData().forPath(path);
        assertEquals(VALUE_2, SerializationUtils.deserialize(data));
    }

    @Test
    public void should_listen_node_change_event() throws Exception {
        DistributedConfigEventListener listener = Mockito.mock(DistributedConfigEventListener.class);
        distributedConfigDataBase.registerListener(listener);

        distributedConfigDataBase.putConfig(KEY_1, VALUE_1);
        distributedConfigDataBase.putConfig(KEY_1, VALUE_2);
        distributedConfigDataBase.putConfig(KEY_2, VALUE_2);
        curatorFramework.delete().forPath("/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/" + KEY_2.replace('.', '/'));

        verify(listener,atLeast(1)).onDistributedConfigReceived(any(DistributedConfigChangedEvent.class));
    }

    @AfterAll
    public static void close() {
        getCuratorFramework().close();
        zookeeperClose();
    }

    interface Constant {
        String CONFIG_NAMESPACE = DEFAULT_CONFIG_NAMESPACE;
        String APPLICATION_NAME = DEFAULT_APPLICATION_NAME;
        String KEY_1 = "distributed.config.test";
        String VALUE_1 = "value1";
        String KEY_2 = "distributed.config.test.user.name";
        String VALUE_2 = "value2";
    }
}

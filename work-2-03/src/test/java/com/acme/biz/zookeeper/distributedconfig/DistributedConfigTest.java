package com.acme.biz.zookeeper.distributedconfig;

import com.acme.biz.zookeeper.ZookeeperContainerEnv;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.ZookeeperDistributedConfigDataBase;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Map;

import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase.DEFAULT_APPLICATION_NAME;
import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase.DEFAULT_CONFIG_NAMESPACE;
import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigTest.Constant.APPLICATION_NAME;
import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigTest.Constant.CONFIG_NAMESPACE;
import static org.junit.jupiter.api.Assertions.*;

public class DistributedConfigTest extends ZookeeperContainerEnv {

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
        String key_1 = "distributed.config.test";
        String value_1 = "value1";
        distributedConfigDataBase.putConfig(key_1, value_1);
        String key_2 = "distributed.config.test.user.name";
        String value_2 = "value2";
        distributedConfigDataBase.putConfig(key_2, value_2);

        Map<String, String> config = distributedConfigDataBase.loadConfig();
        assertTrue(config.containsKey("distributed.config.test"));
        assertTrue(config.containsKey("distributed.config.test.user.name"));
        assertEquals(config.get("distributed.config.test"), "value1");
        assertEquals(config.get("distributed.config.test.user.name"), "value2");
    }

    @AfterAll
    public static void close() {
        getCuratorFramework().close();
        zookeeperClose();
    }

    interface Constant {
        String CONFIG_NAMESPACE = DEFAULT_CONFIG_NAMESPACE;
        String APPLICATION_NAME = DEFAULT_APPLICATION_NAME;
    }
}

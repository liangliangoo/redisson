package com.xiongliang.redisson;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author 小熊不会写代码
 * @date 2023/8/5
 */
public class RedissonTest {

    private static RedissonClient redisson;

    @Before
    public void before() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setDatabase(0);
        redisson = Redisson.create(config);
    }

    @Test
    public void testLock() {
        RLock lock = redisson.getLock("redisson:lockMethodWithoutParams");
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId());
            TimeUnit.MINUTES.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

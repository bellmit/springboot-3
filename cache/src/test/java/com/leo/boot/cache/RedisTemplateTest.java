package com.leo.boot.cache;

import com.leo.boot.cache.domain.Info;
import com.leo.boot.cache.domain.InfoExtention;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testValueOps() {
        String key = "key";
        String value = "value";
        redisTemplate.opsForValue().set(key, value);
        Object result = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testObjOps() {
        String key = "info";
        Info value = new Info().id("123").name("张三");
        redisTemplate.opsForValue().set(key, value);
        Object result = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testHOps() {
        String key = "hash";
        InfoExtention value = ((InfoExtention) new InfoExtention().id("123").name("张三")).extention("补充信息");
        redisTemplate.opsForHash().put(key, value.id(), value);
        InfoExtention result = (InfoExtention) redisTemplate.opsForHash().get(key, value.id());
        redisTemplate.delete(key);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testKeys() {
        String keyFormatter = "key:%s";
        String valueFormatter = "value:%s";

        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForValue().set(String.format(keyFormatter, i), String.format(valueFormatter, i));
        }

        Set<String> keys = redisTemplate.keys(String.format(keyFormatter, "*"));
        Assert.assertEquals(10, keys.size());
        redisTemplate.delete(keys);

        keys = redisTemplate.keys(String.format(keyFormatter, "*"));
        Assert.assertNotNull(keys);
    }
}

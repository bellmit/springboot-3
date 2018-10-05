package com.leo.repo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.domain.User;
import com.leo.domain.User.Gender;

@SuppressWarnings({"rawtypes","unchecked"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;	//StringRedisSerializer序列化
    

	@Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() throws Exception {
    	//存
        stringRedisTemplate.opsForValue().set("key", "value");
        //取
        Assert.assertEquals("value", stringRedisTemplate.opsForValue().get("key"));
    }
    
    @Test
    public void testObj() throws Exception {
        User user=new User("张三", "123456", "sanzhang@leo.com","zs",new Date());
        
        ValueOperations<String, User> operations=redisTemplate.opsForValue();
        operations.set("com.leo", user);	//无过期时间
        operations.set("com.leo.limit", user, 1, TimeUnit.SECONDS);	//过期时间
        Thread.sleep(2000);
        
        Assert.assertTrue(redisTemplate.hasKey("com.leo"));
        Assert.assertFalse(redisTemplate.hasKey("com.leo.limit"));
        
//        redisTemplate.delete("com.leo");	//删除
    }
    
   //String.format null不会报错
   @SuppressWarnings("all")
   @Test
   public void testStringFormat(){
	   String format = "test:%s";
	   redisTemplate.opsForValue().set(String.format(format, null), 1);
	   redisTemplate.delete(String.format(format, null));
   }
   
   //JsonIgnore对redis序列化也有效
   @Test
   public void testJsonSerialize(){
	   User user = new User();
	   user.setGender(Gender.MALE);
	   
//	   redisTemplate.opsForValue().set(User.TEST, user);
   }
   
   @SuppressWarnings("all")
   @Test
   public void testMulget(){
	   List get = redisTemplate.opsForValue().multiGet(Arrays.asList("1","2","3","4","5"));
	   System.out.println(get);	//没有会返回null
   }
}
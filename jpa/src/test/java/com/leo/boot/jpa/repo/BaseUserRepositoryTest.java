package com.leo.boot.jpa.repo;

import com.leo.boot.jpa.domain.User;
import com.leo.boot.jpa.domain.UserVO;
import com.leo.boot.jpa.enumeration.Gender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void testSave() {
        User user = new User().setName("张三").setNick("小张").setGender(Gender.MALE).setAccount(100.00).setTemp("temp");
        userRepository.save(user);
    }

    @After
    public void testDeleteByName() {
        userRepository.deleteByName("张三");
    }

    @Test
    public void testModify() {
        User user = userRepository.findByName("张三").get(0);
        user.setNick("nick");
        assertEquals(0, user.getVersion().intValue());
        user = userRepository.save(user);
        assertEquals(1, user.getVersion().intValue());
    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testConflict() {
        User user = userRepository.findByName("张三").get(0);
        assertEquals(0, user.getVersion().intValue());

        user.setName("李四");
        userRepository.save(user);

        user.setName("王五");
        userRepository.save(user);
    }

    @Test
    public void testFindByName() {
        User user = userRepository.findByName("张三").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testFindByNameOrNick() {
        User user = userRepository.findByNameOrNick("张三", "").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testFindByNameAndNick() {
        User user = userRepository.findByNameOrNick("张三", "小张").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testFindByNameByHQL() {
        User user = userRepository.findByNameByHQL("张三").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testModifyNickByNameByHQL() {
        userRepository.modifyNickByNameByHQL("三", "张三");
        User user = userRepository.findByNameOrNick("", "三").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testFindByNameByNative() {
        User user = userRepository.findByNameByNative("张三").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void testModifyNickByNameByNative() {
        userRepository.modifyNickByNameByNative("三", "张三");
        User user = userRepository.findByNameOrNick("", "三").get(0);
        assertEquals("张三", user.getName());
    }

    @Test
    public void findByNameByProjection() {
        UserVO user = userRepository.findByNameByProjection("张三").get(0);
        assertEquals("张三", user.getName());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals(100.00, user.getAccount(), 0.01);
    }

}

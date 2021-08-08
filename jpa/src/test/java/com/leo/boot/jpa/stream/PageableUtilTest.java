package com.leo.boot.jpa.stream;

import com.leo.boot.jpa.domain.User;
import com.leo.boot.jpa.enumeration.Gender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PageableUtilTest {

    List<User> list;

    @BeforeEach
    void setUp() {
        User user1 = new User().setName("张三").setVersion(1).setGender(Gender.MALE);
        User user2 = new User().setName("李四").setVersion(3).setGender(Gender.FEMALE);
        User user3 = new User().setName("王五").setVersion(5).setGender(Gender.MALE);
        User user4 = new User().setName("赵六").setVersion(2).setGender(Gender.FEMALE);
        User user5 = new User().setName("田七").setVersion(2).setGender(Gender.MALE);
        list = Arrays.asList(user1, user2, user3, user4, user5);
    }

    @Test
    void getPage() {
        Pageable pageable1 = PageRequest.of(0, 10);
        Page<User> page1 = PageableUtil.getPage(list, pageable1);

        Assertions.assertEquals(5, page1.getTotalElements());
        Assertions.assertEquals(5, page1.getNumberOfElements());
        Assertions.assertEquals(1, page1.getTotalPages());

        Pageable pageable2 = PageRequest.of(1, 3, Sort.Direction.ASC, "gender", "version");
        Page<User> page2 = PageableUtil.getPage(list, pageable2);

        Assertions.assertEquals(5, page2.getTotalElements());
        Assertions.assertEquals(2, page2.getNumberOfElements());
        Assertions.assertEquals(2, page2.getTotalPages());

        Assertions.assertEquals(2, page2.getContent().get(0).getVersion());
        Assertions.assertEquals(3, page2.getContent().get(1).getVersion());
    }

    @Test
    void getComparator() {
        Sort sort = Sort.by(Sort.Direction.ASC, "gender").and(Sort.by(Sort.Direction.DESC, "version"));
        List<User> users = list.stream().sorted(PageableUtil.getComparator(sort, User.class)).collect(Collectors.toList());

        Assertions.assertEquals(5, users.get(0).getVersion());
        Assertions.assertEquals(2, users.get(1).getVersion());
        Assertions.assertEquals(1, users.get(2).getVersion());
        Assertions.assertEquals(3, users.get(3).getVersion());
        Assertions.assertEquals(2, users.get(4).getVersion());
    }
}
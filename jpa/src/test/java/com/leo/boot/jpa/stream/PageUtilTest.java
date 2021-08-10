package com.leo.boot.jpa.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

enum Gender {
    MALE, FEMALE;
}

class PageUtilTest {

    List<User> users;

    @BeforeEach
    void setUp() {
        User user1 = User.builder().id(1).name("张三").gender(Gender.MALE).build();
        User user2 = User.builder().id(2).name("李四").gender(Gender.FEMALE).build();
        User user3 = User.builder().id(3).name("王五").gender(Gender.MALE).build();
        User user4 = User.builder().id(4).name("赵六").gender(Gender.FEMALE).build();
        User user5 = User.builder().id(5).name("田七").gender(Gender.MALE).build();
        User empty = new User();
        users = Arrays.asList(empty, user1, user3, user5, user2, user4, null);
    }

    @Test
    void getPage() {
        Pageable pageable1 = PageRequest.of(0, 10);
        Page<User> page1 = PageUtil.getPage(users, pageable1);

        Assertions.assertEquals(7, page1.getTotalElements());
        Assertions.assertEquals(7, page1.getNumberOfElements());
        Assertions.assertEquals(1, page1.getTotalPages());

        Pageable pageable2 = PageRequest.of(1, 3, Sort.Direction.ASC, "gender", "id");
        Page<User> page2 = PageUtil.getPage(users, pageable2);

        Assertions.assertEquals(7, page2.getTotalElements());
        Assertions.assertEquals(3, page2.getNumberOfElements());
        Assertions.assertEquals(3, page2.getTotalPages());

        Assertions.assertEquals(2, page2.getContent().get(0).getId());
        Assertions.assertEquals(4, page2.getContent().get(1).getId());
    }

    @Test
    void getComparator() {
        Sort sort = Sort.by(Sort.Direction.ASC, "gender").and(Sort.by(Sort.Direction.DESC, "id"));
        List<User> users = this.users.stream().sorted(PageUtil.getComparator(sort, User.class)).collect(Collectors.toList());

        Assertions.assertEquals(5, users.get(0).getId());
        Assertions.assertEquals(3, users.get(1).getId());
        Assertions.assertEquals(1, users.get(2).getId());
        Assertions.assertEquals(4, users.get(3).getId());
        Assertions.assertEquals(2, users.get(4).getId());
        Assertions.assertNull(users.get(5).getId());
        Assertions.assertNull(users.get(6));
    }

    @Test
    void illegalArgument() {
        PageRequest pageable = PageRequest.of(1, 3, Sort.Direction.ASC, "id", "illegalArgument");
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageUtil.getPage(users, pageable));
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class User {
    private Integer id;
    private Gender gender;
    private String name;
}
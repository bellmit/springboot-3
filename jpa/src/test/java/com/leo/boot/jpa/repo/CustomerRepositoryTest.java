package com.leo.boot.jpa.repo;

import com.leo.boot.jpa.domain.Customer;
import com.leo.boot.jpa.domain.QCustomer;
import com.leo.boot.jpa.stream.PageUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EntityManager entityManager;

    @Before
    public void testSave() {
        Customer zhangsan = new Customer().setFirstName("zhang").setLastName("san");
        Customer lisi = new Customer().setFirstName("li").setLastName("si");
        customerRepository.saveAll(Arrays.asList(zhangsan, lisi));
    }

    @Test
    public void test() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        QCustomer customer = QCustomer.customer;
        Predicate predicate = customer.lastName.like("%i%");

        // query1
        Page<Customer> customers1 = customerRepository.findAll(predicate, pageRequest);
        // query2
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<Customer> customerList = queryFactory.select(customer).from(customer).where(predicate).fetch();
        Page<Customer> customers2 = PageUtil.getPage(customerList, pageRequest);

        Assert.assertEquals(customers1, customers2);
    }
}
package com.leo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.leo.domain.User;
import com.leo.repo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRepository {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testInsert() {
		//insert
		userRepository.save(new User("张三", "123456", "sanzhang@leo.com","zs",new Date()));
		userRepository.save(new User("李四", "123456", "sili@leo.com","ls",new Date()));
		userRepository.save(new User("王五", "123456", "wuwang@leo.com","ww",new Date()));
		userRepository.save(new User("王六", "123456", "wuwang6@leo.com","ww6",new Date()));
		userRepository.save(new User("王七", "123456", "wuwang7@leo.com","ww7",new Date()));
		userRepository.save(new User("王八", "123456", "wuwang8@leo.com","ww8",new Date()));
	}

	@Test
	public void testUpdate() {
		//update
		User user = userRepository.findByUserName("王五");
		user.setNickName("new");
		userRepository.save(user);
		
		//hql update
		User user2 = userRepository.findByUserName("王五");
		userRepository.modifyByIdAndUserId("王九", user2.getId());
	}
	
	@Test
	public void testDelete() {
		userRepository.delete(userRepository.findByUserName("张三"));
		userRepository.delete(userRepository.findByUserNameOrEmail(null, "sili@leo.com"));
	}
	
	@Test
	public void testQuery() {
		//findAll
		List<User> userList = userRepository.findAll();
		System.out.println(userList);
		//分页
		int page = 0;
		int size = 3;
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC,"id"));
		Page<User> users = userRepository.findAll(pageable);
		Page<User> users2 = userRepository.findByOrderByIdDesc(pageable);//和上面等效
		System.out.println(users.getContent());
		System.out.println(users2.getContent());
		
		//hql
		User user2 = userRepository.findByEmailAddress("wuwang6@leo.com");
		System.out.println(user2);
		
		//sql
		List<User> list = userRepository.FindByNickNameBySql("new");
		System.out.println(list);
	}
	
	//测试多条件查询
	@Test
	public void testConditionsQuery() {
		User user = userRepository.findByUserNameAndNickNameAndEmail("张三", null, null);
		Assert.assertNull(user);
	}
	
	//测试动态查询
	@Test
	public void testDynamiQuery() {
		User user = new User();
		user.setUserName("王");
		
		int page = 0;
		int size = 3;
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC,"id"));
		
		Page<User> list = userRepository.findAll(new Specification<User>() {
			
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				ArrayList<Predicate> predicates = new ArrayList<Predicate>();
				
				if(!StringUtils.isEmpty(user.getUserName())){
					predicates.add(cb.like(root.get("userName"), "%"+user.getUserName()+"%"));
				}
				if(!StringUtils.isEmpty(user.getNickName())){
					predicates.add(cb.like(root.get("nickName"), "%"+user.getNickName()+"%"));
				}
				
				query.where(predicates.toArray(new Predicate[predicates.size()]));
				
				return null;
			}
		}, pageable);
		
		System.out.println(list);
	}
	
	@Test
	public void testOnetoOne(){
		User user = userRepository.findByAddress_Code("1");
		System.out.println(user);
		User user2 = userRepository.findByByAddressCode("1");
		System.out.println(user2);
	}

	@Test
	public void testAll() throws Exception {
		
		testInsert();
		
		testUpdate();
		
		testQuery();

		testDelete();
	}
	
}
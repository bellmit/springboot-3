package com.leo.boot.jpa.repo;

import com.leo.boot.jpa.domain.User;
import com.leo.boot.jpa.domain.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    List<User> findByName(String name);

    List<User> findByNameOrNick(String name, String nick);

    List<User> findByNameAndNick(String name, String nick);

    @Transactional
    void deleteByName(String name);

    /******************** hql begin ********************/
    @Query("select u from User u where u.name = :name")
    List<User> findByNameByHQL(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("update User u set u.nick = :nick where u.name = :name")
    void modifyNickByNameByHQL(@Param("nick") String nick, @Param("name") String name);
    /******************** hql end ********************/

    /******************** native sql begin ********************/
    @Query(value = "select * from t_user where name = :name", nativeQuery = true)
    List<User> findByNameByNative(@Param("name") String name);

    @Transactional
    @Modifying
    @Query(value = "update t_user set nick = :nick where name = :name", nativeQuery = true)
    void modifyNickByNameByNative(@Param("nick") String nick, @Param("name") String name);

    @Query(value = "select * from t_user where name = :name", nativeQuery = true)
    List<UserProjection> findByNameByProjection(@Param("name") String name);
    /******************** native sql end ********************/

}
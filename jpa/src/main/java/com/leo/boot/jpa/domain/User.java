package com.leo.boot.jpa.domain;

import com.leo.boot.jpa.enumeration.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "t_user")
public class User extends BaseEntity<String> {
    private String name;
    private String nick;
    private Double account;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Transient
    private String temp;

    @Version
    private Integer version;

    private Date time;
}

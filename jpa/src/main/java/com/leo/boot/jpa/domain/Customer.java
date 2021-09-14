package com.leo.boot.jpa.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "t_customer")
public class Customer extends BaseEntity<String> {
    private String firstName;
    private String lastName;
}

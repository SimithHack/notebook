package com.test.dubbo.myserviceconsumer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String desc;
    private Integer age;

}

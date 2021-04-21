package com.shanelee.springboot.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author: manji
 * @data: 2021/4/20
 */
@Data
public class Base {
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
}

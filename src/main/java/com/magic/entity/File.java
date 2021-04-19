package com.magic.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author magic_lz
 * @version 1.0
 * @classname File
 * @date 2021/4/19 : 18:00
 */
@Data
public class File {
     private Integer id;
     private String fileName;
     private Integer fileSize;
     private String fileUrl;
     private Date createTime;
     private Integer downloadCount;
     private String serverIp;
}

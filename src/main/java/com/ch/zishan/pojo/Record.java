package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_record")
public class Record implements Serializable {

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    private Long time;
    private Integer learnedNum;
    private Integer addNum;
    private Integer editNum;
}

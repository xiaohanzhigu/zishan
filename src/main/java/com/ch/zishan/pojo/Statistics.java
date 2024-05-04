package com.ch.zishan.pojo;


import cn.hutool.core.lang.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cardCount;
    private int learnedCount;
    private int collectCount;
    private int todayEditCount;
    private int todayLearnedCount;
    private Dict chartData;
}

package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class FilmDetailVO implements Serializable {

    private String filmName;//影片名称
    private String filmEnName;//英文名称
    private String imgAddress;//图片地址
    private String score;//评分
    private String scoreNum ;//评分人数
    private String totalBox ;//总票房
    private String info01 ;//详细信息1
    private String info02 ;//详细信息2
    private String info03 ;//详细信息3
}

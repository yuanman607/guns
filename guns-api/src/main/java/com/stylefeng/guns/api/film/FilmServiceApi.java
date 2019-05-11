package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceApi {
    //获取banners
    List<BannerVO> getBanners();
    //获取热映影片(根据受欢迎程度排序)
    FilmVO getHotFilms(boolean isLimit,int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //获取即将上映的影片
    FilmVO getSoonFilms(boolean isLimit,int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //获取经典影片
    FilmVO getClassicFilms(int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //获取热映影片(正式环境推荐)
//    FilmVO getHotFilms(int nowPage,int nums , ...);
    //获取票房排行榜
    List<FilmInfo> getBoxRanking();
    //获取人气排行榜
    List<FilmInfo> getExpectRanking();
    //获取top100
    List<FilmInfo> getTop();
    //=====获取影片条件接口
    //分类条件
    List<CatVO>getCats();
    //片源条件
    List<SourceVO>getSources();
    //年代条件
    List<YearVO>getYears();
    //根据影片ID获取影片信息
    FilmDetailVO getFilmDetail(int searchType,String searchParam);
    //获取影片相关的其他信息【演员表/图片地址。。。】
}

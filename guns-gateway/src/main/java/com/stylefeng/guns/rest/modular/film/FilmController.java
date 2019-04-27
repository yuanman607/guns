package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;

import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String imgPre="http://img.meetingshop.cn/";

    @Reference(interfaceClass = FilmServiceApi.class,check = false)
    private FilmServiceApi filmServiceApi;
    /**
     * API网关，接口功能聚合
     * 好处：六个接口，一次请求，只进行一次http请求
     * 2同一个接口对外暴露
     * @return
     */
    //首页接口，获取首页信息,get请求
    @RequestMapping(value = "getIndex",method = RequestMethod.GET)
    public ResponseVO getIndex(){
        FilmIndexVO filmIndexVO=new FilmIndexVO();
        //获取banner（轮播）信息
        filmIndexVO.setBanners(filmServiceApi.getBanners());
        //获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(true,8));
        //获取即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(true,8));
        //票房排行榜
        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());
        //人气榜
        filmIndexVO.setExpectRanking(filmServiceApi.getExpectRanking());
        //获取前评分前一百
        filmIndexVO.setTop100(filmServiceApi.getTop());

        return ResponseVO.success(imgPre,filmIndexVO);
    }

    //2、影片条件列表查询接口
    @RequestMapping(value="getConditionList",method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name="catId",required = false,defaultValue = "99")String catId,
                                       @RequestParam(name="sourceId",required = false,defaultValue = "99")String sourceId,
                                       @RequestParam(name="yearId",required = false,defaultValue = "99")String yearId){
        FilmConditionVO filmConditionVO=new FilmConditionVO();
        //类型集合
        List<CatVO>cats=filmServiceApi.getCats();
        boolean flag=false;
        List<CatVO>catResult=new ArrayList<>();
        CatVO catVO=null;
        for (CatVO cat:cats) {
            //判断集合是否存在catId，如果存在则对应的实体变成active状态-->优化：1.数据层查询按id排序2，通过二分法查找
            if (cat.getCatId().equals("99")){
                catVO=cat;
                continue;
            }
            if(cat.getCatId().equals(catId)){
                flag=true;
                cat.setActive(true);
            }
            catResult.add(cat);
            //如果不存在，则全部默认为active状态
            if(flag){
                catVO.setActive(true);
                catResult.add(catVO);
            }
        }
        //片源集合
        List<SourceVO>sources=filmServiceApi.getSources();
        flag=false;
        List<SourceVO>sourceResult=new ArrayList<>();
        SourceVO sourceVO=null;
        for (SourceVO source:sources) {
            //判断集合是否存在catId，如果存在则对应的实体变成active状态-->优化：1.数据层查询按id排序2，通过二分法查找
            if (source.getSourceId().equals("99")){
                sourceVO=source;
                continue;
            }
            if(source.getSourceId().equals(sourceId)){
                flag=true;
                source.setActive(true);
            }
            sourceResult.add(source);
            //如果不存在，则全部默认为active状态
            if(flag){
                sourceVO.setActive(true);
                sourceResult.add(sourceVO);
            }
        }


        //年代集合
        List<YearVO>years=filmServiceApi.getYears();
        flag=false;
        List<YearVO>yearResult=new ArrayList<>();
        YearVO yearVO =null;
        for (YearVO year:years) {
            //判断集合是否存在catId，如果存在则对应的实体变成active状态-->优化：1.数据层查询按id排序2，通过二分法查找
            if (year.getYearId().equals("99")){
                yearVO=year;
                continue;
            }
            if(year.getYearId().equals(sourceId)){
                flag=true;
                year.setActive(true);
            }
            yearResult.add(year);
            //如果不存在，则全部默认为active状态
            if(flag){
                yearVO.setActive(true);
                yearResult.add(yearVO);
            }
        }
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);

        return ResponseVO.success(filmConditionVO);
    }
    @RequestMapping(value="getFilms",method = RequestMethod.GET)
    public ResponseVO getFilms(){




        return null;
    }

}

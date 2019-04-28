package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {

    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO>result=new ArrayList<BannerVO>();

        List<MoocBannerT>moocBanners=moocBannerTMapper.selectList(null);
        for (MoocBannerT moocBannerT:moocBanners) {
            BannerVO bannerVO=new BannerVO();
            bannerVO.setBannerId(moocBannerT.getUuid()+"");
            bannerVO.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVO.setBannerAddress(moocBannerT.getBannerAddress());
            result.add(bannerVO);
        }
        return result;
    }

    private List<FilmInfo>getFilmInfos(List<MoocFilmT> moocFilms){
        List<FilmInfo> filmInfos=new ArrayList<FilmInfo>();
        for (MoocFilmT moocFilmT:moocFilms) {
            FilmInfo filmInfo =new FilmInfo();
            filmInfo.setScore(moocFilmT.getFilmScore());
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmScore(moocFilmT.getFilmScore());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid()+"");
            filmInfo.setExpectNum(moocFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums,int nowPage,int sortId,int sourceId,int yearId,int catId) {
        FilmVO filmVO=new FilmVO();
        List<FilmInfo>filmInfos=new ArrayList<FilmInfo>();

        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","1");
        if(isLimit){
            Page<MoocFilmT> page=new Page<MoocFilmT>(1,nums);
            List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
            //组织filmInfos
            filmInfos=getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfoList(filmInfos);
        }else{
            //
            Page<MoocFilmT> page=null;
            //根据sortId的不同，来组织不同的page对象
            //1-按热门搜索。2-按时间搜索。3-按评价搜索
            switch(sortId){
                case 2:
                    page=new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    new Page<>(nowPage,nums,"film_score");
                    break;
                default:
                    page=new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            if(sourceId!=99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId!=99){
                entityWrapper.eq("film_date",yearId);
            }

            if(catId!=99){
                String catStr="%#"+catId+"#%";
                entityWrapper.like("film_cats",catStr);
            }
            List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
            //组织filmInfos
            filmInfos=getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());

            //总页数
            int totalPages=0;//每页10条，当前有n条 totalPages=n/10+1;
            int totalCounts=moocFilmTMapper.selectCount(entityWrapper);
            totalPages=(totalCounts/nums)+1;
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
            filmVO.setFilmInfoList(filmInfos);
        }
        //判断是否为首页需要的内容
            //如果是，限制条数，及内容为热映影片
            // 如果不是，限制条数
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums,int nowPage,int sortId,int sourceId,int yearId,int catId) {
        FilmVO filmVO=new FilmVO();
        List<FilmInfo>filmInfos=new ArrayList<FilmInfo>();

        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","2");
        if(isLimit){
            Page<MoocFilmT> page=new Page<MoocFilmT>(1,nums);
            List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
            //组织filmInfos
            filmInfos=getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfoList(filmInfos);
        }else{
            Page<MoocFilmT> page=null;
            //根据sortId的不同，来组织不同的page对象
            //1-按热门搜索。2-按时间搜索。3-按评价搜索
            switch(sortId){
                case 2:
                    page=new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    new Page<>(nowPage,nums,"film_score");
                    break;
                default:
                    page=new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            if(sourceId!=99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId!=99){
                entityWrapper.eq("film_date",yearId);
            }

            if(catId!=99){
                String catStr="%#"+catId+"#%";
                entityWrapper.like("film_cats",catStr);
            }
            List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
            //组织filmInfos
            filmInfos=getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());

            //总页数
            int totalPages=0;//每页10条，当前有n条 totalPages=n/10+1;
            int totalCounts=moocFilmTMapper.selectCount(entityWrapper);
            totalPages=(totalCounts/nums)+1;
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
            filmVO.setFilmInfoList(filmInfos);
        }
        //判断是否为首页需要的内容
        //如果是，限制条数，及内容为热映影片
        // 如果不是，限制条数
        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        //条件--》已经上映的，票房前10名
        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","1");
        Page<MoocFilmT>page =new Page<>(1,10,"film_box_office");
        List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo>filmInfos=getFilmInfos(moocFilms);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        //即将上映的，预售前十名
        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","2");
        Page<MoocFilmT>page =new Page<>(1,10,"film_preSaleNum");
        List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo>filmInfos=getFilmInfos(moocFilms);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        //——评分前十名
        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","1");
        Page<MoocFilmT>page =new Page<>(1,10,"film_score");
        List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo>filmInfos=getFilmInfos(moocFilms);
        return filmInfos;
    }

    @Override
    public List<CatVO> getCats() {
        List<CatVO> catVOs=new ArrayList<>();
        //step1查询实体对象 MoocCatDictT
        List<MoocCatDictT> moocCatDicts=moocCatDictTMapper.selectList(null);
        for (MoocCatDictT moocCatDictT:moocCatDicts){
            CatVO catVO=new CatVO();
            catVO.setCatId(moocCatDictT.getUuid()+"");
            catVO.setCatName(moocCatDictT.getShowName());
            catVOs.add(catVO);
        }
        //step2将实体对象转换成业务对象
        return catVOs;
    }

    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sourceVos=new ArrayList<>();
        List<MoocSourceDictT> moocSources=moocSourceDictTMapper.selectList(null);


        for (MoocSourceDictT moocSourceDictT:moocSources){
            SourceVO sourceVO=new SourceVO();
            sourceVO.setSourceId(moocSourceDictT.getUuid()+"");
            sourceVO.setSourceName(moocSourceDictT.getShowName());
            sourceVos.add(sourceVO);
        }

        return sourceVos;
    }

    @Override
    public List<YearVO> getYears() {
        List<YearVO>yearVOs=new ArrayList<>();
        List<MoocYearDictT>moocYears=moocYearDictTMapper.selectList(null);
        for (MoocYearDictT moocYearDictT:moocYears){
            YearVO yearVO=new YearVO();
            yearVO.setYearId(moocYearDictT.getUuid()+"");
            yearVO.setYearName(moocYearDictT.getShowName());
            yearVOs.add(yearVO);
        }
        return yearVOs;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO=new FilmVO();
        List<FilmInfo>filmInfos=new ArrayList<FilmInfo>();
        EntityWrapper<MoocFilmT> entityWrapper=new EntityWrapper<MoocFilmT>();
        entityWrapper.eq("film_status","2");
        Page<MoocFilmT> page=null;
        //根据sortId的不同，来组织不同的page对象
        //1-按热门搜索。2-按时间搜索。3-按评价搜索
        switch(sortId){
            case 2:
                page=new Page<>(nowPage,nums,"film_time");
                break;
            case 3:
                new Page<>(nowPage,nums,"film_score");
                break;
            default:
                page=new Page<>(nowPage,nums,"film_box_office");
                break;
        }
        if(sourceId!=99){
            entityWrapper.eq("film_source",sourceId);
        }
        if(yearId!=99){
            entityWrapper.eq("film_date",yearId);
        }

        if(catId!=99){
            String catStr="%#"+catId+"#%";
            entityWrapper.like("film_cats",catStr);
        }
        List<MoocFilmT>moocFilms=moocFilmTMapper.selectPage(page,entityWrapper);
        //组织filmInfos
        filmInfos=getFilmInfos(moocFilms);
        filmVO.setFilmNum(moocFilms.size());

        //总页数
        int totalPages=0;//每页10条，当前有n条 totalPages=n/10+1;
        int totalCounts=moocFilmTMapper.selectCount(entityWrapper);
        totalPages=(totalCounts/nums)+1;
        filmVO.setTotalPage(totalPages);
        filmVO.setNowPage(nowPage);
        filmVO.setFilmInfoList(filmInfos);
        return filmVO;
    }
}

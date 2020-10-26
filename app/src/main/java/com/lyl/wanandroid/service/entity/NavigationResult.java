package com.lyl.wanandroid.service.entity;


import com.lyl.wanandroid.base.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class NavigationResult extends BaseResult implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * articles : [{"apkLink":"","audit":1,"author":"小编","canEdit":false,"chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":8104,"link":"https://maven.aliyun.com/mvn/search","niceDate":"2019-03-20 23:27","niceShareDate":"未知时间","origin":"","prefix":"","projectLink":"","publishTime":1553095634000,"realSuperChapterId":0,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":0,"superChapterName":"","tags":[],"title":"maven仓库 阿里云托管","type":0,"userId":-1,"visible":1,"zan":0}]
         * cid : 272
         * name : 常用网站
         */

        private int cid;
        private String name;
        private List<ArticleBean> articles;

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ArticleBean> getArticles() {
            return articles;
        }

        public void setArticles(List<ArticleBean> articles) {
            this.articles = articles;
        }

    }
}

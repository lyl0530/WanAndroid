package com.lyl.wanandroid.service.entity;

import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/7/12
 * Describe : 首页文章的返回结果
 */
public class MainArticleResult extends BaseResult {
    /**
     * data : {"curPage":1,"datas":[{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":459,"chapterName":"Activity","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":true,"id":14298,"link":"https://www.jianshu.com/p/e3c89f62d49f","niceDate":"7小时前","niceShareDate":"11小时前","origin":"","prefix":"","projectLink":"","publishTime":1594745425000,"realSuperChapterId":152,"selfVisible":0,"shareDate":1594732828000,"shareUser":"深红骑士","superChapterId":173,"superChapterName":"framework","tags":[],"title":"Activity的启动过程详解（基于10.0源码）","type":0,"userId":29303,"visible":1,"zan":0}],"offset":0,"over":false,"pageCount":443,"size":20,"total":8854}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * curPage : 1
         * datas : [{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":459,"chapterName":"Activity","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":true,"id":14298,"link":"https://www.jianshu.com/p/e3c89f62d49f","niceDate":"7小时前","niceShareDate":"11小时前","origin":"","prefix":"","projectLink":"","publishTime":1594745425000,"realSuperChapterId":152,"selfVisible":0,"shareDate":1594732828000,"shareUser":"深红骑士","superChapterId":173,"superChapterName":"framework","tags":[],"title":"Activity的启动过程详解（基于10.0源码）","type":0,"userId":29303,"visible":1,"zan":0}]
         * offset : 0
         * over : false
         * pageCount : 443
         * size : 20
         * total : 8854
         */

        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<ArticleBean> datas;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ArticleBean> getDatas() {
            return datas;
        }

        public void setDatas(List<ArticleBean> datas) {
            this.datas = datas;
        }

        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    ", datas=" + datas +
                    '}';
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "MainArticleResult{" +
                "data=" + data +
                '}';
    }
}

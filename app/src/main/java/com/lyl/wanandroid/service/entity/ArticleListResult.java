package com.lyl.wanandroid.service.entity;

import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/9/25
 * Describe : 项目Tab里的文章列表
 */
public class ArticleListResult extends BaseResult {

    /**
     * data : {"curPage":1,"datas":[{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":51,"link":"http://blog.csdn.net/liuhe688/article/details/7050868","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之八：创建及调用自己的ContentProvider","type":0,"userId":-1,"visible":1,"zan":0}],"offset":0,"over":true,"pageCount":1,"size":20,"total":6}
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
         * datas : [{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":51,"link":"http://blog.csdn.net/liuhe688/article/details/7050868","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之八：创建及调用自己的ContentProvider","type":0,"userId":-1,"visible":1,"zan":0}]
         * offset : 0
         * over : true
         * pageCount : 1
         * size : 20
         * total : 6
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
                    "curPage=" + curPage +
                    ", offset=" + offset +
                    ", over=" + over +
                    ", pageCount=" + pageCount +
                    ", size=" + size +
                    ", total=" + total +
                    ", datas=" + datas +
                    '}';
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "ArticleListResult{" +
                "data=" + data +
                '}';
    }
}

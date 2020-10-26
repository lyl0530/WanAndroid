package com.lyl.wanandroid.service.entity;

import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/9/25
 * Describe : 项目Tab里的文章列表
 */
public class ProjectArticleListResult extends BaseResult {

    /**
     * data : {"curPage":1,"datas":[{"apkLink":"","audit":1,"author":"wangjianxiandev","canEdit":false,"chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"简单天气APP，使用彩云天气api，使用Kotlin语言基于MVVM模式结合JetPack组件库：LiveData、ViewModel、Lifecycle、Navigation、Room组件，以及使用协程+Retrofit进行网络请求 开发的一款天气app","descMd":"","envelopePic":"https://www.wanandroid.com/blogimgs/1493b21f-34d7-4e0f-b33d-1ebe1b432691.png","fresh":false,"id":15181,"link":"https://www.wanandroid.com/blog/show/2786","niceDate":"2020-09-09 23:42","niceShareDate":"2020-09-09 23:42","origin":"","prefix":"","projectLink":"https://github.com/wangjianxiandev/Weather","publishTime":1599666121000,"realSuperChapterId":293,"selfVisible":0,"shareDate":1599666121000,"shareUser":"","superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"简单天气--Kotlin+JetPack+协程+MVVM架构","type":0,"userId":-1,"visible":1,"zan":0}],"offset":0,"over":false,"pageCount":13,"size":15,"total":181}
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
         * datas : [{"apkLink":"","audit":1,"author":"wangjianxiandev","canEdit":false,"chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"简单天气APP，使用彩云天气api，使用Kotlin语言基于MVVM模式结合JetPack组件库：LiveData、ViewModel、Lifecycle、Navigation、Room组件，以及使用协程+Retrofit进行网络请求 开发的一款天气app","descMd":"","envelopePic":"https://www.wanandroid.com/blogimgs/1493b21f-34d7-4e0f-b33d-1ebe1b432691.png","fresh":false,"id":15181,"link":"https://www.wanandroid.com/blog/show/2786","niceDate":"2020-09-09 23:42","niceShareDate":"2020-09-09 23:42","origin":"","prefix":"","projectLink":"https://github.com/wangjianxiandev/Weather","publishTime":1599666121000,"realSuperChapterId":293,"selfVisible":0,"shareDate":1599666121000,"shareUser":"","superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"简单天气--Kotlin+JetPack+协程+MVVM架构","type":0,"userId":-1,"visible":1,"zan":0}]
         * offset : 0
         * over : false
         * pageCount : 13
         * size : 15
         * total : 181
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
        return "ProjectArticleListResult{" +
                "data=" + data +
                '}';
    }
}

package com.lyl.wanandroid.service.entity;

import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/9/25
 * Describe : 项目Tab里的文章列表
 */
public class CollectListResult extends BaseResult {

    /**
     * data : {"curPage":1,"datas":[{"author":"code小生","chapterId":414,"chapterName":"code小生","courseId":13,"desc":"","envelopePic":"","id":145641,"link":"https://mp.weixin.qq.com/s/esqAFOpFstym2pnhLXqNXA","niceDate":"2020-07-23 22:02","origin":"","originId":14453,"publishTime":1595512962000,"title":"RecyclerView 的曝光统计","userId":70285,"visible":0,"zan":0}],"offset":0,"over":false,"pageCount":2,"size":20,"total":33}
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
         * datas : [{"author":"code小生","chapterId":414,"chapterName":"code小生","courseId":13,"desc":"","envelopePic":"","id":145641,"link":"https://mp.weixin.qq.com/s/esqAFOpFstym2pnhLXqNXA","niceDate":"2020-07-23 22:02","origin":"","originId":14453,"publishTime":1595512962000,"title":"RecyclerView 的曝光统计","userId":70285,"visible":0,"zan":0}]
         * offset : 0
         * over : false
         * pageCount : 2
         * size : 20
         * total : 33
         */

        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<Article1Bean> datas;

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

        public List<Article1Bean> getDatas() {
            return datas;
        }

        public void setDatas(List<Article1Bean> datas) {
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
        return "CollectListResult{" +
                "data=" + data +
                '}';
    }
}

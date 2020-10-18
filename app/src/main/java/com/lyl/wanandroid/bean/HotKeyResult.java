package com.lyl.wanandroid.bean;

import com.lyl.wanandroid.base.BaseResult;

import java.util.List;

/**
 * Created by lym on 2020/3/29
 * Describe : 热词搜索的result
 */
public class HotKeyResult extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 6
         * link :
         * name : 面试
         * order : 1
         * visible : 1
         */

        private int id;
        private String link;
        private String name;
        private int order;
        private int visible;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", link='" + link + '\'' +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    ", visible=" + visible +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HotKeyResult{" +
                "data=" + data +
                '}';
    }
}

package com.lyl.wanandroid.service.entity;


import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class BannerResult extends BaseResult implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @NotNull
    @Override
    public String toString() {
        return "BannerInfoResult{" +
                "data=" + data +
                '}';
    }

    public static class DataBean implements Serializable {
        /**
         * desc : 享学~
         * id : 29
         * imagePath : https://wanandroid.com/blogimgs/9b9a4c7d-00d3-4e20-8f81-685467336de1.png
         * isVisible : 1
         * order : 0
         * title : 别跟我说你对这个技术还不熟！
         * type : 0
         * url : https://mp.weixin.qq.com/s/YzhsZGaz1U1FNTFhKaUVDg
         */

        private String desc;
        private int id;
        private String imagePath;
        private int isVisible;
        private int order;
        private String title;
        private int type;
        private String url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public int getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(int isVisible) {
            this.isVisible = isVisible;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    "desc='" + desc + '\'' +
                    ", id=" + id +
                    ", imagePath='" + imagePath + '\'' +
                    ", isVisible=" + isVisible +
                    ", order=" + order +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}

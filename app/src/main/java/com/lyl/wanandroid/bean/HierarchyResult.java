package com.lyl.wanandroid.bean;


import com.lyl.wanandroid.base.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class HierarchyResult extends BaseResult implements Serializable {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * children :
         *  [{"children":[],"courseId":13,"id":94,"name":"事件分发","order":9000,"parentChapterId":37,"userControlSetTop":false,"visible":1}]
         * courseId : 13
         * id : 37
         * name : 自定义控件
         * order : 45
         * parentChapterId : 0
         * userControlSetTop : false
         * visible : 1
         */

        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private boolean userControlSetTop;
        private int visible;
        private List<ChildrenBean> children;

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getParentChapterId() {
            return parentChapterId;
        }

        public void setParentChapterId(int parentChapterId) {
            this.parentChapterId = parentChapterId;
        }

        public boolean isUserControlSetTop() {
            return userControlSetTop;
        }

        public void setUserControlSetTop(boolean userControlSetTop) {
            this.userControlSetTop = userControlSetTop;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean {
            /**
             * children : []
             * courseId : 13
             * id : 94
             * name : 事件分发
             * order : 9000
             * parentChapterId : 37
             * userControlSetTop : false
             * visible : 1
             */

            private int courseId;
            private int id;
            private String name;
            private int order;
            private int parentChapterId;
            private boolean userControlSetTop;
            private int visible;
            private List<?> children;

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public int getParentChapterId() {
                return parentChapterId;
            }

            public void setParentChapterId(int parentChapterId) {
                this.parentChapterId = parentChapterId;
            }

            public boolean isUserControlSetTop() {
                return userControlSetTop;
            }

            public void setUserControlSetTop(boolean userControlSetTop) {
                this.userControlSetTop = userControlSetTop;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public List<?> getChildren() {
                return children;
            }

            public void setChildren(List<?> children) {
                this.children = children;
            }

            @Override
            public String toString() {
                return "ChildrenBean{" +
                        "courseId=" + courseId +
                        ", id=" + id +
                        ", name='" + name + '\'' +
                        ", order=" + order +
                        ", parentChapterId=" + parentChapterId +
                        ", userControlSetTop=" + userControlSetTop +
                        ", visible=" + visible +
                        ", children=" + children +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "courseId=" + courseId +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    ", parentChapterId=" + parentChapterId +
                    ", userControlSetTop=" + userControlSetTop +
                    ", visible=" + visible +
                    ", children=" + children +
                    '}';
        }
    }
}

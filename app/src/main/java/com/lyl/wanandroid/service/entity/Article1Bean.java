package com.lyl.wanandroid.service.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/7/14
 * Describe :
 */
public class Article1Bean {
    /**
     * author : code小生
     * chapterId : 414
     * chapterName : code小生
     * courseId : 13
     * desc :
     * envelopePic :
     * id : 145641
     * link : https://mp.weixin.qq.com/s/esqAFOpFstym2pnhLXqNXA
     * niceDate : 2020-07-23 22:02
     * origin :
     * originId : 14453
     * publishTime : 1595512962000
     * title : RecyclerView 的曝光统计
     * userId : 70285
     * visible : 0
     * zan : 0
     */

    private String author;
    private int chapterId;
    private String chapterName;
    private int courseId;
    private String desc;
    private String envelopePic;
    private int id;
    private String link;
    private String niceDate;
    private String origin;
    private int originId;
    private long publishTime;
    private String title;
    private int userId;
    private int visible;
    private int zan;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

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

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    @NotNull
    @Override
    public String toString() {
        return "DatasBean{" +
//                "author='" + author + '\'' +
//                ", chapterId=" + chapterId +
//                ", chapterName='" + chapterName + '\'' +
//                ", courseId=" + courseId +
//                ", desc='" + desc + '\'' +
//                ", envelopePic='" + envelopePic + '\'' +
                ", id=" + id +
//                ", link='" + link + '\'' +
//                ", niceDate='" + niceDate + '\'' +
//                ", origin='" + origin + '\'' +
//                ", originId=" + originId +
//                ", publishTime=" + publishTime +
//                ", title='" + title + '\'' +
//                ", userId=" + userId +
//                ", visible=" + visible +
//                ", zan=" + zan +
                '}';
    }
}

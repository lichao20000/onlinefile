package cn.flying.rest.onlinefile.entity;

import java.util.List;

/**
 * 文件的详细信息
 * 
 * @author longjunhao
 * 
 */
public class FileInfo {

    private String fileId;

    private String fileName;

    // 文件类型
    private String fileType;

    // 分类名
    private String className;

    private String creatorName;

    private String createTime;

    private String size;

    private String md5;

    // 点赞数
    private String praiseCount;

    // 收藏数
    private String collectCount;

    // 是否删除
    private String isDelete;

    // 文件评论列表
    private List<FileComment> fileComments;

    // ******** set or get方法

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public List<FileComment> getFileComments() {
        return fileComments;
    }

    public void setFileComments(List<FileComment> fileComments) {
        this.fileComments = fileComments;
    }

}

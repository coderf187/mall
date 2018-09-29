package com.jianf.commons.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息表
 */
public class LoanCustomer implements Serializable {

    private static final long serialVersionUID = -4482028704499931548L;

    private Long id;

    private String mobile;

    //一账通用户Id
    private Long fid;

    private String type;

    private String version;

    private String pid;

    private String name;

    private String idNo;

    private String email;// 邮箱

    private Date createTime;

    private Date updateTime;

    private Date abandonCreditReportTime; // 放弃征信报告时间

    private String mxId;// 魔蝎ID

    private Long dataCenterId;//大数据编号

    private Long coreCustomerId;//核心账户ID

    private Date abandonCreditReportTimeNew; // 放弃征信报告时间（新）

    public Date getAbandonCreditReportTime() {
        return abandonCreditReportTime;
    }

    public void setAbandonCreditReportTime(Date abandonCreditReportTime) {
        this.abandonCreditReportTime = abandonCreditReportTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMxId() {
        return mxId;
    }

    public void setMxId(String mxId) {
        this.mxId = mxId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Long getCoreCustomerId() {
        return coreCustomerId;
    }

    public void setCoreCustomerId(Long coreCustomerId) {
        this.coreCustomerId = coreCustomerId;
    }

    public Date getAbandonCreditReportTimeNew() {
        return abandonCreditReportTimeNew;
    }

    public void setAbandonCreditReportTimeNew(Date abandonCreditReportTimeNew) {
        this.abandonCreditReportTimeNew = abandonCreditReportTimeNew;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

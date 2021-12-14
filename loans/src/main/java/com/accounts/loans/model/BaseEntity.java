package com.accounts.loans.model;


import com.accounts.loans.model.listener.BaseEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity {

    private long version;
    private LocalDateTime createDatetime;
    private LocalDateTime modifiedDatetime;

    @Column(name = "LOCK_VERSION")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    @Column(name = "CREATED_DATE")
    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(LocalDateTime createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Column(name = "MODIFIED_DATE")
    public LocalDateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(LocalDateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }







}

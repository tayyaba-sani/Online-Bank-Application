package com.accounts.accounts.model.listener;

import com.accounts.accounts.model.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.Objects;

public class BaseEntityListener {
    @PrePersist
    public void prePersist(Object object) {


        if (object instanceof BaseEntity) {

            BaseEntity entity = (BaseEntity) object;
            if (Objects.isNull(entity.getCreateDatetime()))
                entity.setCreateDatetime(LocalDateTime.now());
            entity.setModifiedDatetime(LocalDateTime.now());

            entity.setVersion(1L);

        }
    }
    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof BaseEntity) {

            BaseEntity entity = (BaseEntity) object;
            entity.setModifiedDatetime(LocalDateTime.now());
            entity.setVersion(entity.getVersion() + 1L);

        }
    }
}

package com.fastcamplus.pass.repository.user;

import com.fastcamplus.pass.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_group_mapping")

public class UserGroupMappingEntity extends BaseEntity {
    // pk를 userGroupId + userId로 묶은 복합키로 둠 -> UserGroupMappingId 클래스 선언함
    @Id
    private String userGroupId;
    @Id
    private String userId;

    private String userGroupName;
    private String description;
}

package kr.composite.api.user.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User extends BaseEntity {

    @Embedded
    private UserName name;
}

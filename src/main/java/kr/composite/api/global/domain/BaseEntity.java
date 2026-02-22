package kr.composite.api.global.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@SoftDelete
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}

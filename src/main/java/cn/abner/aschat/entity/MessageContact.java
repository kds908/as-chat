package cn.abner.aschat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Message Contact 消息联系人
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "im_msg_contact")
@IdClass(ContactMultiKey.class)
public class MessageContact {
    @Id
    private Long ownerUid;
    @Id
    private Long otherUid;
    private Long mid;
    private Integer type;
    private LocalDateTime createTime;
}

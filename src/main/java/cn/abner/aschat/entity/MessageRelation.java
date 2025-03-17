package cn.abner.aschat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "im_msg_relation")
@IdClass(RelationMultiKey.class)
@Builder
public class MessageRelation {
    @Id
    private Long mid;
    @Id
    private Long ownerUid;
    private Long otherUid;
    private Integer type;
    private LocalDateTime createTime;
}

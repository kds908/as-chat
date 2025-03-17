package cn.abner.aschat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Message Content 消息内容
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
@Table(name = "im_msg_content")
@Builder
public class MessageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;
    private Long senderId;
    private Long recipientId;
    private String content;
    private Integer msgType;
    private LocalDateTime createTime;
}

package cn.abner.aschat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactInfo {
    private Long otherUid;
    private String otherName;
    private String otherAvatar;
    private Long mid;
    private Integer type;
    private String content;
    private Long convUnread;
    private LocalDateTime createTime;
}

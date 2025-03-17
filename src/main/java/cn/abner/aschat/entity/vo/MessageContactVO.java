package cn.abner.aschat.entity.vo;

import cn.abner.aschat.entity.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageContactVO {
    private Long ownerUid;
    private String ownerAvatar;
    private String ownerName;
    private Long totalUnread;
    private List<ContactInfo> contactInfoList;
}

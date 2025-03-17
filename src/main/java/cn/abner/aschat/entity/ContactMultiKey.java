package cn.abner.aschat.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 联合主键
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
public class ContactMultiKey implements Serializable {
    protected Long ownerUid;
    protected Long otherUid;
}

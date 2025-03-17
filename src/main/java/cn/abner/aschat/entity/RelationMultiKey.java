package cn.abner.aschat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class RelationMultiKey implements Serializable {
    protected Long mid;
    protected Long ownerUid;
}

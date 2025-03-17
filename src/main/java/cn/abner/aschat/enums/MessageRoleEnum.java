package cn.abner.aschat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@AllArgsConstructor
@Getter
public enum MessageRoleEnum {
    SEND(0, "发件"),
    RECEIVE(1, "收件");

    private final Integer type;
    private final String description;
}

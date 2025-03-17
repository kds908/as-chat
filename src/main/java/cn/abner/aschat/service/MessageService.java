package cn.abner.aschat.service;

import cn.abner.aschat.entity.vo.MessageContactVO;
import cn.abner.aschat.entity.vo.MessageVO;

import java.util.List;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
public interface MessageService {
    MessageVO sendNewMsg(Long senderUid, Long recipientUid, String content, Integer msgType);

    List<MessageVO> queryConversationMsg(Long ownerUid, Long otherUid);

    List<MessageVO> queryNewerMsgFrom(Long ownerUid, Long otherUid, Long lastMid);

    MessageContactVO queryContacts(Long ownerUid);
}

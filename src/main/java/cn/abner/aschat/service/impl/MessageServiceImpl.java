package cn.abner.aschat.service.impl;

import cn.abner.aschat.common.constant.Constants;
import cn.abner.aschat.entity.*;
import cn.abner.aschat.entity.vo.MessageContactVO;
import cn.abner.aschat.entity.vo.MessageVO;
import cn.abner.aschat.enums.MessageRoleEnum;
import cn.abner.aschat.repository.ContentRepository;
import cn.abner.aschat.repository.MessageContactRepository;
import cn.abner.aschat.repository.MessageRelationRepository;
import cn.abner.aschat.repository.UserRepository;
import cn.abner.aschat.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private MessageRelationRepository relationRepository;
    @Autowired
    private MessageContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public MessageVO sendNewMsg(Long senderUid, Long recipientUid, String content, Integer msgType) {
        LocalDateTime now = LocalDateTime.now();
        // save message content
        MessageContent messageContent = MessageContent.builder()
                .senderId(senderUid)
                .recipientId(recipientUid)
                .content(content)
                .msgType(msgType)
                .createTime(now)
                .build();
        messageContent = contentRepository.saveAndFlush(messageContent);
        Long mid = messageContent.getMid();

        // save sender box
        MessageRelation senderRelation = MessageRelation.builder()
                .mid(mid)
                .ownerUid(senderUid)
                .otherUid(recipientUid)
                .type(MessageRoleEnum.SEND.getType())
                .createTime(now)
                .build();
        relationRepository.save(senderRelation);

        // save recipient box
        MessageRelation recipientRelation = MessageRelation.builder()
                .mid(mid)
                .ownerUid(recipientUid)
                .otherUid(senderUid)
                .type(MessageRoleEnum.RECEIVE.getType())
                .createTime(now)
                .build();
        relationRepository.save(recipientRelation);

        // update sender contact
        MessageContact senderContact;
        Optional<MessageContact> senderContactOptional = contactRepository.findById(new ContactMultiKey(senderUid, recipientUid));
        if (senderContactOptional.isPresent()) {
            senderContact = senderContactOptional.get();
            senderContact.setMid(mid);
        } else {
            senderContact = MessageContact.builder()
                    .mid(mid)
                    .ownerUid(senderUid)
                    .otherUid(recipientUid)
                    .type(MessageRoleEnum.SEND.getType())
                    .createTime(now)
                    .build();
        }
        contactRepository.save(senderContact);

        // update recipient contact
        MessageContact recipientContact;
        Optional<MessageContact> recipientContactOptional = contactRepository.findById(new ContactMultiKey(recipientUid, senderUid));
        if (recipientContactOptional.isPresent()) {
            recipientContact = recipientContactOptional.get();
            recipientContact.setMid(mid);
        } else {
            recipientContact = MessageContact.builder()
                    .mid(mid)
                    .ownerUid(recipientUid)
                    .otherUid(senderUid)
                    .type(MessageRoleEnum.RECEIVE.getType())
                    .createTime(now)
                    .build();
        }
        contactRepository.save(recipientContact);

        // update unread count
        redisTemplate.opsForValue().increment(recipientUid + Constants.TOTAL_UNREAD_SUFFIX, 1);
        redisTemplate.opsForHash().increment(recipientUid + Constants.CONVERSION_UNREAD_SUFFIX, String.valueOf(senderUid), 1);

        // build message vo
        Optional<User> selfOptional = userRepository.findById(senderUid);
        Optional<User> otherOptional = userRepository.findById(recipientUid);
        if (selfOptional.isPresent() && otherOptional.isPresent()) {
            User self = selfOptional.get();
            User other = otherOptional.get();
            return MessageVO.builder()
                    .mid(mid)
                    .content(content)
                    .ownerUid(self.getUid())
                    .type(senderContact.getType())
                    .otherUid(self.getUid())
                    .createTime(now)
                    .ownerUidAvatar(self.getAvatar())
                    .otherUidAvatar(other.getAvatar())
                    .ownerName(self.getUsername())
                    .otherName(other.getUsername())
                    .build();
        }
        return null;
    }

    @Override
    public List<MessageVO> queryConversationMsg(Long ownerUid, Long otherUid) {
        List<MessageRelation> relationList = relationRepository.findAllByOwnerUidAndOtherUidOrderByMidAsc(ownerUid, otherUid);
        return composeMessageVO(ownerUid, otherUid, relationList);
    }

    @Override
    public List<MessageVO> queryNewerMsgFrom(Long ownerUid, Long otherUid, Long lastMid) {
        List<MessageRelation> relationList = relationRepository.findAllByOwnerUidAndOtherUidAndMidIsGreaterThanOrderByMidAsc(ownerUid, otherUid, lastMid);
        return composeMessageVO(ownerUid, otherUid, relationList);
    }

    private List<MessageVO> composeMessageVO(Long ownerUid, Long otherUid, List<MessageRelation> relationList) {
        if (!relationList.isEmpty()) {
            // build message vo list
            List<MessageVO> messageVOList = new ArrayList<>();
            Optional<User> selfOptional = userRepository.findById(ownerUid);
            Optional<User> otherOptional = userRepository.findById(otherUid);
            if (selfOptional.isPresent() && otherOptional.isPresent()) {
                User self = selfOptional.get();
                User other = otherOptional.get();
                relationList.forEach(relation -> {
                    Long mid = relation.getMid();
                    Optional<MessageContent> contentOptional = contentRepository.findById(mid);
                    contentOptional.ifPresent(o -> {
                        String content = o.getContent();
                        MessageVO messageVO = MessageVO.builder()
                                .mid(mid)
                                .content(content)
                                .ownerUid(self.getUid())
                                .type(relation.getType())
                                .otherUid(other.getUid())
                                .createTime(relation.getCreateTime())
                                .ownerUidAvatar(self.getAvatar())
                                .otherUidAvatar(other.getAvatar())
                                .ownerName(self.getUsername())
                                .otherName(other.getUsername())
                                .build();
                        messageVOList.add(messageVO);
                    });
                });
            }

            // update unread count
            Object convUnreadObj = redisTemplate.opsForHash().get(ownerUid + Constants.CONVERSION_UNREAD_SUFFIX, String.valueOf(otherUid));
            if (convUnreadObj != null) {
                long convUnread = Long.parseLong(String.valueOf(convUnreadObj));
                // clear unread count
                redisTemplate.opsForHash().delete(ownerUid + Constants.CONVERSION_UNREAD_SUFFIX, String.valueOf(otherUid));
                // update total unread count
                Long totalUnreadAfterClear = redisTemplate.opsForValue().decrement(ownerUid + Constants.TOTAL_UNREAD_SUFFIX, convUnread);
                // while the total unread count <= 0, we should remove it
                if (totalUnreadAfterClear != null && totalUnreadAfterClear <= 0) {
                    redisTemplate.delete(ownerUid + Constants.TOTAL_UNREAD_SUFFIX);
                }
            }
            return messageVOList;
        }
        return null;
    }

    @Override
    public MessageContactVO queryContacts(Long ownerUid) {
        List<MessageContact> contactList = contactRepository.findByOwnerUidOrderByMidDesc(ownerUid);
        if (!contactList.isEmpty()) {
            Optional<User> userOptional = userRepository.findById(ownerUid);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Object totalUnreadObj = redisTemplate.opsForValue().get(ownerUid + Constants.TOTAL_UNREAD_SUFFIX);
                MessageContactVO contactVO = MessageContactVO.builder()
                        .ownerUid(user.getUid())
                        .ownerName(user.getUsername())
                        .ownerAvatar(user.getAvatar())
                        .totalUnread(totalUnreadObj != null ? Long.parseLong(String.valueOf(totalUnreadObj)) : 0)
                        .build();

                contactList.forEach(contact -> {
                    Long mid = contact.getMid();
                    Optional<MessageContent> contentOptional = contentRepository.findById(mid);
                    Optional<User> otherUserOptional = userRepository.findById(contact.getOtherUid());
                    if (otherUserOptional.isPresent() && contentOptional.isPresent()) {
                        User otherUser = otherUserOptional.get();
                        MessageContent content = contentOptional.get();
                        Object convUnreadObj = redisTemplate.opsForHash().get(user.getUid() + Constants.CONVERSION_UNREAD_SUFFIX, String.valueOf(otherUser.getUid()));
                        ContactInfo contactInfo = ContactInfo.builder()
                                .otherUid(otherUser.getUid())
                                .otherName(otherUser.getUsername())
                                .otherAvatar(otherUser.getAvatar())
                                .mid(mid)
                                .type(contact.getType())
                                .content(content.getContent())
                                .convUnread(convUnreadObj != null ? Long.parseLong(String.valueOf(convUnreadObj)) : 0)
                                .createTime(contact.getCreateTime())
                                .build();
                        if (contactVO.getContactInfoList() == null) {
                            List<ContactInfo> contactInfoList = new ArrayList<>();
                            contactInfoList.add(contactInfo);
                            contactVO.setContactInfoList(contactInfoList);
                        } else {
                            contactVO.getContactInfoList().add(contactInfo);
                        }
                    }
                });
                return contactVO;
            }
        }
        return null;
    }
}

package cn.abner.aschat.service.impl;

import cn.abner.aschat.common.constant.Constants;
import cn.abner.aschat.common.exception.InvalidUserInfoException;
import cn.abner.aschat.common.exception.UserNotExistException;
import cn.abner.aschat.entity.ContactInfo;
import cn.abner.aschat.entity.MessageContact;
import cn.abner.aschat.entity.MessageContent;
import cn.abner.aschat.entity.User;
import cn.abner.aschat.entity.vo.MessageContactVO;
import cn.abner.aschat.repository.ContentRepository;
import cn.abner.aschat.repository.MessageContactRepository;
import cn.abner.aschat.repository.MessageRelationRepository;
import cn.abner.aschat.repository.UserRepository;
import cn.abner.aschat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private MessageRelationRepository relationRepository;
    @Autowired
    private MessageContactRepository contactRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User login(String email, String password) {
        Integer count = userRepository.countByEmail(email);
        if (count < 1) {
            log.warn("用户不存在！");
            throw new UserNotExistException("该用户不存在：" + email);
        } else {
            Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
            if (optionalUser.isPresent()) {
                log.info("登录成功！");
                return optionalUser.get();
            } else {
                log.warn("用户或密码错误");
                throw new InvalidUserInfoException("用户或密码错误");
            }
        }
    }

    @Override
    public MessageContactVO getContacts(User loginUser) {
        List<MessageContact> contactList = contactRepository.findByOwnerUidOrderByMidDesc(loginUser.getUid());
        if (!contactList.isEmpty()) {
            Object totalUnreadObj = redisTemplate.opsForValue().get(loginUser.getUid() + Constants.TOTAL_UNREAD_SUFFIX);
            MessageContactVO contactVO = MessageContactVO.builder()
                    .ownerUid(loginUser.getUid())
                    .ownerName(loginUser.getUsername())
                    .ownerAvatar(loginUser.getAvatar())
                    .totalUnread(totalUnreadObj != null ? Long.parseLong(String.valueOf(totalUnreadObj)) : 0)
                    .build();

            contactList.forEach(contact -> {
                Long mid = contact.getMid();
                Optional<MessageContent> contentOptional = contentRepository.findById(mid);
                Optional<User> otherUserOptional = userRepository.findById(contact.getOtherUid());
                if (otherUserOptional.isPresent() && contentOptional.isPresent()) {
                    User otherUser = otherUserOptional.get();
                    MessageContent content = contentOptional.get();
                    Object convUnreadObj = redisTemplate.opsForHash().get(loginUser.getUid() + Constants.CONVERSION_UNREAD_SUFFIX, String.valueOf(otherUser.getUid()));
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
        return null;
    }

    @Override
    public List<User> getAllUsersExcept(User exceptUser) {
        return userRepository.findUsersByUidIsNot(exceptUser.getUid());
    }
}

package cn.abner.aschat.controller;

import cn.abner.aschat.entity.vo.MessageContactVO;
import cn.abner.aschat.entity.vo.MessageVO;
import cn.abner.aschat.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Message Controller
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息
     *
     * @param senderUid    发送人 ID
     * @param recipientUid 接收人 ID
     * @param content      消息内容
     * @param msgType      消息类型
     * @param model        Model
     * @param session      HttpSession
     * @return message
     */
    @PostMapping("/sendMsg")
    public String sendMsg(@RequestParam Long senderUid, @RequestParam Long recipientUid, @RequestParam String content,
                          @RequestParam Integer msgType, Model model, HttpSession session) {
        MessageVO messageVO = messageService.sendNewMsg(senderUid, recipientUid, content, msgType);
        if (messageVO != null) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());
            try {
                return jsonMapper.writeValueAsString(messageVO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 查询消息
     *
     * @param ownerUid 当前用户
     * @param otherUid 联系人
     * @param model    Model
     * @param session  HttpSession
     * @return message list
     */
    @GetMapping("/queryMsg")
    public String queryMsg(@RequestParam Long ownerUid, @RequestParam Long otherUid, Model model, HttpSession session) {
        List<MessageVO> messageVOList = messageService.queryConversationMsg(ownerUid, otherUid);
        if (messageVOList != null) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());
            try {
                return jsonMapper.writeValueAsString(messageVOList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * 从最新 mid 起获取新消息
     *
     * @param ownerUid 当前用户 ID
     * @param otherUid 联系人 ID
     * @param lastMid  最新消息 ID
     * @param model    Model
     * @param session  HttpSession
     * @return message list
     */
    @GetMapping("queryMsgSinceMid")
    public String queryMsgSinceId(@RequestParam Long ownerUid, @RequestParam Long otherUid, @RequestParam Long lastMid,
                                  Model model, HttpSession session) {
        List<MessageVO> messageVOList = messageService.queryNewerMsgFrom(ownerUid, otherUid, lastMid);
        if (messageVOList != null) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());
            try {
                return jsonMapper.writeValueAsString(messageVOList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * 查询联系人
     * @param ownerUid 当前用户 ID
     * @param model Model
     * @param session HttpSession
     * @return contact list
     */
    @GetMapping("/queryContacts")
    public String queryContacts(@RequestParam Long ownerUid, Model model, HttpSession session) {
        MessageContactVO contactVO = messageService.queryContacts(ownerUid);
        if (contactVO != null) {
            JsonMapper jsonMapper = new JsonMapper();
            jsonMapper.registerModule(new JavaTimeModule());
            try {
                return jsonMapper.writeValueAsString(contactVO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}

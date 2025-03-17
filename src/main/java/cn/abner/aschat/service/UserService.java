package cn.abner.aschat.service;

import cn.abner.aschat.entity.User;
import cn.abner.aschat.entity.vo.MessageContactVO;

import java.util.List;

public interface UserService {
    User login(String email, String password);

    MessageContactVO getContacts(User loginUser);

    List<User> getAllUsersExcept(User loginUser);
}

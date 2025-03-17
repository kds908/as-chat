package cn.abner.aschat.controller;

import cn.abner.aschat.common.constant.Constants;
import cn.abner.aschat.common.exception.InvalidUserInfoException;
import cn.abner.aschat.common.exception.UserNotExistException;
import cn.abner.aschat.entity.User;
import cn.abner.aschat.entity.vo.MessageContactVO;
import cn.abner.aschat.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String welcomePage(@RequestParam(name = "username", required = false) String username, Model model, HttpSession session) {
        if (session.getAttribute(Constants.SESSION_KEY) != null) {
            return "index";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            User loginUser = userService.login(email, password);
            model.addAttribute("loginUser", loginUser);
            session.setAttribute(Constants.SESSION_KEY, loginUser);

            List<User> otherUsers = userService.getAllUsersExcept(loginUser);
            model.addAttribute("otherUsers", otherUsers);

            MessageContactVO contactVO = userService.getContacts(loginUser);
            model.addAttribute("contactVO", contactVO);
            return "index";
        } catch (UserNotExistException e1) {
            model.addAttribute("errorMsg", "用户不存在");
            return "login";
        } catch (InvalidUserInfoException e2) {
            model.addAttribute("errorMsg", "错误的用户名或密码");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(Constants.SESSION_KEY);
        return "redirect:/";
    }
}

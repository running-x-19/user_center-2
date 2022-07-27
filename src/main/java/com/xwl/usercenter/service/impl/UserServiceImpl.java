package com.xwl.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.usercenter.constant.UserConstant;
import com.xwl.usercenter.entities.User;
import com.xwl.usercenter.mapper.UserMapper;
import com.xwl.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author xwl
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-07-12 18:50:18
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper originUser;

    private static final String SALT = "xwl";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        //校验
        if (userAccount == null || userPassword == null || checkPassword == null) {
            return -1;
        }

        if (userAccount.length() < 4) return -1;

        if (userPassword.length() < 8 || checkPassword.length() < 8) return -1;

        if (!userPassword.equals(checkPassword)) return -1;

        //不能包含特殊字符
        //String validPattern = "//pP|//pS|//s+";
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        //账号不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(User::getUser_password, userPassword);
        queryWrapper.eq(User::getUser_account, userAccount);
        int count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //密码不能使用铭文
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //插入数据
        User user = new User();
        user.setUser_account(userAccount);
        user.setUser_password(encryptPassword);
        boolean save = this.save(user);
        if (save != true) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest servletRequest) {
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        //长度限制
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            return null;
        }

        //不能包含特殊字符
        //String validPattern = "//pP|//pS|//s+"
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        //账号是否存在, 不用插入数据
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = originUser.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login fail, userAccount not match userPassword");
            return null ;
        }

        //用户脱敏
        //不返回用户密码, 更新时间, 死否删除
        User safeUser = getSafeUser(user);

        //记录用户的登录状态
        servletRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safeUser);
        return safeUser;
    }


    @Override
    public User getSafeUser(User originUser) {
        if (originUser == null) {
            return null;
        }

        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUser_name(originUser.getUser_name());
        safeUser.setUser_account(originUser.getUser_account());
        safeUser.setAvatar_url(originUser.getAvatar_url());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUser_role(originUser.getUser_role());
        safeUser.setUser_status(originUser.getUser_status());
        safeUser.setCreate_time(originUser.getCreate_time());
        return safeUser;
    }
}
package com.markerhub.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.entity.User;
import com.markerhub.service.UserService;
import com.markerhub.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/*
supports：为了让realm支持jwt的凭证校验
        doGetAuthorizationInfo：权限校验
        doGetAuthenticationInfo：登录认证校验
 */

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;


    /**
     * 判断是否为jwtToken，如果是就强转
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

//        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
//        info.addStringPermission("user:add");
        //拿到当前登录的这个对象
//        Subject subject=SecurityUtils.getSubject();
//        User currentUser = (User) subject.getPrincipal();//拿到User对象
        //设置当前用户的权限
//        info.setStringPermission(currentUser.getPerms());
        return null;
    }


    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
       JwtToken jwtToken=(JwtToken) authenticationToken;


////        //获取当前的的用户
//        Subject subject= SecurityUtils.getSubject();
//        //封装用户的登录数据
//        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //执行登录的方法，如果没有异常就说明ok了
//        subject.login(token);

        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(Long.valueOf(userId));
        if (user==null){
            throw new UnknownAccountException("账号不存在");
        }
        if(user.getStatus()==-1){
            throw new LockedAccountException("账号已被锁定");
        }
        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        //密码认证，shiro做~
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());

    }
}

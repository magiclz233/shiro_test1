package com.cnpc.test;

import com.cnpc.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class CustomRealmTest {

    CustomRealm customRealm = new CustomRealm();



    @Test
    public void testAuthentication(){

        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
       defaultSecurityManager.setRealm( customRealm );

       //加密认证
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(  );
        matcher.setHashAlgorithmName( "md5" );
        matcher.setHashIterations( 1 );

        customRealm.setCredentialsMatcher( matcher );

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //用户自己输入的用户及密码
        UsernamePasswordToken token = new UsernamePasswordToken("magic","123456");

        //将用户输入的与realm取得的进行比较 正确登入
        subject.login(token);

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        //用户登出
//        subject.logout();
//        System.out.println("subject.isAuthenticated() = " + subject.isAuthenticated());

        //赋予用户角色权限（可以多个角色权限）
        subject.checkRoles("admin","user");
        subject.checkPermissions( "user:add","user:delete" );
    }
}

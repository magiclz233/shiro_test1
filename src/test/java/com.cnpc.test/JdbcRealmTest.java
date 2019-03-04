package com.cnpc.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro_test");
        dataSource.setUsername("magic");
        dataSource.setPassword("123456");
    }





    @Test
    public void testAuthentication(){

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        //自定义sql 查询test_user表
        String sql = "select password from test_user where name = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSQL = "select role_name from test_user_role where name = ?";
        jdbcRealm.setUserRolesQuery(roleSQL);

        String permissionSQL = "select permission from roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionSQL);

        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //用户自己输入的用户及密码
        UsernamePasswordToken token = new UsernamePasswordToken("root","123456");

        //将用户输入的与realm取得的进行比较 正确登入
        subject.login(token);

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        //用户登出
//        subject.logout();
//        System.out.println("subject.isAuthenticated() = " + subject.isAuthenticated());

        //赋予用户角色权限（可以多个角色权限）
        subject.checkRole("admin");
        subject.checkRoles("admin","user");
        subject.checkPermission("user:select");
        subject.checkPermissions("user:select","user:delete");
    }
}

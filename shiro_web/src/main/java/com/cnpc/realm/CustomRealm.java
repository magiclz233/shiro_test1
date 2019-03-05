package com.cnpc.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    Map<String,String> userMap  = new HashMap<>( 16 );
    {
        userMap.put( "magic","e10adc3949ba59abbe56e057f20f883e" );
        super.setName( "customRealm" );
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();

        //从数据库或者缓存当中获取角色数据
        Set<String> roles = getRolesByUserName(userName);

        Set<String> permissions = getPermissionByUserName(userName);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(  );
        authorizationInfo.setRoles( roles );
        authorizationInfo.setStringPermissions( permissions );
        return authorizationInfo;
    }

    private Set<String> getPermissionByUserName(String userName) {
        Set<String> permission = new HashSet<>(  );
        permission.add( "user:add" );
        permission.add( "user:delete" );
        return permission;
    }

    /**
     *
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        Set<String> userRoles = new HashSet<>(  );
        userRoles.add( "admin" );
        userRoles.add( "user" );
        return userRoles;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     * 主体认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();

        String password = getPasswordByUserName(userName);
        if (password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new
                SimpleAuthenticationInfo( "magic",password,"customRealm" );
        authenticationInfo.setCredentialsSalt( ByteSource.Util.bytes( "magic" ) );
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }

    public static void main(String[] args){
        Md5Hash md5Hash = new Md5Hash( "123456" );
        System.out.println(md5Hash.toString());
    }
}

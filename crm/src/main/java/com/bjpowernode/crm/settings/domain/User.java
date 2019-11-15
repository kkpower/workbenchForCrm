package com.bjpowernode.crm.settings.domain;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class User {

    /*

        关于日期.时间

            对于字符串所表现的日期和时间有其固定的格式：

            最常用的固定的格式：

            日期：yyyy-MM-dd 10位
            日期+时间：yyyy-MM-dd HH:mm:ss 19位

        关于登录

            1.验证账号和密码

            该形式只能取得返回的条数，其他信息取不到，所以不推荐
            int count = 执行sql语句 select count(*) from tbl_user where loginAct=? and loginPwd=?
            count:0 没有查到，账号密码错误
            count:1 查到了一条，账号密码正确
            count:>1 开发时有可能表中有垃圾数据 虽然账号密码正确，但是仍然应该是登录失败的
            如果账号密码正确，还需要验证失效时间，此时就没有办法继续验证了
            因为我们还得发出sql语句取得该条记录对应的失效时间...

            该形式能够取得该条记录的详细信息，所有登录需要验证的信息全能够取得，所以推荐使用该形式
            User user = 执行sql语句 select * from tbl_user where loginAct=? and loginPwd=?
            user:null 没有查到，账号密码错误
            user:不是null 查到了，账号密码正确
            如果账号密码正确，还需要验证失效时间，我们从user对象中取出失效时间继续验证就可以了

            2.从user对象中取出失效时间，验证失效时间 expireTime

            3.从user对象中取出锁定状态，验证锁定状态 lockState

            4.从user对象中取出允许访问的ip地址群，验证当前浏览器端的ip地址是否包含在ip地址群中

     */

    private String id;  //主键
    private String loginAct;    //登录账号
    private String name;    //用户真实姓名
    private String loginPwd;    //登录密码
    private String email;   //邮箱
    private String expireTime;  //失效时间 yyyy-MM-dd HH:mm:ss 19位
    private String lockState;   //锁定状态
    private String deptno;  //部门编号
    private String allowIps;    //允许访问的ip地址群
    private String createTime;  //创建时间 yyyy-MM-dd HH:mm:ss 19位
    private String createBy;    //创建人
    private String editTime;    //修改时间 yyyy-MM-dd HH:mm:ss 19位
    private String editBy;  //修改人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}

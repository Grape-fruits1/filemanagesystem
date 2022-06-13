/**
 * @author 17291
 * @version 1.0
 * @className User
 * @description TODO 用户
 * @date 2022/6/5 2:41
 */
public class User {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String userName;
    String password;

    byte uid;
    User() {

    }

    User(String name, String pass,byte id) {
        this.userName = name;
        this.password = pass;
        this.uid = id;
    }

    public void show(){
        System.out.println("用户名:"+userName);
        System.out.println("密码:"+password);
    }
}
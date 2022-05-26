import java.util.Scanner;

/**
 * @author 17291
 * @version 1.0
 * @className VI
 * @description TODO 文本编辑器
 * @date 2022/5/26 14:07
 */
public class VI {
    /**
     *文件名
     */
    private String fileName = "a.txt";
    /**
     * VI修改模式
     */
    private final String MODIFICATION_MODE = "i";
    /**
     * VI命令模式
     */
    private final String COMMAND_MODE = ":";
    public VI() {

    }
    public VI(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 初始化VI方法
     */
    public void start() {
        loadBuffer();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String str = scan.next();
            //进入编辑模式
            if (MODIFICATION_MODE.equals(str)) {

            }
            //进入命令模式
            else if (COMMAND_MODE.equals(str)) {

            }
        }

    }
    /**
     * 加载VI文件缓冲区方法
     */
    private void loadBuffer() {
        //测试中：若文件之前已存在
        if (true) {
            //从旧文件中还原缓冲区
        }
    }
}
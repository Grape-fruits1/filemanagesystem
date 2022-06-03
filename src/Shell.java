import java.util.Scanner;

/**
 * @author 17291
 * @version 1.0
 * @className Shell
 * @description TODO 命令行解释器
 * @date 2022/5/26 14:06
 */
public class Shell {
    /**
     * 操作系统入口
     *
     * @param args
     */
    public static void main(String[] args) {
        FileAllocation file1 = new FileAllocation();
        while (true) {
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            String[] inputHandled = input.split(" ");
            String command = inputHandled[0];
            String param1 = "";
            String param2 = "";
            if (inputHandled.length == 2) {
                param1 = input.split(" ")[1];
            } else if (inputHandled.length == 3) {
                param2 = input.split(" ")[2];
            } else if (inputHandled.length > 3) {
                System.out.println("Parameter is too much!");
            }
            switch (command) {
                //无输入内容直接回车
                case "" -> {
                }
                //输入vi指令
                case "vi" -> {
                    VI vi;
                    if ("".equals(param1)) {
                        vi = new VI();
                        byte[] a = vi.start();
                        file1.realBlockAllocate(Transmit.byteToInt(a), a);
                    } else {
                    }
                }
                case "exit" -> {
                    return;
                }
                default -> {
                    System.out.println("There is not such command!");
                }
            }
        }
    }
}
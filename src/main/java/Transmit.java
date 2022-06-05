import java.io.*;

/**
 * @program: filemanagesystem
 * @description: 文件读写
 * @author: Ran
 * @create: 2022-06-03 14:48
 **/

public class Transmit {
    FileAllocation file1 = new FileAllocation();
    VI vi = new VI();
    byte[] a = vi.start();
    int size = Transmit.byteToInt(a);

    /**
     * byte转int,输出所占块数
     */
    public static int byteToInt(byte[] sizeByte1) {
        int size = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            size += (sizeByte1[i] & 0xFF) << shift;
        }
        return size;
    }

//    /**
//     * 将文件输入到内存
//     */
//    public void outPut(int blockIndex) throws IOException {
//        File file = new File("res/test.txt");
//        FileInputStream fo = new FileInputStream(file);
//        byte[] a = new byte[blockIndex];
//        int len;
//        while ((len = fo.read(a)) != -1) {
//            // 每次读取后,把数组变成字符串打印
//            System.out.println(new String(a));
//        }
//        fo.close();
//    }

    /**
     * 将文件输入到指定内存（blockIndex所指定的物理块）
     */
    public byte[][] outPut(int blockIndex) throws IOException {
        try {
            File file = new File("res/test.txt");
            FileInputStream fi = new FileInputStream(file);
            // 根据索引定位物理块，用并写入此物理块
            byte[][] data = file1.forUse(Transmit.byteToInt(a), a);
            fi.read(data[blockIndex]);
            fi.close();
            return data;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 磁盘输出流
     */
    public void outPutDisc(byte[] b) throws IOException {
        File file = new File("res/test.txt");
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(b);
    }

//    public void byteOutPut(byte [] a) {
//        // 创建字节数组输入流
//        ByteArrayInputStream byteInPut = new ByteArrayInputStream(a);
//        int i = byteInPut.read(); // 从输入流中读取下一个字节，并转换成int型数据
//        while (i != -1) { // 如果不返回-1，则表示没有到输入流的末尾
//            System.out.println("原值=" + (byte) i + "\t\t\t转换为int类型=" + i);
//            i = byteInPut.read(); // 读取下一个
//        }
//    }


}

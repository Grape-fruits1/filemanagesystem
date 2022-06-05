import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * @author Lodisaq
 * @version 1.0
 * @className IndexBlock
 * @description 索引块
 * @date 2022/5/26 13:36
 */
public class FileControlBlock {
    //访问权限：读、写、执行
    static final byte READABLE = 4;
    static final byte WRITABLE = 2;
    static final byte EXECUTABLE = 1;
    byte readability = READABLE;
    byte writability = WRITABLE;
    byte enforceability = EXECUTABLE;
public class IndexBlock {
    //文件名
    private String fileName;
    //索引节点
    private int inode;
    //文件创建时间
    Date creationTime;
    //最后修改时间
    Date modifiedTime;
    //文件所有者
    User owner;

    /**
     * 此构造方法用于第一次创建fcb，不适用于从磁盘读取已有文件
     */
    public FileControlBlock(String fileName) {
        creationTime = new Date();
        this.fileName = fileName;
        chomd((byte) 0);
    }

    /**
     * 修改权限方法
     * @param permission 0 - 7 分别表示三种权限大小
     */
    void chomd(byte permission) {
        switch (permission) {
            case 0 -> {
                readability = 0;
                writability = 0;
                enforceability = 0;
            }
            case 1 -> {
                readability = 0;
                writability = 0;
                enforceability = EXECUTABLE;
            }
            case 2 -> {
                readability = 0;
                writability = WRITABLE;
                enforceability = 0;
            }
            case 3 -> {
                readability = 0;
                writability = WRITABLE;
                enforceability = EXECUTABLE;
            }
            case 4 -> {
                readability = READABLE;
                writability = 0;
                enforceability = 0;
            }
            case 5 -> {
                readability = READABLE;
                writability = 0;
                enforceability = EXECUTABLE;
            }
            case 6 -> {
                readability = READABLE;
                writability = WRITABLE;
                enforceability = 0;
            }
            case 7 -> {
                readability = READABLE;
                writability = WRITABLE;
                enforceability = EXECUTABLE;
            }
            case default -> {
                System.out.println("undefined permission!");
            }
        }
    }
    /**
     * 修改权限方法
     * @param parameter +：增加权限；-：关闭权限；=：修改为新的权限
     */
    public void chomd(String parameter) {
        if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(parameter).matches()) {
            chomd(Byte.parseByte(parameter));
            return;
        }
        byte newReadability = 0;
        byte newWritability = 0;
        byte newEnforceability = 0;
        if (parameter.contains("r")) {
            newReadability = READABLE;
        }
        if (parameter.contains("w")) {
            newWritability = WRITABLE;
        }
        if (parameter.contains("x")) {
            newEnforceability = EXECUTABLE;
        }
        char operate = parameter.charAt(0);
        switch (operate) {
            case '+' -> {
                readability = (byte) (newReadability | readability);
                writability = (byte) (newWritability | writability);
                enforceability = (byte) (newEnforceability | enforceability);
            }
            case '-' -> {
                if (readability <= newReadability) {
                    readability = 0;
                } else {
                    readability = READABLE;
                }
                if (writability <= newWritability) {
                    writability = 0;
                } else {
                    writability = WRITABLE;
                }
                if (enforceability <= newEnforceability) {
                    enforceability = 0;
                } else {
                    enforceability = EXECUTABLE;
                }
            }
            case '=' -> {
                chomd((byte)(newReadability + newWritability + newEnforceability));
            }
        }
    }

    /**
     * 获取权限信息
     * @return 表示权限的字符串
     */
    public String getPermission() {
        StringBuilder nowPermission = new StringBuilder("rwx");
        if (readability == 0) {
            nowPermission.setCharAt(0, '-');
        }
        if (writability == 0) {
            nowPermission.setCharAt(1, '-');
        }
        if (enforceability == 0) {
            nowPermission.setCharAt(2, '-');
        }
        return String.valueOf(nowPermission);
    }

    /**
     * 获取文件创建时间
     * @return 格式：dd-MM-yyyy HH:mm:ss
     */
    public String getCreationTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(creationTime);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getInode() {
        return inode;
    }

    public void setInode(int inode) {
        this.inode = inode;
    }

    //调试用主函数入口
    public static void main(String[] args) {
//        MainFunction mainFunctions = new MainFunction();
//        mainFunctions.mainB();
        InodeBlocks i = new InodeBlocks();
        i.test();

    }


}

/**
 * 整个混合索引所占用的磁盘 20000*192
 */

class InodeBlocks {
    //初始化磁盘
    byte[][] blocks = new byte[20000][256];

    /**
     * 初始化
     */
    public InodeBlocks() {
        initIndex();
    }

    /**
     * int到byte[] 由高位到低位
     *
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }


    /**
     * 初始化索引
     */
    public void initIndex() {
        int i = 0;
        int indexValue = 0;
        byte[] buff;
        for (; i < 10; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(indexValue);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                indexValue += 1;
            }
        }
        //至此直接索引完成

        //二级索引开始
        int secondIndex = 65;
        for (; i < 62; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(secondIndex);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                secondIndex += 1;
            }
        }

        System.out.println(secondIndex);
        // 二级索引的值从第65-3392开始分配

        secondIndex = 640;
        i = 65;
        for (; i < 3393; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(secondIndex);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                secondIndex += 1;
            }
        }
        System.out.println(secondIndex);

        //二级索引至此完成物理块分配 640-213631

        i = 62;
        int thirdIndex = 3393;
        for (; i < 65; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(thirdIndex);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                thirdIndex += 1;
            }
        }

        i = 3393;
        thirdIndex = 3585;
        for (; i < 3585; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(thirdIndex);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                thirdIndex += 1;
            }
        }
        System.out.println(thirdIndex);
        //三级索引物理快分配完成
        i = 3585;
        thirdIndex = 213632;
        for (; i < 15873; i++) {
            for (int j = 0; j < 256; j += 4) {
                buff = intToByteArray(thirdIndex);
                this.blocks[i][j] = buff[0];
                this.blocks[i][j + 1] = buff[1];
                this.blocks[i][j + 2] = buff[2];
                this.blocks[i][j + 3] = buff[3];
                thirdIndex += 1;
            }
        }
        System.out.println(thirdIndex);

    }

    /**
     * 根据偏移量获得物理地址
     *
     * @param index 偏移量
     */
    public int queryIndex(int index) {
        if (index <= 639)
            return index;
        else if (index <= 213631) {
            int i = 1;
            byte[] buff = new byte[4];
            for (; i < 53; i++) {
                if (index < 640 + 4096 * i) {
                    break;
                }
            }
            //System.out.println(i+9);

            int x = (index - 640) / 64 + 65;
            int y = (index - 640) % 64;

            y = y * 4;
            buff[0] = this.blocks[x][y];
            buff[1] = this.blocks[x][y + 1];
            buff[2] = this.blocks[x][y + 2];
            buff[3] = this.blocks[x][y + 3];

//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(byteArrayToInt(buff));
            return byteArrayToInt(buff);
        } else if (index <= 1000063) {
            int i = 1;
            byte[] buff = new byte[4];
            for (; i < 4; i++) {
                if (index < 213631 + 262144 * i) {
                    break;
                }
            }

            int x = (index - 213632) / 64 + 3585;
            int y = (index - 213632) % 64;
            int z = (x-3585) /64 +3393;

            y = y * 4;
            buff[0] = this.blocks[x][y];
            buff[1] = this.blocks[x][y + 1];
            buff[2] = this.blocks[x][y + 2];
            buff[3] = this.blocks[x][y + 3];
            System.out.println(x);
            System.out.println(y);
            System.out.println(z);
            return byteArrayToInt(buff);

        } else {
            System.out.println("索引越界");
            return 0;
        }
    }


    public void test() {
        System.out.println(queryIndex(213633));

    }
}


abstract class FileClass{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInode() {
        return inode;
    }

    public void setInode(int inode) {
        this.inode = inode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String name;
    private int inode;
    // 0表示目录文件 1表示数据文件
    private int type;
}

class FileType extends FileClass{
    FileType(String name,int inode,int type){
        setName(name);
        setInode(inode);
        setType(type);
    }
}

class DirectoryType extends FileClass{
    DirectoryType(String name,int inode,int type){
        setName(name);
        setInode(inode);
        setType(type);
    }
    private ArrayList<FileClass> subsetFile = new ArrayList<>();
}


class initDirectory{
    DirectoryType root = new DirectoryType("/",0,0);

}




















/*************************************************************************/
/**
 * 一个索引块存64个索引指针大小 1536byte
 */
class InodeBlock {
    //每个索引磁盘块: 512*3=1536 Byte
    byte[][] b = new byte[64][3];
    Integer blockId;

    @Override
    public String toString() {
        System.out.println("The " + this.blockId + " InodeBlock's message:");
        int j = 1;
        for (int i = 0; i < 64; i++) {
            String inode = new String(this.b[i]);
            System.out.println("The " + j + " index's value is :" + inode);
            j++;
        }
        return "*****************************************";
    }

    public void initBlock(int i, String j) {
        this.b[i] = j.getBytes();
    }

    public void showBlock(int i) {
        String inode = new String(this.b[i]);
        System.out.println(inode);
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}

/**
 * 主函数
 */
class MainFunction {
//    InodeBlock ib1 = new InodeBlock();

    /**
     * 初始化索引表
     */
    //存储所有的索引节点块
    ArrayList<InodeBlock> inodeBlocks = new ArrayList<>();

    public void allBlocks() {
        int number = 64;
        for (int i = 0; i < number; i++) {
            InodeBlock inodeBlock = new InodeBlock();
            inodeBlock.setBlockId(i);
            initBlocks(inodeBlock, i);
            this.inodeBlocks.add(inodeBlock);
        }
    }

    public void showAllBlocks() {
        for (InodeBlock inodeBlock : this.inodeBlocks) {
            System.out.println(inodeBlock);
        }

    }

    /**
     * 初始化第number个索引磁盘块
     *
     * @param number 磁盘块ID
     * @param ib1    需要初始化的磁盘
     */
    public void initBlocks(InodeBlock ib1, int number) {
        ib1.setBlockId(number);
        int i = ib1.getBlockId() * 64;
        int tag = number * 64;
        int j = 0;
        for (; i < (64 + tag); i++) {
            ib1.initBlock(j, Integer.toString(i));
            j++;
        }
    }

    public void mainB() {
        allBlocks();
        showAllBlocks();
    }

}
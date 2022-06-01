import java.util.*;


/**
 * @author Lodisaq
 * @version 1.0
 * @className FileControlBlock
 * @description 文件控制块
 * @date 2022/5/26 13:36
 */
public class FileControlBlock {
    //文件名
    private String fileName;
    //索引节点
    private int inode;

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
        MainFunction mainFunction = new MainFunction();
        mainFunction.mainB();
    }
}

class FileMessage {
    private Date createDate;
    private String owner;
}

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
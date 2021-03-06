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
public class IndexBlock {

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


    //调试用主函数入口
    public static void main(String[] args) {
//        MainFunction mainFunctions = new MainFunction();
//        mainFunctions.mainB();
//        InodeBlocks i = new InodeBlocks();
//        i.test();
        InitDirectory i = new InitDirectory();
        i.mainSet();
//        FileAllocation f = new FileAllocation();
//        f.testA();
//        f.fillInIndex();


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
            int z = (x - 3585) / 64 + 3393;

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

/**
 * 文件类，包含目录和文件的抽象类
 */
abstract class FileClass {
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

/**
 * 数据文件类
 */
class FileType extends FileClass {
    FileType(String name, int inode, int type) {
        setName(name);
        setInode(inode);
        setType(type);
    }

}

/**
 * 目录文件类
 */
class DirectoryType extends FileClass {
    DirectoryType(String name, int inode, int type) {
        setName(name);
        setInode(inode);
        setType(type);
    }

    public void cloneDir(DirectoryType dir) {
        dir.setName(getName());
        dir.setType(getType());
        dir.setInode(getInode());
        dir.setSubsetFile(getSubsetFile());
    }

    DirectoryType() {

    }


    public HashMap<String, FileClass> getSubsetFile() {
        return subsetFile;
    }

    public void setSubsetFile(HashMap<String, FileClass> subsetFile) {
        this.subsetFile = subsetFile;
    }

    private HashMap<String, FileClass> subsetFile = new HashMap<>();


    @Override
    public String toString() {
        System.out.println(getName() + "的子目录************");
        for (HashMap.Entry<String, FileClass> sub : subsetFile.entrySet()) {
            if (!sub.getKey().equals(".")) {
                if (!sub.getKey().equals(".."))
                    System.out.println(sub.getKey());
            }
        }
        return "**********************";
    }
}

/**
 * 目录初始化
 */
class InitDirectory {
    DirectoryType root = new DirectoryType("/", 0, 0);
    DirectoryType nowDir = new DirectoryType();

    public static void initDir(DirectoryType parent, DirectoryType self) {
        self.getSubsetFile().put(".", self);
        self.getSubsetFile().put("..", parent);
    }

    public void mainSet() {
        Scanner input = new Scanner(System.in);
        ArrayList<User> log = new ArrayList<>();
        String pass = new String();
        String nameU;
        int flag = 1;
        for (int i = 1; i < 9; i++) {
            pass = String.valueOf(i);
            nameU = "user".concat(pass);
            User u = new User(nameU, pass, (byte) i);
            log.add(u);
        }
        //登录系统
        while (flag == 1) {
            System.out.println("please input name");
            String u = input.nextLine();
            System.out.println("please input password");
            String p = input.nextLine();
            for (User u1 : log) {
                if (u1.getUserName().equals(u))
                    if (u1.getPassword().equals(p))
                        flag = 0;

            }
            if (flag == 1)
                System.out.println("match error");
        }

        System.out.println("welcome");
        FileAllocation fa = new FileAllocation();
        InodeBlocks iBlock = new InodeBlocks();
        FileControlBlock fB = new FileControlBlock();

        //给root的FCB分配地址
        root.setInode(fa.returnFreeIndex(1)[0]);
        byte[]  tag= new byte[512];
        tag = fB.cFileControlBlock();
        //向FCB的地址中写入信息
        fa.writeControlBlockMessage(root.getInode(),tag);
        fB.setInode(fa.returnFreeIndex(1)[0]);
        //给FCB中索引表分配了地址
        fa.writeControlBlockMessage(root.getInode(),tag);
        Arrays.fill(tag, (byte) 0);
        tag[0] = 1;

        fa.writeControlBlockMessage(fB.getInode(),tag);

        byte[] storage = new byte[512];
        for(int i =0;i<512;i++){
            byte[] tb = new byte[16];
            tb = writeBlock("home",fa.returnFreeIndex(1)[0]);
            Arrays.fill(tag, (byte) 0);
            tag[0] = 1;
            fa.writeControlBlockMessage(fa.returnFreeIndex(1)[0],tag);
            for(int j =0;j<16;j++){
                storage[i] = tb[j];
                i++;
            }

            tb = writeBlock("lib",fa.returnFreeIndex(1)[0]);
            Arrays.fill(tag, (byte) 0);
            tag[0] = 1;
            fa.writeControlBlockMessage(fa.returnFreeIndex(1)[0],tag);
            for(int j =0;j<16;j++){
                storage[i] = tb[j];
                i++;
            }


            tb = writeBlock("program",fa.returnFreeIndex(1)[0]);
            Arrays.fill(tag, (byte) 0);
            tag[0] = 1;
            fa.writeControlBlockMessage(fa.returnFreeIndex(1)[0],tag);
            for(int j =0;j<16;j++){
                storage[i] = tb[j];
                i++;
            }

            fa.writeControlBlockMessage(fB.getInode(),storage);
            break;
        }

        storage=fa.readControlBlockMessage(fB.getInode());

        for(int i = 0;i<512;i++){
            String s = new String(String.valueOf((storage[i])));
            System.out.println(s);
        }
//        byte[] nameByte = new byte[12];
//        int i=0;
//        int j=0;
//        int fl=0;
//        for(;i<512;i++){
//            if(storage[i]!=0 ){
//                nameByte[j] = storage[i];
//                fl++;
//                j++;
//            }else {
//                if(fl !=0){
//                    if(nameByte.length>1) {
//                        System.out.println(new String(nameByte));
//                        fl = 0;
//                        j = 0;
//                    }
//                }
//            }
////            nameByte[j] = storage[i];
////            if(storage[i]==0) {
////                if (nameByte.length > 1) {
////                    System.out.println(new String(nameByte));
////                }else {
////                    System.out.println(nameByte[0]);
////                }
////                Arrays.fill(nameByte, (byte) 0);
////                j=0;
////                continue;
////            }
////            j++;
//        }


        DirectoryType x = new DirectoryType("x", 626, 0);
        DirectoryType home = new DirectoryType("home", 65, 0);
        DirectoryType lib = new DirectoryType("lib", 6, 0);
        DirectoryType program = new DirectoryType("program", 621, 0);
        FileClass a = new FileType("a.c", 90, 1);
        FileClass b = new FileType("b.txt", 90, 1);

        nowDir = root;
        this.root.getSubsetFile().put(".", root);
        this.root.getSubsetFile().put("home", home);
        this.root.getSubsetFile().put("lib", lib);
        this.root.getSubsetFile().put("program", program);

        initDir(root, home);
        initDir(root, lib);
        initDir(root, program);

        home.getSubsetFile().put("x", x);
        initDir(home, x);
        home.getSubsetFile().put("a.c", a);
        program.getSubsetFile().put("b.txt", b);

        System.out.println(root);
        System.out.println(home);
        System.out.println(lib);
        System.out.println(program);


        String s;
        String[] inputHandled;
        String s1;
        while (true) {
            s = input.nextLine();
            inputHandled = s.split(" ");
            if (inputHandled.length > 1) {
                s1 = inputHandled[1];
            } else s1 = "#";
            switch (inputHandled[0]) {
                case "rm":
                    deleteFile(s1);
                    break;
                case "create":
                    createFile(s1);
                    break;
                case "vi":
                    break;
                case "mkdir":
                    createDir(s1, 35);
                    break;
                case "cd":
                    switchDir(inputHandled[1]);
                    break;
                case "delete":
                    deleteDir(inputHandled[1]);
                    break;
                case "dir":
                    System.out.println(this.nowDir);
                    break;
                default:
                    System.out.println("指令错误");
                    break;
            }
        }
    }


    public byte[] writeBlock(String str, int id) {
        int discBlock;
        byte[] bName = new byte[12];
        bName = str.getBytes();
        byte[] bId = new byte[4];
        bId = IndexBlock.intToByteArray(id);
        byte[] oneItem = new byte[16];
        for (int i =0;i<16;i++){
            for (int j =0;j<12;j++){
                oneItem[i] = bName[j];
                i++;
                if(bName.length <=i){
                    i = 12;
                    break;
                }
            }
            for (int j =0;j<4;j++){
                oneItem[i] = bId[j];
                i++;
            }
        }

        byte[] bName1 = new byte[12];
        byte[] bId1 =new byte[4];
        for(int i=0;i<16;i++){
            for (int j =0;j<12;j++){
                bName1[j] = oneItem[i];
                i++;
                if(oneItem[i] ==0)
                    break;
            }
            i = 12;
            for (int j =0;j<4;j++){
                 bId1[j]=oneItem[i] ;
                i++;
            }
            i--;
        }
        System.out.println(new String(bName1));
        System.out.println("******写入调试******");
        return oneItem;
    }

    /**
     * 创建目录
     *
     * @param name  目录名字
     * @param inode 索引节点
     */
    public void createDir(String name, int inode) {
        DirectoryType directory = new DirectoryType(name, inode, 0);
        initDir(this.nowDir, directory);
        this.nowDir.getSubsetFile().put(name, directory);
        System.out.println(this.nowDir);
    }

    /**
     * 切换路径
     *
     * @param name 绝对路径
     */
    public void switchDir(String name) {
        if (name.equals("..")) {
            this.nowDir = (DirectoryType) this.nowDir.getSubsetFile().get("..");
            System.out.println("--------------当前目录:" + this.nowDir.getName() + "--------------");
            System.out.println(this.nowDir);
            System.out.println("--------------当前目录:" + this.nowDir.getName() + "--------------");

        } else {
            String[] hand = name.split("/");
            this.nowDir = root;
            for (String s : hand) {
                for (HashMap.Entry<String, FileClass> sub : this.nowDir.getSubsetFile().entrySet()) {
                    if (sub.getValue().getName().equals(s)) {
                        this.nowDir = (DirectoryType) sub.getValue();
                        break;
                    }
                }
            }
            System.out.println("--------------当前目录:" + this.nowDir.getName() + "--------------");
            System.out.println(this.nowDir);
            System.out.println("--------------当前目录:" + this.nowDir.getName() + "--------------");
        }
    }

    public void deleteDir(String name) {
        if (this.nowDir.getSubsetFile().get(name).getType() == 1) {
            System.out.println("该文件是普通文件,命令使用错误");
            return;
        }
        DirectoryType dir = new DirectoryType("asd", 20, 1);
        dir = (DirectoryType) this.nowDir.getSubsetFile().get(name);

        int tag = 1;
        for (HashMap.Entry<String, FileClass> sub : dir.getSubsetFile().entrySet()) {
            if (!sub.getKey().equals("."))
                if (!sub.getKey().equals(".."))
                    if (sub.getValue().getType() == 0) {
                        System.out.println("不是最低目录不可以删除");
                        tag = 0;
                        break;
                    }
        }
        if (tag == 1) {
            this.nowDir.getSubsetFile().remove(name);
            System.out.println(this.nowDir);

        }
    }

    public void createFile(String name) {
        FileType f = new FileType(name, 0, 1);
        this.nowDir.getSubsetFile().put(name, f);
        System.out.println(this.nowDir);
    }

    public void deleteFile(String name) {

        if (this.nowDir.getSubsetFile().get(name).getType() == 0) {
            System.out.println("该文件是目录文件,命令使用错误");
            return;
        }
        FileType f = (FileType) this.nowDir.getSubsetFile().get(name);

        this.nowDir.getSubsetFile().remove(name);
        System.out.println(this.nowDir);
    }

}


/************************************封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印封印*************************************/

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
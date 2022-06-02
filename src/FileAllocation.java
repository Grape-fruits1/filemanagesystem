import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @program: filemanagesystem
 * @description: 文件分配
 * @author: Ran
 * @create: 2022-05-27 21:15
 **/

public class FileAllocation {
    int size = 2049;
    // arraylist_1为逻辑块数组、disc_block为物理块字节数组（二维）
    ArrayList<Integer> arraylist_1 = new ArrayList<Integer>(Collections.nCopies(10, 0));
    static byte[][] disc_block = new byte[512][20];
//    // 以下数组为arraylist_2的构成数组
//    ArrayList<Byte> arraylist_2_1 = new ArrayList<Byte>();
//    ArrayList<Byte> arraylist_2_2 = new ArrayList<Byte>();
//    ArrayList<Byte> arraylist_2_3 = new ArrayList<Byte>();


    /**
     * 计算输入块大小
     */
    public int calculate() {
        if (size % 512 == 0)
            return size / 512;
        else
            return size / 512 + 1;
    }

    /**
     * 记录逻辑块
     */
    public ArrayList<Integer> logicBlock() {
        int num = 0;
        // 通过calculate方法计算出当前文件所需物理块数量，并更新逻辑块
        for (int i = 0; i < arraylist_1.size() && num < calculate(); i++) {
            if (arraylist_1.get(i) == 0) {
                arraylist_1.set(i, 1);
                num++;
            }
        }
        // 返回当前逻辑块数组 arraylist_1
        return arraylist_1;
    }

    /**
     * 物理块的分配、以及与逻辑块的映射
     */
    public void realBlockAllocate(int size, byte[] a) {
        for (int i = 0; i < size; i++) {
            disc_block[i][0] = a[i];
        }
        arraylist_1 = logicBlock();
        System.out.println("index list:" + arraylist_1);
    }

//    /**
//     * 记录物理块
//     */
//    public void realBlock() {
//        byte a = 0;
//        byte b = 1;
//        arraylist_2.add(arraylist_2_1);
//        arraylist_2.add(arraylist_2_2);
//        arraylist_2.add(arraylist_2_3);
//        // 给所有物理块赋初值0
//        for (int i = 0; i < 16; i++) {
//            arraylist_2_1.add(a);
//            arraylist_2_2.add(a);
//            arraylist_2_3.add(a);
//        }
//        System.out.println();
//        System.out.println("System current Bitmap:");
//        for (int i = 0; i < arraylist_2.size(); i++) {
//            for (int j = 0; j < arraylist_2.get(i).size(); j++)
//                System.out.print(arraylist_2.get(i).get(j) + " ");
//            System.out.println();
//        }
//    }


//        if (arraylist_1.get(0) == 0) {
//        for (int i = 0; i < blocksNumber; i++) {
//            if (arraylist_1.get(i) == 0)
//                arraylist_1.set(arraylist_1.get(i), 1);
//        }
//        for (Integer integer : arraylist_1) {
//            System.out.print(integer);
//        }
//    private void Manage() {
//        bitmap = new int[row][column];
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < column; j++) {
//                int random = new Random().nextInt(2);
//                //Random随机数类.nextInt()方法生成随机数
//                bitmap[i][j] = random;
//            }
//        }
//    }
//
//    // 打印Bitmap
//    private void printBM() {
//        System.out.println("BitMap:");
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < column; j++) System.out.print(bitmap[i][j] + "  ");
//            System.out.println();
//        }
//    }
}
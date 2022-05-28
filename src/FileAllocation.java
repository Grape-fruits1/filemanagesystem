import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @program: filemanagesystem
 * @description: bitmap
 * @author: Ran
 * @create: 2022-05-27 21:15
 **/

public class FileAllocation {
    int row = 4, column = 8;       //行列数
    int[][] bitmap;                //位示图
    // arraylist_1为逻辑块数组、arraylist_2为物理块数组（二维）
    ArrayList<Integer> arraylist_1 = new ArrayList<Integer>(Collections.nCopies(10, 0));
    ArrayList<Integer> arraylist_2 = new ArrayList<Integer>();

    /**
     * 记录逻辑块
     */
    public void LogicBlock() {
        // 某文件所占块数
        arraylist_1.set(4, 2);
        int blocksNumber = 6;
        int num = 0;
        byte message = 10;
        int size_1 = arraylist_1.size();
        for (int i = 0; i < size_1 && num < blocksNumber; i++) {
            if (arraylist_1.get(i) == 0) {
                arraylist_1.set(i, 1);
                num++;
            }
        }
        // 输出逻辑块数组 arraylist_1
        for (Integer integer : arraylist_1)
            System.out.print(integer + " ");
    }

//        if (arraylist_1.get(0) == 0) {
//        for (int i = 0; i < blocksNumber; i++) {
//            if (arraylist_1.get(i) == 0)
//                arraylist_1.set(arraylist_1.get(i), 1);
//        }
//        for (Integer integer : arraylist_1) {
//            System.out.print(integer);
//        }

    private void Manage() {
        bitmap = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int random = new Random().nextInt(2);
                //Random随机数类.nextInt()方法生成随机数
                bitmap[i][j] = random;
            }
        }
    }

    // 打印Bitmap
    private void printBM() {
        System.out.println("BitMap:");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) System.out.print(bitmap[i][j] + "  ");
            System.out.println();
        }
    }
}
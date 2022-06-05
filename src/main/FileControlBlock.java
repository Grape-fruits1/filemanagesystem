package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author 17291
 * @version 1.0
 * @className FileControlBlock
 * @description TODO 文件控制块
 * @date 2022/6/5 14:21
 */
public class FileControlBlock {
        //访问权限：读、写、执行
        static final byte READABLE = 4;
        static final byte WRITABLE = 2;
        static final byte EXECUTABLE = 1;
        byte readability = READABLE;
        byte writability = WRITABLE;
        byte enforceability = EXECUTABLE;

        //文件创建时间
        Date creationTime;
        //最后修改时间
        Date modifiedTime;
        //文件所有者
        User owner;

        //文件名
        private String fileName;
        //索引节点
        private int inode;

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
}
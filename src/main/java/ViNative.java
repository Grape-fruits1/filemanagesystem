import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 本地函数vi模块
 * @author 17291
 */
public interface ViNative extends Library {
    /**
     * 加载vi模块动态库，源码为lin目录下library.cpp和libraru.h
     */
    ViNative vi = (ViNative) Native.load("lib/libvi1_0.dll", ViNative.class);
    /**
     * vi启动函数
     * 参数：文件名
     * 返回值：由c语言中char*转化来的String，其中前4个字符拼接成一个int型的数，指示后面数据所占字节数
     */

    String start(String fileName);
}

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author 17291
 */
public interface ViNative extends Library {
    ViNative vi = (ViNative) Native.load("lib/libvi1_0.dll", ViNative.class);

    void start(String fileName);
}

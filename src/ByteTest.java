import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @className: ByteTest
 * @author: Lodisaq
 * @description: byte使用测试
 * @date: 2022/5/26 14:03
 * @version: 1.0
 */
public class ByteTest
{
    public static void main(String args[]) throws UnsupportedEncodingException {
        //JAVA BYTE ARRAY DECLARATION
        byte b[], c; //b IS AN ARRAY a IS NOT AN ARRAY
        //MEMORY ALLOCATION FOR JAVA BYTE ARRAY
        b = new byte [ 512 ];
        String str = "你好世界!";

        //ASSIGNING ELEMENTS TO JAVA BYTE ARRAY
        /*b[ 0 ] = 20 ;
        b[ 1 ] = 10 ;
        b[ 2 ] = 30 ;
        b[ 3 ] = 'x' ;*/

        b = str.getBytes(StandardCharsets.UTF_8);
        String message = new String(b);
        c = 100 ;
        //JAVA BYTE ARRAY OUTPUT
        System.out.println( "Java byte Array Example" );
        System.out.println( "a value is : " +c);
        for ( int i= 0 ;i<b.length;i++)
        {
            System.out.println( "Element at Index : " + i + " " + b[i]);
        }
        System.out.println(str);
    }
}

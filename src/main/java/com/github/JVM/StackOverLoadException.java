package com.github.JVM;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StackOverLoadException {

    public static long sum2(long a){
        if(a == 1){
           return 1;
        }else{
           return sum2(a-1)*sum2(a);
        }
    }

    public static void main(String[] args) {
        System.out.println(sum2(10000));
    }
}

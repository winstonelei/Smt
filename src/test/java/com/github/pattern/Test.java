package com.github.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Test {

    public static void main(String[] args) {
        String str = "ming tian jiu yao fang jia le ,da jia。";
        System.out.println(str);
        String reg = "\\b[a-z]{4}\\b";

        //将规则封装成对象。
        Pattern p = Pattern.compile(reg);

        //让正则对象和要作用的字符串相关联。获取匹配器对象。
        Matcher m  = p.matcher(str);

        //System.out.println(m.matches());//其实String类中的matches方法。用的就是Pattern和Matcher对象来完成的。
        //只不过被String的方法封装后，用起来较为简单。但是功能却单一。

//		boolean b = m.find();//将规则作用到字符串上，并进行符合规则的子串查找。
//		System.out.println(b);
//		System.out.println(m.group());//用于获取匹配后结果。

        //System.out.println("matches:"+m.matches());
        while(m.find())
        {
            System.out.println(m.group());
            System.out.println(m.start()+"...."+m.end());
        }
    }
}

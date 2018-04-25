package com.github.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    public static void main(String[] args) {
        String mobile = "+8613534568797";
        String regex = "(\\+\\d+)?(1[3458]\\d{9})+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobile);
        if(matcher.matches()){
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }

        String regex1 = "(\\+\\d+)?(1[3458]\\d{9})+$";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(mobile);
        if(matcher1.matches()){
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }


        String mobile2 = "121212";
        String regex2 = "^([\\d+]+)$";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(mobile2);
        if(matcher2.matches()){
            System.out.println(matcher2.group());
            System.out.println(matcher2.group(1));
            System.out.println(matcher2.group(2));
        }


        Pattern NOT_AND = Pattern.compile("^[A-Za-z0-9]+$");
        String expression ="abcd";
        Matcher notAndmatcher = NOT_AND.matcher(expression);
        if (notAndmatcher.matches()) {
            System.out.println();
            System.out.println(notAndmatcher.group());
        }

        Pattern SPLITPATTERN = Pattern.compile("^[A-Za-z0-9_\\-\\.]+$");
        String image ="lg_img_1-bb.png";
        Matcher matcherImage = SPLITPATTERN.matcher(image);
        if (matcherImage.matches()) {
            System.out.println();
            System.out.println(matcherImage.group());
        }


        Pattern RANGE_STR = Pattern.compile("^((\\w*[A-Za-z_])([0-9]+))-((\\w*[A-Za-z_])([0-9]+))\\s?(\\[(\\d+)%\\])?$");
        String rangerStr ="abc1-abc20[30%]";
        Matcher rangerMatcher = RANGE_STR.matcher(rangerStr);
        if (rangerMatcher.matches()) {
            System.out.println();
            System.out.println(rangerMatcher.group());
        }

        Pattern RANGE_NUM = Pattern.compile("^([0-9]+\\s?(\\[(\\d+)\\])?)-([0-9]+\\s?(\\[(\\d+)%\\])?)$");
        String rangerNumber ="100-200[30%]";
        Matcher numberMatcher = RANGE_NUM.matcher(rangerNumber);
        if (numberMatcher.matches()) {
            System.out.println();
            System.out.println(numberMatcher.group());
        }


        Pattern OR = Pattern.compile("^[A-Za-z0-9_\\-\\.]+\\s?(\\[(\\d+)%\\])?(,\\s?[A-Za-z0-9_\\-\\.]+\\s?(\\[(\\d+)%\\])?)+$");
        String orString ="lg_img_1.png ,lg_img_1.png[20%]";
        Matcher orMatcher = OR.matcher(orString);
        if (orMatcher.matches()) {
            System.out.println();
            System.out.println(orMatcher.group());
        }

    }

}

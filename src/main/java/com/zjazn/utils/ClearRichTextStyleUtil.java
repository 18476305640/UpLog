package com.zjazn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClearRichTextStyleUtil {
    // 保证不会干扰页面, 富文本内容的">","<",是用别的符号表示 ,&gt; &lt; 所以不会清掉内容的东西,只会清除样式的
    final static String regex = "<.*?>|<.*";
    final static String subst = "";
    public static String getShowCharacter(String oriText, Integer length, String afterStr) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(oriText);
        // 被替换的值将包含在结果变量中
        String afterContent = matcher.replaceAll(subst);
        // 获取指定长度，且在后面加上指定字符
        if (afterContent.length()>length){
            afterContent=afterContent.substring(0,length)+afterStr;
        }
        return afterContent;
    }

    public static void main(String[] args) {
        System.out.println(getShowCharacter("<p>hello</p><a href",20,""));
    }
}

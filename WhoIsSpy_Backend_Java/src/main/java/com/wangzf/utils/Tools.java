package com.wangzf.utils;

import java.util.ArrayList;
import java.util.Random;

public class Tools {

    public static String[] generateWord(){
        ArrayList<String[]> wordList = new ArrayList<>();
        String[] str1 = {"苹果", "橘子"};
        String[] str2 = {"妖怪", "人妖"};
        String[] str3 = {"丝袜", "秋裤"};
        String[] str4 = {"泳裤", "内裤"};
        String[] str5 = {"恐龙", "母老虎"};
        String[] str6 = {"鼻毛", "胸毛"};
        String[] str7 = {"老虎", "狮子"};

        wordList.add(str1);
        wordList.add(str2);
        wordList.add(str3);
        wordList.add(str4);
        wordList.add(str5);
        wordList.add(str6);
        wordList.add(str7);

        Random random = new Random();
        String[] selectedWord = wordList.get(random.nextInt(wordList.size()));

        return selectedWord;

    }
    public static String generateRandomNumberString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            // 生成一个 0 到 9 的随机数
            int digit = random.nextInt(10);
            // 将随机数添加到 StringBuilder 中
            sb.append(digit);
        }

        return sb.toString();
    }
}

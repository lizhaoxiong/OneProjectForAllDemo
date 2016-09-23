package com.ok.utils.utils.compare;

import com.ok.utils.utils.AppUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Comparator;


/**
 * Created by lizhaoxiong on 2016/9/4.
 *
 */
public class CompareByabc implements Comparator<AppUtils.AppInfo> {
    @Override
    public int compare(AppUtils.AppInfo lhs, AppUtils.AppInfo rhs) {
        return sortByabc(lhs.getName(),rhs.getName());
    }

    private int sortByabc(String lhs, String rhs) {
        String pingYinL = getPingYin(lhs);
        String pingYin1R = getPingYin(rhs);
        int lhs_ascii = pingYinL.toUpperCase().charAt(0);
        int rhs_ascii =  pingYin1R.toUpperCase().charAt(0);
        // 判断若不是字母，则排在字母之后
        if (lhs_ascii < 65 || lhs_ascii > 90)
            return 1;
        else if (rhs_ascii < 65 || rhs_ascii > 90)
            return -1;

        if(lhs_ascii!=rhs_ascii){
            return lhs_ascii - rhs_ascii;
        }
        int h = lhs.length() - rhs.length();
        return h > 0 ? -1 : 1;
    }

    public String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (char curchar : input) {
                if (Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else
                    output += Character.toString(curchar);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

}

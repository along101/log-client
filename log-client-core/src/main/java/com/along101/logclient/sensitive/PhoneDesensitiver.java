package com.along101.logclient.sensitive;

import com.along101.logclient.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaochuanjun
 * @since 2017/4/6
 */
public class PhoneDesensitiver {

    private static final String regex = "0?(13|14|15|18)[0-9]{9}";

    private static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public static String filterPhone(String source) {
        return filterPhone(source, "***PHONE***");
    }

    public static String filterPhone(String source, String replacement) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.replaceAll(replacement);
        }
        return source;
    }

    public static void main(String[] args) {
        String str = "13101654965baike.xsoft340311198310300233lab.18101654965netasd;fjasdj340311198310300233owejkji,baike.sss,adsfjsaigo340311198310300233jgalksjf18930239579ljoija340311198310300233skdjfljsalgbaike.ljlkj340311198310300233";
        String filterPhone = PhoneDesensitiver.filterPhone(str, "***PHONE***");
        System.out.println(filterPhone);
    }
}

package com.along101.logclient.sensitive;

import com.along101.logclient.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaochuanjun
 * @since 2017/2/13
 */
public class EmojiDesensitiver {

    private static final Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]", Pattern.UNICODE_CASE | Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);

    public static String filterEmoji(String source) {
        return filterEmoji(source, "***EMOJI***");
    }

    public static String filterEmoji(String source, String replacement) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        String utf8Source;
        try {
            byte[] utf8Bytes = source.getBytes("UTF-8");
            utf8Source = new String(utf8Bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return source;
        }
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8Source);
        utf8Source = unicodeOutlierMatcher.replaceAll(replacement);
        return utf8Source;
    }
}

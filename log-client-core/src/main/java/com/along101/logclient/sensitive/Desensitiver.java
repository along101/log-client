package com.along101.logclient.sensitive;

/**
 * 脱敏器
 *
 * @author gaochuanjun
 * @since 2017/4/6
 */
public class Desensitiver {

    public static String desensitive(String source) {
        return PhoneDesensitiver.filterPhone(IdCardDesensitiver.filterIdCard(source));
    }

    public static void main(String[] args) {
        String str = "{\"appid\":\"10480003\",\"cardNumber\":\"6221560501640810\",\"iDNumber\":\"513002199406122566\",\"interfaceName\":\"BankCardRelationProxy\",\"name\":\"向毅\",\"userid\":\"44389111\"}";
        String filter = Desensitiver.desensitive(str);
        System.out.println(filter);
    }
}

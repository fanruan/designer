package com.fr.design.remote;

import com.fr.stable.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Utils {

    private static final String SOURCES =
            "._-~`ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    private Utils() {

    }

    public static Collection<? extends RemoteMember> getRemoteMember(String keyword) {
        // todo 使用决策平台api获取决策平台用户
        List<RemoteMember> res = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RemoteMember remoteMember = new RemoteMember(generate());
            if (StringUtils.isEmpty(keyword)) {
                res.add(remoteMember);
                continue;
            }
            if (remoteMember.getName().contains(keyword)) {
                res.add(remoteMember);
            }
        }
        return res;
    }


    /**
     * Generate a random string.
     *
     * @return String string
     */
    private static String generate() {
        Random random = new Random();
        char[] text = new char[6];
        for (int i = 0; i < 6; i++) {
            text[i] = Utils.SOURCES.charAt(random.nextInt(Utils.SOURCES.length()));
        }
        return new String(text);
    }
}

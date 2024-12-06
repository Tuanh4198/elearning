package io.yody.yodemy.elearning.service.helpers;

import org.nentangso.core.security.NtsSecurityUtils;

public class Helper {

    public static String getUserCodeUpperCase() {
        String userCode = NtsSecurityUtils.getCurrentUserLogin().get();
        return userCode.toUpperCase();
    }
}

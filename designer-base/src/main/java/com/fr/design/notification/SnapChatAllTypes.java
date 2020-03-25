package com.fr.design.notification;

import com.fr.stable.CommonUtils;

/**
 * created by Harrison on 2020/03/16
 **/
public abstract class SnapChatAllTypes {
    
    public enum Menu implements SnapChatKey {
        
        /**
         * 社区按钮
         */
        BBS("BBS");
        
        private static final String SIGN = "0001";
        
        private String key;
        
        Menu(String key) {
            this.key = key;
        }
        
        public String getKey() {
            return key;
        }
        
        @Override
        public String calc() {
            
            return CommonUtils.join(
                    new String[]{SIGN, getKey()}, "-"
            );
        }
    }
    
}

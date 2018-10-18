package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum SystemRoleType {
    CATERING_STAFF("配餐员"),
    CANTEEN_ADMIN("食堂负责人"),
    SYS_ADMIN("系统管理员");

    private String displayName;

    SystemRoleType(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

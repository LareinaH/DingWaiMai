package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum OrderStatus {
    SUBMITTED("已提交"),
    BILLED("已出单"),
    COMPLETED("已完成"),
    USER_CANCEL("用户取消"),
    ADMIN_CANCEL("商家取消"),
    SYS_CANCEL("系统取消");

    private String displayName;

    OrderStatus(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

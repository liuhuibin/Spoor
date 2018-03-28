package com.spoor;

/**
 * <p>Desc: Spoor's log level</p>
 * <p>Author:Created by huibin</p>
 * <p>Date: Created on 18/3/26 11:13</p>
 */

public enum LogLevel {

    INFO("I"),DEBUG("D"),WARNING("W"),ERROR("E");
    private String level;

    LogLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return level;
    }
}

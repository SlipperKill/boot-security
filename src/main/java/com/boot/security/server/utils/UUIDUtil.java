package com.boot.security.server.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * @return 32字节UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

package com.shls.s3backup.util;

import java.text.DecimalFormat;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 下午5:34
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class FileUtil {
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

package com.naver.goods.dto;

/**
 * @program: EnergyStorage
 * @ClassName: InfoResults
 * @description:
 * @author: ligang
 * @create: 2024-03-28 18:23:23
 */

public class InfoResults {

    public static InfoResult success() {
        return InfoResult.builder().code(200).message("success").build();
    }

    public static InfoResult error() {
        return InfoResult.builder().code(500).message("操作失败").build();
    }

    public static InfoResult success(String message) {
        return InfoResult.builder().code(200).message(message).build();
    }

    public static InfoResult error(String message) {
        return InfoResult.builder().code(500).message(message).build();
    }

    public static <T> InfoResult ok(String message, T data) {
        return InfoResult.builder().code(200).message(message).data(data).build();
    }

    public static <T> InfoResult ok(T data) {
        return InfoResult.builder().code(200).message("success").data(data).build();
    }
}

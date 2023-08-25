package com.example.myapp.commu.status;

public enum CommuFileDeleteYn {
    YES("Y", "삭제"),
    NO("N", "삭제하지 않음");

    private final String code;
    private final String description;

    CommuFileDeleteYn(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommuFileDeleteYn fromCode(String code) {
        for (CommuFileDeleteYn deleteyn : values()) {
            if (deleteyn.getCode().equals(code)) {
                return deleteyn;
            }
        }
        throw new IllegalArgumentException("Invalid CommuType code: " + code);
    }
}
package com.example.myapp.commu.status;

public enum CommuType {
    NORMAL("N", "일반글"),
    ANNOUNCEMENT("A", "공지글");

    private final String code;
    private final String description;

    CommuType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommuType fromCode(String code) {
        for (CommuType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CommuType code: " + code);
    }
}

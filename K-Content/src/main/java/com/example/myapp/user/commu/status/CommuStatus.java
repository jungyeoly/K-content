package com.example.myapp.user.commu.status;

public enum CommuStatus {
    NO("N", "신고 안한거"),
    YES("Y", "사용자가 삭제"),
    REPORT("R", "사용자가 신고"),
    KILL("K", "관리자가 신고된거 삭제처리");

    private final String code; //enum의 각 항목이 포함할 수 있는 추가적인 정보를 나타냄. code는 CommuStatus의 실제 문자값("N", "Y" 등)을 나타내며 desciption은 상태 설명을 나타냄("일반 게시글") 표시
    private final String description;

    CommuStatus(String code, String description) { //enum의 각 항목이 생성될 때 사용되는 생성자
        this.code = code;
        this.description = description;
    }

    public String getCode() { //enum의 code 및 description필드 값을 외부에서 가져올 수 있도록 해줌
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommuStatus fromCode(String code) { //주어진 'code' 값을 가진 'CommuStatus'항목을 찾아 반환하는 기능을 제공함.
        for (CommuStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CommuStatus code: " + code);
    }
}

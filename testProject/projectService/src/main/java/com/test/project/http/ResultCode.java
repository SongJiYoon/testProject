package com.test.project.http;

public enum ResultCode {
      success("0000","성공")
    , error("9999","실패")

    
    /* 공통(9000~) */
//    , service_inspection_time("9000", "<div style=\"font-size:30px;text-align:center;\"><b>스마트구몬 회원 이용제한 안내</b></div>" 
//                                    + "<div style=\"font-size:20px;\"><br/>인프라 증설 작업으로 인하여 서비스가 임시 <br/>제한됩니다.<br/>"
//    		                        + "고객 여러분께 불편을 드려 죄송합니다.<br/><br/></div>"
//                                    + "<div style=\"font-size:21px;\"><b>제한 일자 : 2018년 11월 15일(일)<br/>제한 시간 : 10:30 ~ 11:00<br/>"
//    		                        + "제한 내용 : 작업시간 동안 서비스 이용 불가</b></div>"
//                                    + "<div style=\"font-size:20px;\"><br/>빠른 시간 내에 작업을 완료하여 보다 나은 <br/>서비스로 보답하겠습니다.<br/>"
//    		                        + "궁금하신 사항은 고객센터(1588-5566)로 <br/>문의해 주시기 바랍니다.</div>")
    , service_inspection_time("9000", "<div style=\"font-size:30px;text-align:center;\"><b>스마트구몬 회원 이용제한 안내</b></div>" 
    								+ "<div style=\"font-size:20px;\"><br/>인프라 증설 작업으로 인하여 서비스가 임시 제한됩니다.<br/>"
    								+ "고객 여러분께 불편을 드려 죄송합니다.<br/><br/></div>"
    								+ "<div style=\"font-size:21px;\"><b>제한 일자 : 2018년 11월 15일(일)<br/>제한 시간 : 10:30 ~ 11:00<br/>"
    								+ "제한 내용 : 작업시간 동안 서비스 이용 불가</b></div>"
    								+ "<div style=\"font-size:20px;\"><br/>빠른 시간 내에 작업을 완료하여 보다 나은 서비스로 보답하겠습니다.<br/>"
    								+ "궁금하신 사항은 고객센터(1588-5566)로 문의해 주시기 바랍니다.</div>")
    , not_found_user("9001", "사용자 정보를 찾을 수 없습니다.")
    , not_found_inputVal("9002", "입력값이 존재하지 않습니다.")
    , not_found_vstTutor("9003", "방문교사 정보를 찾을 수 없습니다.")
    , not_korea_ip("9005", "대한민국 내에서만 사용 가능한 서비스입니다.\n현재 접속 지역이 해외이신 경우,\n귀국 후 사용 가능합니다.")
    ;

    String code;
    String msg;
    private ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
/* validation */
input.error, textarea.error{
    border: 1px dashed red;
}
label.error{
    margin-left:10px;
    color:red;
    display:none;
}
label.error.exception{
    margin-left:10px;
    color:blue;
    display:none;
}
</style>

<script>

var userIdChk   = "";
var userId      = '${accountInfo.userId}';
var confirmTxt  = "";

$(document).ready(function(){

    // 유효성 체크 함수 선언
    fn_checkValid();
    
    // 확인창 text
    confirmTxt = userId != "" ? '수정' : '등록';
    
    // 수정
    if(userId != ""){
        $("#userStsCd option[value='${accountInfo.userStsCd}']").attr("selected", "selected");
        $("#userId"   ).attr("readonly",true);
        $("#btn_check").css("display","none");
        $("#regist"   ).css("display","none");
    // 등록
    }else{
        $("#update"   ).css("display","none");
        $("#delete"   ).css("display","none");
    }
});

/* **************************
 * 유효성 체크 함수
/* ************************** */
function fn_checkValid(){

    $("#regForm").validate({
        ignore: [],
        rules : {
                     userId: { required: true , maxlength:"20", minlength:"1"}
                   , txtpwd: { required: function(){return  $("#userNo").val() == ""}, maxlength:"10"}
                   , userNm: { required: true , maxlength:"20" }
                   , telNo:  { required: true , number:true , maxlength:"20" }
                },
      messages: {
                     userId: { required: "아이디를 입력하세요."   , maxlength:"20자 이내로 입력해주세요." , minlength:"아이디를 입력해주세요."}
                   , txtpwd: { required: "패스워드를 입력하세요." , maxlength:"10자 이내로 입력해주세요." }
                   , userNm: { required: "이름을 입력하세요."     , maxlength:"20자 이내로 입력해주세요." }
                   , telNo:  { required: "전화번호를 입력하세요." , maxlength:"20자 이내로 입력해주세요." , number:"숫자로 입력해주세요."}
                }
    });
}

/* **************************
 * 아이디 체크
/* ************************** */
function fn_userIdCheck(){

    var userId = $("input[name=userId]").val();
    if(userId.length > 0){
        $.ajax({
            method: "POST",
            url   : "/admin/system/account/checkUserId.html",
            data  : { userId: userId }
        }).done(function( msg ) {
            if(msg == "Y"){
                alert("아이디가 이미 사용중입니다. 아이디를 다시 입력해주세요.");
                $("input[name=userId]").val("");
                userIdChk = "N";
            }else{
                alert("해당 아이디는 사용가능합니다.");
                userIdChk = "Y";
            }
        });
    }else{
        alert("아이디를 입력해주세요.");
    }
}

/* **************************
 * 등록 / 수정
/* ************************** */
function fn_checkFrm() {

    var pwd = $("#pwd").val();
    pwd = pwd.replace(/(^\s*)|(\s*$)/gi, "");
    
    // 아이디 체크(필수)
    if(userIdChk != "Y" && userId == ""){
        alert('아이디 체크를 해주세요.');
        return;
    }
    // 유효성 체크
    if(!($("#regForm").valid())){
        return;
    }
    // 등록, 수정 처리
    if(confirm("계정 정보를 "+confirmTxt+" 하시겠습니까?")){
        $("form[name=accountInfo]").submit();
        location.href="/admin/system/account/list.html";        
    }
}

/* ******************************
 * fillZero() : 0 채워넣는 함수
/* ****************************** */
function fillZero(value, length) {
    var result = "" + value;
    
    for ( var step = result.length; step < length; step++) {
        result = "0" + result;
    }
    return result;
}

/* ******************************
 * KeyDown 숫자만 입력 
/* ****************************** */
function fn_KeyDownOnlyNumber() {
    var code = window.event.keyCode;
    
    if (   (code >= 35 && code <= 40)  || (code >= 48 && code <= 57)
        || (code >= 96 && code <= 105) || code == 8   || code == 9
        ||  code == 13 || code == 46) {
        window.event.returnValue = true;
        return;
    }
    window.event.returnValue = false;
}

/* *********************************************
 * KeyPress 숫자만 입력(shift+숫자를 제외시킴) 
/* ********************************************* */
function fn_KeyPressOnlyNumber(){
      
    var code = window.event.keyCode;
    
    if(code >= 48 && code <= 57){
            window.event.returnValue = true;
            return;
    }
    window.event.returnValue = false;
}

/* ****************************
 * Input Box : passWord 처리 
/* **************************** */
$(function() {
    // cursor position get
    jQuery.fn.getCursorPosition = function() {
        if (this.lengh == 0) return -1;
        return $(this).getSelectionStart();
    }
    
    jQuery.fn.getSelectionStart = function() {
        if (this.lengh == 0) return -1;
        input = this[0];
    
        var pos = input.value.length;
    
        if (input.createTextRange) {
            var r = document.selection.createRange().duplicate();
            r.moveEnd('character', input.value.length);
            if (r.text == '') pos = input.value.length;
            pos = input.value.lastIndexOf(r.text);
        } else if (typeof(input.selectionStart) != "undefined") pos = input.selectionStart;
    
        return pos;
    }
    
    // keypress     
    $("#txtpwd").keypress(function(e) {
        setTimeout(function() {
            maskPassword(e)
        }, 100);
    });
    
    // keydown
    $("#txtpwd").keydown(function(e) {
        if (e.keyCode == 8 || e.keyCode == 46) {
            setTimeout(function() {
                maskPassword(e)
            }, 1);
        }
    });
});

/* ****************************
 * passWord * 표 처리 
/* **************************** */
function generateStars(n) {
    var stars = '';
    for (var i = 0; i < n; i++) {
        stars += '*';
    }
    return stars;
}

/* ****************************
 * passWord mask 처리 
/* **************************** */
function maskPassword(e) {

    var text    = $("#pwd").val();
    var stars   = $("#pwd").val().length;
    var unicode = e.keyCode ? e.keyCode : e.charCode;
    $("#keycode").html(unicode);
    
    // password cursor position get 
    var curPos    = $("#txtpwd").getCursorPosition();
    var PwdLength = $("#txtpwd").val().length;
    
    if (unicode != 9 && unicode != 13 && unicode != 37 && unicode != 40 && unicode != 37 && unicode != 39) {
        
        // not backSpace or del
        if (unicode != 8 && unicode != 46) {
            text = text + String.fromCharCode(unicode);
            stars += 1;
        }
        // backSpace or del
        else if (unicode == 8 && stars != PwdLength) {
            stars -= 1;
            text = text.substr(0, curPos) + text.substr(curPos + 1);
        } 
        $("#pwd").val(text);
        $("#txtpwd").val(generateStars(stars));
    }
}
</script>

<div class="container-fluid">
	<div class="animated fadeIn">
        <div class="row">
            <div class="col-lg-12">
		        <div class="card">
		            <div class="card-header">
                        <i class="fas fa-table"></i>
                        <strong>계정 ${empty accountInfo.userNo? '등록' : '수정'}</strong>
                    </div>
                    <div class="card-body" id="colorPickerBlock">
                        <form class="form-inline" action="addProc.json" method="POST" id="regForm" name="accountInfo">
                            <input type="hidden" id="userNo" name="userNo" value="${accountInfo.userNo}" />
                            <table class="table table-sm table-bordered table-striped">
                                <tbody>
                                    <tr>
                                        <th><label for="userId">아이디 </label></th>
                                        <td>
                                            <input type="text" class="form-control" id="userId" name="userId" value="${accountInfo.userId}" 
                                            <c:if test="${empty accountInfo.userNo}">autofocus="autofocus"</c:if> />
                                            <a class="btn btn-primary btn-flat" href="#" id="btn_check" onclick="javascript:fn_userIdCheck();">아이디체크</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><label for="userNm">이름</label></th>
                                        <td><input type="text" class="form-control" id="userNm" name="userNm" value="${accountInfo.userNm}" 
                                        <c:if test="${!empty accountInfo.userNo}">autofocus="autofocus"</c:if>/></td>
                                    </tr>
                                    <tr>
                                        <th><label for="txtpwd">비밀번호</label></th>
                                        <td>
                                            <div class="">
                                                <input type="text"  class="form-control"  id="txtpwd" name="txtpwd"  />
                                                <input type="hidden" id="pwd" name="pwd" size="15"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><label for="telNo">연락처</label></th>
                                        <td>
                                            <div class="">
                                                <input type="text" class="form-control" id="telNo" name="telNo" onkeydown="javascript:fn_KeyDownOnlyNumber();" onkeypress="javascript:fn_KeyPressOnlyNumber();" value="${accountInfo.telNo}"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr id= "group" >
                                        <th><label for="grpId">그룹선택</label></th>
                                        <td>
                                            <div>
                                                <c:forEach var="list" items="${checkGrpList}"  >
                                                    <label style="display:inline-block" class="col-form-label" for="${list.grpId}">
                                                    <input type="checkbox" name="grpId" id="${list.grpId}" value="${list.grpId}" 
                                                    <c:forEach var="item" items="${accountGrpList}" >
                                                        <c:if test="${item.grpId == list.grpId}">checked </c:if>
                                                    </c:forEach>     
                                                    />
                                                    ${list.grpNm}</label>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                </c:forEach>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><label for="userStsCd">상태</label></th>
                                        <td>
                                            <select class="form-control" id="userStsCd" name="userStsCd">
                                                <option value="OK">정상</option>
                                                <option value="CANCLE">계약해제</option>
                                            </select>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                  <div class="card-footer text-right">
                      <a class="btn btn-primary" href="#" onclick="fn_checkFrm();">${empty accountInfo.userNo? '등록' : '수정'}</a>
                      <a class="btn btn-primary btn-flat"  href="<%=request.getContextPath()%>/system/account/list.html">취소</a>
                  </div>
                </div>
            </div>
        </div>
    </div>
</div>


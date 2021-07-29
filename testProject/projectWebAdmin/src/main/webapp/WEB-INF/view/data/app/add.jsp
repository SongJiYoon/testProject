<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval expression="@webProp" var="webProp"/>

<style>
th{
    background-color:#f0f0f0;
    text-align:center;
    vertical-align:middle !important;
}
</style>

<div class="outer">
    <div class="inner bg-light lter">
        <div class="row">
            <div class="col-lg-12">
                <div class="box box box-solid">
                    <header>
                         <div class="icons"> <i class="fa fa-table"></i> </div>
                         <h5>${ appInfo.appDivCd eq "PRI" ? '초등' : '중등'} APP APK ${ appInfo.apkMngNo == 0 ? '등록' : '수정'}</h5>                     
                     </header>
                     <div class="body">
                        <h:form id="frm" name="frm" class="form-horizontal from-submit" action="addProc.json" object="appInfo" enctype="multipart/form-data" method="POST">
                            <input type="hidden" id="apkMngNo" name="apkMngNo" value="${appInfo.apkMngNo}" />
                            <input type="hidden" id="appDivCd" name="appDivCd" value="${appInfo.appDivCd}" />
                            <table class="table table-bordered">
                                <tbody>
                                    <c:if test="${appInfo.apkMngNo == 0}">
                                    <tr>
                                        <th class="col-lg-1">APK 파일</th>
                                        <td colspan="5">
                                            <div class="col-lg-12 form-inline">
                                                <input type="file" id="apkFile" name="apkFile" class="input-group" style="width:100%;" ${empty appInfo.apkFileNo ? 'required' : ''} />
                                                <input type="hidden" id="apkFileNo" name="apkFileNo" value="${appInfo.apkFileNo}" />
                                            </div>
                                        </td>
                                    </tr>
                                    </c:if>
                                    <c:if test="${appInfo.apkMngNo != 0}">
                                    <tr>
                                        <th class="col-lg-1">APK 버전</th>
                                        <td colspan="5">
                                            <div class="col-lg-12 form-inline">
                                                ${appInfo.verNm}
                                                <input type="file" id="apkFile" name="apkFile" class="input-group" style="width:100%; left: 50px;" />
                                                <input type="hidden" id="apkFileNo" name="apkFileNo" value="${appInfo.apkFileNo}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-lg-1">등록일</th>
                                        <td colspan="5">
                                            <div class="col-lg-12 form-inline">
                                                <fmt:formatDate value="${appInfo.regDttm}" pattern="yyyy.MM.dd" />
                                            </div>
                                        </td>
                                    </tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <div class="col-lg-12 text-right btn-toolbar sidebar-right-opened">
                                <a id="regist" class="btn btn-primary" >${ appInfo.apkMngNo == 0 ? '등록' : '수정'}</a>
                                <a id="del" class="btn btn-primary" >삭제</a>
                                <a class="btn btn-primary"  onclick="history.back();">목록</a>
                            </div>
                        </h:form>
                    </div>
                    <br/>
                    <br/>
                </div>
            </div>
        </div>
    </div>
</div>


<script type='text/javascript'>

var contextPath = '<%=request.getContextPath()%>';

/* 저장 버튼 클릭 */
function fn_save(){
	
	var file = frm.apkFile.value; 
	var fileExt = file.substring(file.lastIndexOf('.')+1); 
	
    if(fileExt != "apk"){
    	alert("apk 파일로 등록해주세요.");
    	return false;
    }

    if(confirm("저장 하시겠습니까?")){
	    $("form[name='frm']").submit();
	}
}

function fn_del(){
	if(confirm("삭제 하시겠습니까?")){
		$("#frm").attr('action', 'appDel.json');
	    $("#frm").submit();
	}
}

$(document).ready(function() {
	
    $('#regist').click(function(){
    	fn_save();
    });  
    
    $('#del').click(function(){
    	fn_del();
    }); 
    
     $("#frm").ajaxForm({
    	success: function(data){
    		if(data["resultCode"] == "9999"){
       			alert(data["resultMsg"]);
    		}else{
        		alert(data["resultMsg"]);
        		if("${appInfo.appDivCd}" == "PRI"){
            		location.href=contextPath+'/data/app/list.html';
        		}else{
            		location.href=contextPath+'/data/app/listMid.html';        			
        		}
    		}
    	},
    	error: function(data){
    		alert(data["resultMsg"]);
    	}
    }); 
}); 

</script>
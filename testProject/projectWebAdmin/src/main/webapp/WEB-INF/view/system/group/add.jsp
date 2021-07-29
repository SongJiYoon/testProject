<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
th{
    background-color:#f0f0f0;
}
</style>

<script>
var saveArray   = new Array();
var delArray    = new Array();

$(document).ready(function(){

    // 전체 선택 , 전체 해제
    $("#chkSubAll").click(function(){
        if($("#chkSubAll").is(":checked")){
            $("input[name=chk]").prop("checked",true);
        }else{
            $("input[name=chk]").prop("checked",false);
        }
    });
});

/* ***************************
 * 저장 버튼 클릭 이벤트
/* *************************** */
function fn_save(){
    var chkCnt    = 0;
    var grpIdFlag = false; 
    var grpNmFlag = false;

    $("input[name=chk]").each(function(){
        if(this.checked){
            chkCnt++;
        }
    });

    // 그룹 아이디, 그룹 명 체크
    $("input[name=chk]").each(function(index){
        if(this.checked){
            if($("input[name=grpId]").eq(index).val()==""){
                alert("그룹 아이디가 입력되지 않았습니다.");
                grpIdFlag = true;
                return false;
            }
            if($("input[name=grpNm]").eq(index).val()==""){
                alert("그룹명이 입력되지 않았습니다.");
                grpNmFlag = true;
                return false;
            }
        }
    });
    
    if(grpIdFlag || grpNmFlag){
        return;
    }
    
    // 체크된 값만 저장 
    $("input[name=chk]").each(function(i){
        var grpIdVal = $("input[name=chk]").eq(i).val();
        if(this.checked){
            saveArray.push({  grpId : grpIdVal == "on" || grpIdVal == "undefined" || grpIdVal == "" ? $("input[name=grpId]").eq(i).val() : $("input[name=chk]").eq(i).val()   // 새로추가한 값 : 기존값  
                            , grpNm : $("input[name=grpNm]").eq(i).val()
                            , useYn: "Y"});
        }
    });

    if(delArray == "" && saveArray == ""){
        alert("저장할 내역이 없습니다.");
        return;
    }
    
    // 저장 처리
    if(confirm("저장 하시겠습니까?")){
        if(delArray != null){ // 삭제 Data 존재
            $.merge(saveArray, delArray); // Data Merge 
        }
        if(saveArray != ""){
            f_ajax("/admin/system/group/addProc.json", saveArray, fn_result);
        }
    }else{
    	// 초기화
        saveArray = [];
    }
}

/* **************************
 * 저장 처리 결과
/* ************************** */
function fn_result(data){
	// 초기화
	delArray = [];
	saveArray= [];
	
    if(data.resultCode=="0000"){
        alert("저장 되었습니다.");
        location.href="/admin/system/group/add.html"; 
    }
}

/* ***************************
 * 행추가 버튼 클릭 이벤트
/* *************************** */
function fn_add(){
 
    var rowHtml = "";
    rowHtml =  '<tr>';
    rowHtml += '<td align="center" ><input type="checkbox" checked name="chk"/></td>';
    rowHtml += '<td align="center" ><input type="text" class="form-control" name="grpId"  onkeypress="javascript:fn_setCheckRow(this);" maxlength="20" /></td>';
    rowHtml += '<td align="center" ><input type="text" class="form-control" name="grpNm"  onkeypress="javascript:fn_setCheckRow(this);" maxlength="20" /></td>';
    rowHtml += '</tr>';

    $("#codeTr").append(rowHtml);
}


/* ***************************
 * 삭제 버튼 클릭 이벤트
/* *************************** */
function fn_del(){
    
    var chkCnt  = 0;
    var delFlag = false;

    // 체크 값 count
    $("input[name=chk]").each(function(){
        if(this.checked){
            chkCnt++;
        }
    });

    if(chkCnt == 0){
        alert("삭제할 행을 선택해 주세요.");
        return;
    }

    $("input[name=chk]").each(function(){
        if(this.checked){
            // 기존 등록된 그룹 아이디 
            if(this.value != undefined && this.value != ""&& this.value != "on"){
                if(delArray != ""){
                    for(var i=0; i < delArray.length; i++){
                        if(delArray[i] == this.value){
                            delFlag = true;
                        }
                    }
                }
                if(!delFlag) delArray.push({grpId : this.value , grpNm : $(this).parent().parent().find("input[name=grpNm]").val() , useYn: "N"}); // 삭제 data
                $(this).parent().parent().remove();
            }else{
                $(this).parent().parent().remove();
            }
        }
    });
}


/* ***********************
 * keyup 이벤트
/* *********************** */
function fn_setCheckRow(obj){
    var rowIndex = $(obj).parent().parent().parent().children().index($(obj).parent().parent());
    $("input[name=chk]").eq(rowIndex).prop("checked",true);
}

</script>

<div class="container-fluid">
	<div class="animated fadeIn">
    <form name="frm" id="frm">
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-table"></i>
                        <strong>그룹관리</strong>
                    </div>
                    <div id="infoCollapse" class="card-body" style="width:100%;float:left;">
                        <div class="text-right sidebar-right-opened"  style="width:100%;">
                            <a class="btn btn-primary btn-flat" style="left:400px;" href="javascript:fn_add();">행추가</a>
                            <a class="btn btn-primary btn-flat btn-danger" style="left:300px;" href="javascript:fn_del();">행삭제</a>
                        </div><br/>
                        <table id="dataTable" class="table table-sm table-bordered table-hover table-condensed" style="width:100%;">
                            <thead>
                                <tr class="success">
                                    <th class="text-center"><input type="checkbox" id="chkSubAll"/></th>
                                    <th class="text-center">그룹아이디</th>
                                    <th class="text-center">그룹명</th>
                                </tr>
                            </thead>
                            <tbody id="codeTr">
                                <c:choose>
                                    <c:when test="${empty list}">
                                        <tr>
                                            <td colspan="3" style="text-align:center;">조회된 데이터가 없습니다.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${list}">
                                            <tr>
                                                <td align="center" ><input type="checkbox" name="chk" value="${item.grpId}" /></td>
                                                <td align="center" ><input type="text" class="form-control" name="grpId" value="${item.grpId}"  onkeypress="javascript:fn_setCheckRow(this);" maxlength="20" disabled="disabled"/></td>
                                                <td align="center" ><input type="text" class="form-control" name="grpNm" value="${item.grpNm}"  onkeypress="javascript:fn_setCheckRow(this);" maxlength="20"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
				    <div class="card-footer text-right sidebar-right-opened">
				        <a class="btn btn-primary btn-flat" style="left:200px;" href="javascript:fn_save();">저장</a>
				    </div>
                </div>
           </div>
        </div>
    </form>
</div>
</div>

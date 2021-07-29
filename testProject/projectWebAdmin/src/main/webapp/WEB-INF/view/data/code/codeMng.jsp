<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
th{
	background-color:#f0f0f0;
}
</style>
<script>
var addRowCnt = 0;
var addRowCnt1 = 0;

var realDataCnt = 0;
var SecRealDataCnt = 0;

var saveArray = new Array();

var delArray = [];

var parCodeVal = "";

$(document).ready(function(){
	//alert("대분류 선택 또는 체크박스 선택 및 클릭시 소분류 재조회할 것.");
	// 유효성 체크 함수 선언
    //fn_checkValid();
	
	/*--------------------------------------*/
	/*전체 선택/전체 해제 [대분류]    					*/
	/*--------------------------------------*/
	$("#chkAll").click(function(){
		if($("#chkAll").is(":checked")){
			$("input[name=chk]").prop("checked",true);
		}else{
			$("input[name=chk]").prop("checked",false);
		}
	});
	
	/*--------------------------------------*/
	/*전체 선택/전체 해제 [소분류]    					*/
	/*--------------------------------------*/
	$("#chkSubAll").click(function(){
		if($("#chkSubAll").is(":checked")){
			$("input[name=chk1]").prop("checked",true);
		}else{
			$("input[name=chk1]").prop("checked",false);
		}
	});
	
	/*--------------------------------------*/
	/*대분류 페이징 처리      	    					*/
	/*--------------------------------------*/
	$('#pageList').bootpag({}).on("page", function(event, /* page number here */ num){
		$('#page').val(num);
		fn_selectList();
	});
	
	/*--------------------------------------*/
	/*조회 버튼 	      	    					*/
	/*--------------------------------------*/ 
    $('#searchBtn').click(function(){
    	$('#page').val(1);
    	$('#totCnt').val("");
    	fn_selectList();
    });
	
	fn_selectList();
});

/*--------------------------------------*/
/*대/소분류 리스트 가져오기         					*/
/*--------------------------------------*/
function fn_selectList(){
	delArray = [];
	saveArray = [];
	
	var data = {codeName : $("input[name=name]").val() , useYn : $("select[name=useYn]").val()};
	data["page"] = $("#page").val();
	data["totCnt"] = $("#totCnt").val();
	
	f_ajax("<%=request.getContextPath()%>/data/code/codeList.json", data ,fn_selectListCallBack);
}

/*--------------------------------------*/
/*대분류 리스트 가져오기 콜백함수      				    */
/*--------------------------------------*/
var idxNum = 0;
function fn_selectListCallBack(data){
	
	$('#parCodeTr, #codeTr').empty();
	var totCnt  = 0;
	var page    = 1;
	var rowHtml = "";
	
	var bgcolor = "";
	$.each(data.list, function(index, entry) {
		
		if(data.list.length-1 == index) {
			bgcolor = "#f0f0f0";
			parCodeVal = entry.cmmGrpCd;
			fn_selectSbuCodeList(this,parCodeVal);
			idxNum = entry.rn;
		}else{  
			bgcolor  = "";
		}
		
		parCodeVal = entry.cmmGrpCd;
        
   		rowHtml += '<tr bgcolor="'+bgcolor+'">';
   		rowHtml += '<td align="center" ><input type="checkbox" name="chk" value="'+entry.cmmGrpCd+'"/></td>';
   		rowHtml += '<td align="center" onclick="javascript:fn_selectSbuCodeList(this,\''+parCodeVal+'\',\''+index+'\');">'+entry.rn+'</td>';
   		rowHtml += '<td align="center" hidden><input type="text" class="form-control" id="code'+(realDataCnt+1)+'" name="code1" readonly value="'+entry.cmmGrpCd+'"/></td>';
   		rowHtml += '<td align="center" ><input type="text" class="form-control" id="codeName'+(realDataCnt+1)+'" name="codeName" value="'+entry.cdNm+'" onkeyup="javascript:fn_setCheck(this);"/></td>';
   		rowHtml += '<td align="center" >';
   		rowHtml += '	<select class="form-control" id="useYn'+(realDataCnt+1)+'" name="useYn1" onchange="javascript:fn_setCheck(this);">';
   		  
   		var selectY = "";
   		var selectN = "";
   		
   		if(entry.useYn == "Y") selectY = "selected";
   		else selectY = "";
   		
   		if(entry.useYn == "N") selectN = "selected";
   		else selectN = "";
   		
   		rowHtml += '		<option value="Y" '+selectY+'>Y</option>';
   		rowHtml += '		<option value="N" '+selectN+'>N</option>';
   		rowHtml += '	</select>';
   		rowHtml += '</td>';
   		rowHtml += '</tr>';
   		
		
    });
    
	$('#parCodeTr').append(rowHtml);
	
    if(data.list.length == 0){
        $('#parCodeTr').append(
                "<tr>"
               +"<td align='center' colspan='4']>조회된 데이터가 없습니다.</td>"
               +"</tr>"
        );
        $('#codeTr').append(
                "<tr>"
               +"<td align='center' colspan='7']>조회된 데이터가 없습니다.</td>"
               +"</tr>"
        );
        $('#pageList').html("");
    }else{
    	var totCnt = data.list[0].totCnt;
    	var page = data.list[0].page;

    	var totPage = Math.ceil(parseInt(totCnt) / 10);
    	//alert("총 페이지 " + totPage);
    	if(isNaN(totPage)) totPage = 0;
    	
    	$('#pageList').bootpag({
    		total: totPage, /* 총 페이지 수 */
    	   	page: parseInt(page), /* 현재 페이지 */
    	   	maxVisible: 10, /* 하단에 보여주는 페이지버튼 수 / 페이지 당 보여주는 리스트 수 아님 */
    	   	leaps: true
    	});  
    	
    	$('#page').val(page);
        $('#totCnt').val(totCnt);
        
        $('#pageList ul').addClass('justify-content-center');
        $('#pageList li').addClass('page-item');
        $('#pageList a').addClass('page-link');
    }
}

/*--------------------------------------*/
/*소분류 리스트 가져오기         					*/
/*--------------------------------------*/
function fn_selectSbuCodeList(obj,parCodeVal1,index){
	var flag = false;
	$("input[name=chk1]").each(function(){
		if(this.checked) flag = true;
	});
	if(flag){
		if(!confirm("소분류에 저장되지 않은 내역이 있습니다. 저장하지 않으시겠습니까?")){
			return false;
		}
	}
	if(obj != undefined){
		$("#parCodeTr tr").attr("bgcolor","");
		
		$(obj).parent().attr("bgcolor","#f0f0f0");
		$(obj).parent().find("input[name=chk]").prop("checked",true);
		//alert($(obj).find("input[name=chk]").val());
		parCodeVal = $(obj).parent().find("input[name=chk]").val();
		
		if(parCodeVal == undefined) {
			parCodeVal = parCodeVal1;
			$(obj).parent().eq(index).attr("bgcolor","#f0f0f0");
			//$(obj).parent().eq(":not("+index+")").attr("bgcolor","");
			//$(obj).parent().not(":eq("+index+")").removeAttr("bgcolor");
			
		}
		
		if(parCodeVal1 != undefined) {
			
			//alert($(obj).parent().not(":eq("+index+")"));
			
			parCodeVal = parCodeVal1;
			//alert(eval($(obj).parent()));
			$(obj).parent().eq(index).attr("bgcolor","#f0f0f0");
			//$(obj).parent().not(":eq("+index+")").attr("bgcolor","");
		}
	}else{
		$('#codeTr').empty();
		parCodeVal = "";
		return;
	}
	//return true;
	
	$('#codeTr').empty();
	var data = {code: parCodeVal};
	f_ajax("<%=request.getContextPath()%>/data/code/subCodeList.json", data ,fn_selectSubListCallBack);
}
/*--------------------------------------*/
/*소분류 리스트 가져오기 콜백함수      				    */
/*--------------------------------------*/
function fn_selectSubListCallBack(data){
	//alert(JSON.stringify(data));
	
	$('#codeTr').empty();
	var rowHtml = "";
	var cnt = 1;
	
	$.each(data.list, function(index, entry) {
       
   		rowHtml += '<tr>';
   		rowHtml += '<td align="center" ><input type="checkbox" name="chk1" value="'+entry.subCode+'"/></td>';
   		rowHtml += '<td align="center" >'+(cnt)+'</td>';
   		rowHtml += '<td align="center" ><input type="text" name="code" value="'+entry.cmmGrpCd+'" readonly style="border:0px;text-align:center;"/></td>';
   		rowHtml += '<td align="center" ><input type="text" class="form-control" name="subCode" value="'+entry.subCode+'"  onkeyup="javascript:fn_setCheckRow(this);" /></td>';
   		rowHtml += '<td align="center" ><input type="text" class="form-control" name="codeSubName" value="'+entry.codeSubName+'"   onkeyup="javascript:fn_setCheckRow(this);"/></td>';
   		rowHtml += '<td align="center" ><input type="text" class="form-control" name="order" value="'+entry.cdOdr+'"   onkeyup="javascript:fn_setCheckRow(this);" onkeydown="javascript:fn_KeyDownOnlyNumber();" onkeypress="javascript:fn_KeyPressOnlyNumber();set_comma(this);" style="text-align:right;"/></td>';
   		
   		rowHtml += '<td align="center" >';
   		rowHtml += '	<select class="form-control" id="useYn'+(cnt)+'" name="subUseYn" onchange="javascript:fn_setCheckRow(this);">';
   		
   		var selectY = "";
   		var selectN = "";
   		
   		if(entry.useYn == "Y") selectY = "selected";
   		else selectY = "";
   		
   		if(entry.useYn == "N") selectN = "selected";
   		else selectN = "";
   		
   		rowHtml += '		<option value="Y" '+selectY+'>Y</option>';
   		rowHtml += '		<option value="N" '+selectN+'>N</option>';
   		rowHtml += '	</select>';
   		rowHtml += '</td>';
   		rowHtml += '</tr>';
   		
   		cnt++;
    });
    
	$('#codeTr').append(rowHtml);
	
    if(data.list.length == 0){
        $('#codeTr').append(
                "<tr>"
               +"<td align='center' colspan='7']>조회된 데이터가 없습니다.</td>"
               +"</tr>"
        );
    }
}

/*--------------------------------------*/
/*소분류 리스트 가져오기         					    */
/*--------------------------------------*/
function fn_selectSubList(obj){
	var flag = false;
	$("input[name=chk1]").each(function(){
		if(this.checked) flag = true;
	});
	if(flag){
		if(!confirm("소분류에 저장되지 않은 내역이 있습니다. 저장하지 않으시겠습니까?")){
			return false;
		}
	}
	if(obj != undefined){
		$(obj).parent().attr("bgcolor","#f0f0f0");
		$(obj).parent().find("input[name=chk]").prop("checked",true);
		//alert($(obj).find("input[name=chk]").val());
		parCodeVal = $(obj).parent().find("input[name=chk]").val();
	}
	return true;
	
	//f_ajax("<c:url value='/data/com/codeSubList.html'/>","",fn_selectSubCodeList);
}
/*--------------------------------------*/
/* 대분류 행추가 버튼 클릭 이벤트					*/
/*--------------------------------------*/
function fn_addRow(){
// 	if(addRowCnt1 > 0){
// 		alert("소분류 등록 완료 후, 행추가 하세요.");
// 		return;
// 	}
	//$("#codeTr").html("");
	
	if($("#parCodeTr tr").find("td").attr("colSpan")== 4){
		$("#parCodeTr").empty();	
	}
	
	if(idxNum > 0 ){
		realDataCnt = idxNum++; 
	}else{
		realDataCnt = $("#parCodeTr tr").size();
	}
	SecRealDataCnt = $("#codeTr tr").size();
	
	var rowHtml = "";
	addRowCnt++;
	
	$("#parCodeTr tr").attr("bgcolor","");
	//$("#parCodeTr tr").eq(realDataCnt).css("bgcolor","blue");
	
	rowHtml = '<tr bgcolor="#f0f0f0">';
	rowHtml += '<td align="center" ><input type="checkbox" checked name="chk" value=""/></td>';
	rowHtml += '<td align="center" onclick="javascript:fn_selectSbuCodeList();">'+(realDataCnt+1)+'</td>';
	rowHtml += '<td align="center" hidden><input type="text" class="form-control" id="code'+(realDataCnt+1)+'" name="code1" readonly/></td>';
	rowHtml += '<td align="center" ><input type="text" class="form-control" id="codeName'+(realDataCnt+1)+'" name="codeName" onkeyup="javascript:fn_setCheck(this);"/></td>';
	rowHtml += '<td align="center" >';
	rowHtml += '	<select class="form-control" id="useYn'+(realDataCnt+1)+'" name="useYn1" onchange="javascript:fn_setCheck(this);">';
	rowHtml += '		<option value="Y">Y</option>';
	rowHtml += '		<option value="N">N</option>';
	rowHtml += '	</select>';
	rowHtml += '</td>';
	rowHtml += '</tr>';
	
	var flag = fn_selectSubList();
	if(flag) {
		$("#parCodeTr").append(rowHtml);
		$("#codeTr").empty();
		parCodeVal = "";
		//fn_selectSbuCodeList(null,"",null);
	}
}

/*--------------------------------------*/
/* 대분류 삭제 버튼 클릭 이벤트					*/
/*--------------------------------------*/
function fn_delRow(){
	var chkCnt = 0;
	$("input[name=chk]").each(function(){
		if(this.checked){
			chkCnt++;
		}
	});
	
	if(chkCnt == 0){
		alert("삭제할 행을 선택해 주세요.");
		return;
	}
	var delFlag = false;
	var idx = "";
	//if(confirm("삭제하시면 데이터를 잃을 수 있습니다. 정말로 삭제하시겠습니까?")){
		$("input[name=chk]").each(function(i){
			if(this.checked){
				if(this.value != undefined && this.value != "" && this.value != "on"){
					if(delArray != ""){
						for(var i=0;i<delArray.length;i++){
							if(delArray[i] == this.value){
								delFlag = true;
							}
						}
					}
					if(!delFlag) delArray.push({code : this.value , subCode : "" ,codeName : $(this).parent().parent().find("input[name=codeName]").val(), codeSubName : "" , order : "", useYn : "N" , flag : "parent",originCode :""});
					$(this).parent().parent().remove();	
				}else{
					$(this).parent().parent().remove();	
				}				
			}
		});
		//alert(delArray);
		//if(saveArray != ""){
		//	alert("아작스 통신");
		//}
	//}
}


/*--------------------------------------*/
/* 대분류 저장 버튼 클릭 이벤트					*/
/*--------------------------------------*/
// function fn_saveRow(){
// 	var chkCnt = 0;
// 	$("input[name=chk]").each(function(){
// 		if(this.checked){
// 			chkCnt++;
// 		}
// 	});
	
// 	if(chkCnt == 0){
// 		alert("저장할 행을 선택해 주세요.");
// 		return;
// 	}
	
// 	var flag = false;
// 	$("input[name=chk]").each(function(index){
// 		if(this.checked){
// 			if($("input[name=codeName]").eq(index).val()==""){
// 				alert((index+1)+"의 행의 코드명이 입력되지 않았습니다.");
// 				flag = true;
// 				return false;
// 			}
// 		}
// 	});
	
// 	if(flag){
// 		return;
// 	}
	
// 	var addArray = new Array();
// 	if(confirm("저장 하시겠습니까?")){
// 		$("input[name=chk]").each(function(i){
// 			if(this.checked){
// 				addArray.push({"chk" : this.value,
// 							  "code" : $("input[name=code]").eq(i).val(),
// 							  "codeName" : $("input[name=codeName]").eq(i).val(),
// 							  "useYn" : $("input[name=useYn]").eq(i).val()});
// 			}
// 		});
// 		//alert(addArray);
// 		if(addArray != ""){
// 			//alert("아작스 통신");
// 		}
// 	}
// }


/*--------------------------------------*/
/* 저장 버튼 클릭 이벤트					        */
/*--------------------------------------*/
function fn_save(){
	var chkCnt = 0;
	var chkCnt1= 0;
	$("input[name=chk1]").each(function(){
		if(this.checked){
			chkCnt++;
		}
	});
	
	$("input[name=chk]").each(function(){
		if(this.checked){
			chkCnt1++;
		}
	});
	
// 	if(chkCnt == 0 && chkCnt1 == 0){
// 		alert("저장할 행을 선택해 주세요.");
// 		return;
// 	}
	
	var flag = false;
	var flag1= false;

	$("input[name=chk]").each(function(index){
		if(this.checked){
			if($("input[name=codeName]").eq(index).val()==""){
				alert("대분류 "+(index+1)+"의 행의 코드명이 입력되지 않았습니다.");
				flag1 = true;
				return false;
			}
		}
	});
	
	$("input[name=chk1]").each(function(index){
		if(this.checked){
			if($("input[name=subCode]").eq(index).val()==""){
				alert("소분류 "+(index+1)+"의 행의 코드가 입력되지 않았습니다.");
				flag = true;
				return false;
			}
			if($("input[name=codeSubName]").eq(index).val()==""){
				alert("소분류 "+(index+1)+"의 행의 코드명이 입력되지 않았습니다.");
				flag = true;
				return false;
			}
			if($("input[name=order]").eq(index).val()==""){
				alert("소분류 "+(index+1)+"의 행의 정렬순서가 입력되지 않았습니다.");
				flag = true;
				return false;
			}
		}
	});
	//if(!$("#frm").valid()) return;
	
	if(flag || flag1){
		return;
	}
	
	
	//alert("대분류 저장 후, 소분류는 대분류 저장 성공 후, 콜백 함수로 처리하여 소분류 테이블의 대분류코드는 맨 마지막에 등록한 코드로 한다.\n (새로운 생성시, 단,기존에 존재하는 대분류 코드인 경우는 제외함.)");
	$("input[name=chk1]").each(function(i){
		if(this.checked){
			saveArray.push({code: parCodeVal 
				     , subCode : $("input[name=subCode]").eq(i).val() 
				     , codeName : ""
				     , codeSubName : $("input[name=codeSubName]").eq(i).val()
				     , order : $("input[name=order]").eq(i).val()
				     , useYn : $("select[name=subUseYn]").eq(i).val()
				     , flag : "sub"
				     , originCode : this.value});
		}
	});
	$("input[name=chk]").each(function(i){
		if(this.checked){
			saveArray.push({code: $("input[name=code1]").eq(i).val() 
				     , subCode : "" 
				     , codeName : $("input[name=codeName]").eq(i).val()
				     , codeSubName : ""
				     , order : ""
				     , useYn : $("select[name=useYn1]").eq(i).val()
				     , flag : "parent"
				     , originCode : ""});
		}
	});
	
	if(delArray == "" && saveArray == ""){
		alert("저장할 내역이 없습니다.");
		return;
	}
	if(confirm("저장 하시겠습니까?")){
		if(delArray != null){
			//saveArray.push(delArray);
			$.merge(saveArray,delArray);
			
			//alert(JSON.stringify(delArray));
		}
		//console.log(JSON.stringify(saveArray));
		
		if(saveArray != ""){
			//alert("아작스 통신");
			f_ajax("<%=request.getContextPath()%>/data/code/regist.json", saveArray, fn_registSub);
		}
	}else{
		saveArray = [];
	}
}

/*--------------------------------------*/
/* 대분류 등록
/*--------------------------------------*/
function fn_registSub(data){
	if(data.resultCode=="0000"){
		//alert(JSON.stringify(data));
		alert("저장 되었습니다.");
		$('#page').val(1);
		fn_selectList();
	}else{
		delArray = [];
		saveArray= []; 
	}
	
	if(data.resultCode=="0000"){
		//f_ajax("<%=request.getContextPath()%>/data/code/registSub.json", saveArray, fn_registSubResult);
	}
}

// function fn_registSubResult(data){
// 	if(data.resultCode=="0000"){
// 		alert(JSON.stringify(data));
// 		alert("저장 되었습니다.");
// 	}
// }
/*--------------------------------------*/
/* 소분류 행추가 버튼 클릭 이벤트					*/
/*--------------------------------------*/
function fn_add(){
	var chkAddCnt = 0;
	
	$("input[name=chk]").each(function(){
		if(this.value == undefined || this.value == "" || this.value == "on"){
			chkAddCnt++;
		}else{
			if(this.checked){
				chkAddCnt++;
				//parCodeVal = this.value; 
			}
		}
	});
	
	var flag = false;
	$("input[name=chk1]").each(function(){
		var value = this.value;

		$("input[name=chk1]").each(function(){
		    if(!$.contains(this.value, value)){
		    	flag = true; 
			}
		});
	});
	
// 	if(chkAddCnt == 1){
// 		if(flag){
// 			alert("소분류 등록시,대분류를 변경할 수 없습니다.");
// 			return;
// 		}
// 	}
// 	if(chkAddCnt > 1){
// 		alert("대분류를 여러개 등록하실때 소분류를 등록할 수 없습니다.");
// 		return;
// 	}
	
	var rowHtml = "";
	addRowCnt1++;
	
	if($("#codeTr tr").find("td").attr("colSpan")== 7){
		$("#codeTr").empty();	
	}
	
	realDataCnt = $("#parCodeTr tr").size();
	SecRealDataCnt = $("#codeTr tr").size();
	
	rowHtml = '<tr>';
	rowHtml += '<td align="center" ><input type="checkbox" checked name="chk1"/></td>';
	rowHtml += '<td align="center" >'+(SecRealDataCnt+1)+'</td>';
	rowHtml += '<td align="center" ><input type="text" name="code" value="'+parCodeVal+'" readonly style="border:0px;text-align:center;"/></td>';
	rowHtml += '<td align="center" ><input type="text" class="form-control" name="subCode"  onkeyup="javascript:fn_setCheckRow(this);" /></td>';
	rowHtml += '<td align="center" ><input type="text" class="form-control" name="codeSubName"  onkeyup="javascript:fn_setCheckRow(this);"/></td>';
	rowHtml += '<td align="center" ><input type="text" class="form-control" name="order" onkeyup="javascript:fn_setCheckRow(this);" onkeydown="javascript:fn_KeyDownOnlyNumber();" onkeypress="javascript:fn_KeyPressOnlyNumber();set_comma(this);" style="text-align:right;"/></td>';
	rowHtml += '<td align="center" >';
	rowHtml += '	<select class="form-control" id="level" name="subUseYn" onchange="javascript:fn_setCheckRow(this);">';
	rowHtml += '		<option value="Y">Y</option>';
	rowHtml += '	    <option value="N">N</option>';
	rowHtml += '	</select>';
	rowHtml += '</td>';
	rowHtml += '</tr>';
	
	$("#codeTr").append(rowHtml);
}


/*--------------------------------------*/
/* 소분류 삭제 버튼 클릭 이벤트					*/
/*--------------------------------------*/
function fn_del(){
	var chkCnt = 0;
	var delFlag = false;
	
	$("input[name=chk1]").each(function(){
		if(this.checked){
			chkCnt++;
		}
	});
	
	if(chkCnt == 0){
		alert("삭제할 행을 선택해 주세요.");
		return;
	}
	//var saveArray = new Array();
	//if(confirm("삭제하시면 데이터를 잃을 수 있습니다. 정말로 삭제하시겠습니까?")){
		//alert("삭제로직");
		$("input[name=chk1]").each(function(){
			if(this.checked){
// 				if(this.value != undefined && this.value != "on"){
// 					saveArray.push(this.value);
// 				}else{
// 					$(this).parent().parent().remove();	
// 				}	
				if(this.value != undefined && this.value != ""&& this.value != "on"){
					if(delArray != ""){
						for(var i=0;i<delArray.length;i++){
							if(delArray[i] == this.value){
								delFlag = true;
							}
						}
					}
					
					if(!delFlag) delArray.push({code: parCodeVal , subCode : this.value ,codeName : "", codeSubName : $(this).parent().parent().find("input[name=codeSubName]").val() , order : $(this).parent().parent().find("input[name=order]").val() ,useYn : "N" , flag : "sub", originCode: this.value});
					$(this).parent().parent().remove();	
				}else{
					$(this).parent().parent().remove();	
				}	
			}
		});
		//alert(saveArray);
// 		if(saveArray != ""){
// 			alert("아작스 통신");
// 		}
	//}
}

/*--------------------------------------*/
/* 대분류 keyup 이벤트					*/
/*--------------------------------------*/
function fn_setCheck(obj){
	var rowIndex = $(obj).parent().parent().parent().children().index($(obj).parent().parent());
	
	$("input[name=chk]").eq(rowIndex).prop("checked",true);
}

/*--------------------------------------*/
/* 소분류 keyup 이벤트					*/
/*--------------------------------------*/
function fn_setCheckRow(obj){
	var rowIndex = $(obj).parent().parent().parent().children().index($(obj).parent().parent());
	
	$("input[name=chk1]").eq(rowIndex).prop("checked",true);
}

/* *****************************************
 * 유효성 체크 함수
/* ***************************************** */
function fn_checkValid(){
	$('#frm').validate({
		ignore: [],
        rules: {
        	 "codeName[]": { required: true , maxlength:"20"}
	       , "subCode[]": { required: true , maxlength:"10"}
	       , "codeSubName[]": { required: true , maxlength:"20"}
	       , "order[]": { required: true, maxlength:"10" }
	    },
        messages: {
        	"codeName[]": { maxlength:"20자 이내로 입력해주세요."}
	          , "subCode[]": { maxlength:"10자 이내로 입력해주세요."}
	          , "codeSubName[]": { maxlength:"20자 이내로 입력해주세요."}
	          , "order[]" : { maxlength:"10자 이내로 입력해주세요."}
           }
    });
}
</script>

<div class="container-fluid">
	<div class="animated fadeIn">
	    <!--검색-->
		<div class="row">
			<div class="col-lg-12">
				<div class="card">
                    <div class="card-header">
                    	<i class="fas fa-edit"></i>
						<strong>공통코드 관리</strong>
					</div>
					<!-- .BODY -->
					<div class="card-body" id="colorPickerBlock">
						<h:form id="searchForm" class="form-horizontal" object="dataInfo">
							<input type="hidden" id="page" name="page" value="${dataInfo.page}" />
							<input type="hidden" id="totCnt" value="${list[0].totCnt}" />
						    <div class="form-group row">
						        <label for="name" class="col-lg-1 col-form-label text-right" >코드명 : </label>
						        <div class="col-lg-2">
									<input type="text" class="form-control" id="name" name="name" value="${dataInfo.name}" autofocus="autofocus" />
								</div>
								<label for="useYn" class="col-lg-1 col-form-label text-right" >사용여부 : </label>
								<div class="col-lg-2">
									<select class="form-control" id="useYn" name="useYn">
										<option value="all">전체</option>
						        		<option value="Y" <c:if test="${empty dataInfo.useYn or dataInfo.useYn == 'Y'}">selected</c:if>>Y</option>
									    <option value="N" <c:if test="${dataInfo.useYn == 'N'}">selected</c:if>>N</option>
									</select>
								</div>								
						        <div class="col-lg-1">
									<button id="searchBtn" class="btn btn-danger" type="button" >조회</button>
								</div>
							</div>
						</h:form>
					</div>
					<!-- /.BODY -->
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-lg-12">
				<form name="frm" id="frm">
					<div class="card">
	                    <div class="card-header">
							<i class="fas fa-table"></i>
							<strong>목록</strong>						
						</div>
						<div id="infoCollapse" class="card-body">
							<div class="row">
								<div class="text-left sidebar-right-opened"  style="width:100%;margin-bottom:10px;color:red;">
								      &nbsp;&nbsp;&nbsp; ※ 사용중인 코드를 사용하지 않고 싶으실땐, 소분류 사용여부를 'N'으로 저장 후, 동일 상위코드번호에 대해 새로운 코드를 생성하십시오.
								</div>
								
								<div class="col-lg-5">
									<div class="text-right sidebar-right-opened" >
										<a class="btn btn-primary btn-flat" href="javascript:fn_addRow();">행추가</a>
										<a class="btn btn-primary btn-flat btn-danger" href="javascript:fn_delRow();">행삭제</a>
									</div><br/>
									
									<table id="dataTable" class="table table-sm table-bordered table-hover table-condensed">
								    	<thead>
								    		<tr class="success">
								    			<th class="text-center"><input type="checkbox" id="chkAll"/></th>
								    			<th class="text-center">번호</th>
								    			<th class="text-center">코드명</th>
								    			<th class="text-center">사용여부</th>
								    		</tr>
								    	</thead>
								    	<tbody id="parCodeTr">
								    	</tbody>
								    </table>
								    <div id="pageList">					    
			 						</div>
		 						</div>
		 						
		 						<div class="col-lg-7"> 
								<div class="text-right sidebar-right-opened" >
									<a class="btn btn-primary btn-flat"  href="javascript:fn_add();">행추가</a>
									<a class="btn btn-primary btn-flat btn-danger" href="javascript:fn_del();">행삭제</a>
								</div><br/>
								
								<table id="dataTable" class="table table-sm table-bordered table-hover table-condensed">
							    	<thead>
							    		<tr class="success">
							    			<th class="text-center"><input type="checkbox" id="chkSubAll"/></th>
							    			<th class="text-center">번호</th>
							    			<th class="text-center">상위코드번호</th>
							    			<th class="text-center">코드</th>
							    			<th class="text-center">코드명</th>
							    			<th class="text-center">정렬순서</th>
							    			<th class="text-center">사용여부</th>
							    		</tr>
							    	</thead>
							    	<tbody id="codeTr">
							    	</tbody>
							    </table>
								</div>
							</div>
						</div>
						<!--12-->
						<div class="card-footer text-right sidebar-right-opened">
						<a class="btn btn-primary btn-flat" href="javascript:fn_save();">저장</a>
						</div>
					</div>
				</form>
			</div>	
		</div>	
	</div>
</div>

<script type='text/javascript'>
realDataCnt = $("#parCodeTr tr").size();
SecRealDataCnt = $("#codeTr tr").size();
</script>
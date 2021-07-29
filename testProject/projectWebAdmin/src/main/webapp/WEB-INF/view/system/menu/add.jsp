<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
th{
	background-color:#f0f0f0;
}
</style>
<script>
var headMenuNo;
var middleMenuNo;
var headIndex;
var middleIndex;
var saveArray = new Array();
var delArray = new Array();
var headFlag = true;
$(document).ready(function(){
	f_ajax("<%=request.getContextPath()%>/system/menu/setHeadList.json", '' ,fn_headSet);
	
	$(document).on("keydown", ':text', function(){
		$(this).parent().parent().find("#chkUnit").attr("checked", true);
	}); 
	
	$(document).on("change", ':input', function(){
		$(this).parent().parent().find("#chkUnit").attr("checked", true);
	});
	
	
	//allchk 설정
    $("#headAll, #middleAll, #lastAll").click(function() {
    	var allChk = $(this).attr("id").replace("All","")
    	
    	if($(this).is(":checked")){
    		$('#'+allChk + 'Tr').find('[name=chkUnit]').prop('checked',true);
    	}else{
    		$('#'+allChk + 'Tr').find('[name=chkUnit]').prop('checked',false);
    	}
    });
	
	
});

function fn_middleList(prtMenuNo, obj){
	$(obj).parent().parent().find("tr").attr("bgcolor", "");	
	$(obj).parent().attr("bgcolor","#f0f0f0");
	headMenuNo = prtMenuNo;
	
	if(prtMenuNo != ''){
		var data = {"menuNo" : prtMenuNo };
		f_ajax("<%=request.getContextPath()%>/system/menu/setMiddleList.json", data ,fn_middleSet);
	}else{
		middleIndex = 0;
		$("#middleTr").empty();
		$("#lastTr").empty();
	}
}
function fn_lastList(prtMenuNo, obj){
	$(obj).parent().parent().find("tr").attr("bgcolor", "");	
	$(obj).parent().attr("bgcolor","#f0f0f0");
	$(obj).parent().parent().find("#selected").val("");
	$(obj).parent().find("#selected").val("selected");
	middleMenuNo = prtMenuNo;
	
	if(prtMenuNo != ''){
		var data = {"menuNo" : prtMenuNo };
		f_ajax("<%=request.getContextPath()%>/system/menu/setLastList.json", data ,fn_lastSet);
	}else{
		$("#lastTr").empty();
	}
	
}

function fn_headSet(data){
	
	var html = "";
	var prtMenuNo = "";
	var bgcolor = "";
	headIndex = data.list.length;
	
	$.each(data.list, function(index, entry){
		
		if(index == 0){
			prtMenuNo = entry.menuNo;
			bgcolor = "#f0f0f0";
		}else{
			bgcolor = "";
		}
		
		html += '<tr style="cursor:pointer;" bgcolor="'+ bgcolor +'">';
		html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="'+ entry.menuNo +'"/></td>';
		html += '<td align="center" onclick="fn_middleList(\''+ entry.menuNo +'\', this)">'+ (index + 1) +'</td>'
		html += '<td align="center"><input type="text" id="menuNm" class="form-control" value="'+ entry.menuNm +'"/></td>';
		html += '<td align="center"><input type="text" id="menuOdr" class="form-control" maxlength="2" value="'+ entry.menuOdr +'"/></td>';
		html += '<td align="center">';
			html += '<select class="form-control" id="useYn">';
				var selectY = "";
		   		var selectN = "";
		   		
		   		if(entry.useYn == "Y") selectY = "selected";
		   		else selectY = "";
		   		
		   		if(entry.useYn == "N") selectN = "selected";
		   		else selectN = "";
	   		
	   			html += '<option value="Y" '+selectY+'>Y</option>';
	   			html += '<option value="N" '+selectN+'>N</option>';
			html += '</select>';
		html += '</td>';
		html += '</tr>';
	});
	$("#headTr").append(html);
	var middlePrtData = {"menuNo" : prtMenuNo};
	headMenuNo = prtMenuNo;
	f_ajax("<%=request.getContextPath()%>/system/menu/setLastList.json", middlePrtData ,fn_middleSet);
}
function fn_middleSet(data){
	$("#middleTr").empty();
	
	var html = "";
	var prtMenuNo = "";
	var bgcolor = "";
	middleIndex = data.list.length;
	
	$.each(data.list, function(index, entry){
		
		if(index == 0){
			prtMenuNo = entry.menuNo;
			bgcolor = "#f0f0f0";
		}else{
			bgcolor = "";
		}
		
		html += '<tr style="cursor:pointer;" bgcolor="'+ bgcolor +'">';
		html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="'+ entry.menuNo +'"/></td>';
		html += '<td align="center" onclick="fn_lastList(\''+ entry.menuNo +'\', this)">'+ (index + 1) +'</td>'
		html += '<td align="center"><input type="text" id="menuNm" class="form-control" value="'+ entry.menuNm +'"/></td>';
		html += '<td align="center"><input type="text" id="menuOdr" class="form-control" maxlength="2" value="'+ entry.menuOdr +'"/></td>';
		html += '<td align="center">';
			html += '<select class="form-control" id="useYn">';
				var selectY = "";
		   		var selectN = "";
		   		
		   		if(entry.useYn == "Y") selectY = "selected";
		   		else selectY = "";
		   		
		   		if(entry.useYn == "N") selectN = "selected";
		   		else selectN = "";
	   		
	   			html += '<option value="Y" '+selectY+'>Y</option>';
	   			html += '<option value="N" '+selectN+'>N</option>';
			html += '</select>';
		html += '</td>';
		html += '</tr>';
	});
	$("#middleTr").append(html);
	
	var lastPrtData = {"menuNo" : prtMenuNo};
	middleMenuNo = prtMenuNo;
	f_ajax("<%=request.getContextPath()%>/system/menu/setLastList.json", lastPrtData ,fn_lastSet);
}

function fn_lastSet(data){
	$("#lastTr").empty();
	
	var html = "";
	$.each(data.list, function(index, entry){
		
		html += '<tr style="cursor:pointer;">';
		html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="'+ entry.menuNo + '"/></td>';
		html += '<td align="center"><input type="text" id="menuNm" class="form-control" value="' + entry.menuNm + '"/></td>';
		html += '<td align="center"><input type="text" id="linkAddr" class="form-control" value="' + entry.linkAddr + '"/></td>';
		html += '<td align="center"><input type="text" id="menuOdr" class="form-control" maxlength="2" value="' + entry.menuOdr + '"/></td>';
		html += '<td align="center">';
			html += '<select class="form-control" id="useYn">';
				var selectY = "";
		   		var selectN = "";
		   		
		   		if(entry.useYn == "Y") selectY = "selected";
		   		else selectY = "";
		   		
		   		if(entry.useYn == "N") selectN = "selected";
		   		else selectN = "";
	   		
	   			html += '<option value="Y" '+selectY+'>Y</option>';
	   			html += '<option value="N" '+selectN+'>N</option>';
			html += '</select>';
		html += '</td>';
		html += '</tr>';
	});
	$("#lastTr").append(html);
	
}
function fn_addHead(){
	if(!headFlag){
		alert("대메뉴는 한번에 하나씩만 추가 가능합니다.");
		return false;
	}
	var html = "";
	headIndex += 1;
	
	html += '<tr style="cursor:pointer;">';
	html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="headAdd" checked/></td>';
	html += '<td align="center" onclick="fn_middleList(\'\', this)">'+ headIndex +'</td>'
	html += '<td align="center"><input type="text" id="menuNm" class="form-control" /></td>';
	html += '<td align="center"><input type="text" id="menuOdr" maxlength="2" class="form-control"/></td>';
	html += '<td align="center">';
		html += '<select class="form-control" id="useYn">';
   			html += '<option value="Y">Y</option>';
   			html += '<option value="N">N</option>';
		html += '</select>';
	html += '</td>';
	html += '</tr>';
	
	$("#headTr").append(html);
	headFlag = false;
}

function fn_addMiddle(){
	var html = "";
	middleIndex += 1;
	
	html += '<tr style="cursor:pointer;">';
	html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="middleAdd" checked/></td>';
	html += '<td align="center" onclick="fn_lastList(\'\', this)">'+ middleIndex +'</td>'
	html += '<td align="center"><input type="text" id="menuNm" class="form-control" /></td>';
	html += '<td align="center"><input type="text" id="menuOdr" maxlength="2" class="form-control"/></td>';
	html += '<td align="center">';
		html += '<select class="form-control" id="useYn">';
   			html += '<option value="Y">Y</option>';
   			html += '<option value="N">N</option>';
		html += '</select>';
	html += '</td>';
	html += '<td style="display:none;"><input id="selected" type="hidden" value=""></td>'
	html += '</tr>';
	
	$("#middleTr").append(html);
	if(middleIndex == 1){
		$("#middleTr tr td:nth-child(2)").click();
	}
}

function fn_addLast(){
	if(middleIndex == 0){
		alert("중메뉴를 먼저 추가 해주세요.");
		return false;
	}
	var html = "";
	
	html += '<tr style="cursor:pointer;">';
	html += '<td align="center"><input type="checkbox" id="chkUnit" name="chkUnit" value="lastAdd" checked/></td>';
	html += '<td align="center"><input type="text" id="menuNm" class="form-control" /></td>';
	html += '<td align="center"><input type="text" id="linkAddr" class="form-control" /></td>';
	html += '<td align="center"><input type="text" id="menuOdr" maxlength="2" class="form-control"/></td>';
	html += '<td align="center">';
		html += '<select class="form-control" id="useYn">';
   			html += '<option value="Y">Y</option>';
   			html += '<option value="N">N</option>';
		html += '</select>';
	html += '</td>';
	html += '</tr>';
	
	$("#lastTr").append(html);
}

function fn_save(){
	var flag = true;
	if($("[name=chkUnit]:checked").val() == undefined && delArray == ""){
		alert("선택된 메뉴가 없습니다.");
		return false;
	}
	$.merge(saveArray, delArray);
	
	
	
	var headTr = $("#headTr").find("[name=chkUnit]:checked");
	var middleTr = $("#middleTr").find("[name=chkUnit]:checked");
	var lastTr = $("#lastTr").find("[name=chkUnit]:checked");
	var parentChildren;
	
	headTr.each(function(i){
		parentChildren = $(headTr[i]).parent().parent()
		if(parentChildren.find("#menuNm").val() == "" || parentChildren.find("#menuOdr").val() == ""){
			flag = false;
    		return false;
    	}
		saveArray.push({
			    menuNo : $(headTr[i]).val()
			  , menuNm : parentChildren.find("#menuNm").val()
			  , menuOdr : parentChildren.find("#menuOdr").val()
			  , linkAddr : ""
			  , useYn : parentChildren.find("#useYn").val()
			  , prtMenuNo : ""
		})
	});
	middleTr.each(function(i){
		parentChildren = $(middleTr[i]).parent().parent()
		if(parentChildren.find("#menuNm").val() == "" || parentChildren.find("#menuOdr").val() == ""){
    		flag = false;
    		return false;
    	}
		saveArray.push({
			    menuNo : $(middleTr[i]).val()
			  , menuNm : parentChildren.find("#menuNm").val()
			  , menuOdr : parentChildren.find("#menuOdr").val()
			  , linkAddr : ""
			  , useYn : parentChildren.find("#useYn").val()
			  , prtMenuNo : headMenuNo
			  , gubun : parentChildren.find("#selected").val()
		})
	});
	lastTr.each(function(i){
		parentChildren = $(lastTr[i]).parent().parent()
		if(parentChildren.find("#menuNm").val() == "" || parentChildren.find("#menuOdr").val() == "" || parentChildren.find("#linkAddr").val() == ""){
    		flag = false;
    		return false;
    	}
		saveArray.push({
			    menuNo : $(lastTr[i]).val()
			  , menuNm : parentChildren.find("#menuNm").val()
			  , menuOdr : parentChildren.find("#menuOdr").val()
			  , linkAddr : parentChildren.find("#linkAddr").val()
			  , useYn : parentChildren.find("#useYn").val()
			  , prtMenuNo : middleMenuNo
		})
	});
	console.log(saveArray);
	
	if(!flag){
		alert("입력되지 않은 값이 있습니다.");
		saveArray = [];
		return false;
	}
	f_ajax("<%=request.getContextPath()%>/system/menu/addProc.json", saveArray, fn_result);	
}

function fn_result(data){
	if(data.resultCode=="0000"){
		alert("저장 되었습니다.");
		location.href='<%=request.getContextPath()%>/system/menu/add.html';
	}else{
		delArray = [];
		saveArray= []; 
	}
}
function fn_del(type){
	var tr = $("#" + type + "Tr").find("[name=chkUnit]:checked");
	
	if(!confirm("삭제시 하위메뉴도 같이 삭제 됩니다.\n삭제하시겠습니까?")) return false;
	
	tr.each(function(i){
		if($(tr[i]).val().match("M")){
			alert("지울 수 없는 메뉴가 있습니다.");
			return false;
		}
		else if(!$(tr[i]).val().match("Add")){
			delArray.push({
				menuNo : $(tr[i]).val()
			  , gubun : type + "Del"
			})
		} 
		$(tr[i]).parent().parent().remove()
		
	});
	
	
	
}

</script>
<div class="container-fluid">
	<div class="animated fadeIn">
		<div class="row">
			<div class="col-lg-12">
				<div class="card">
                    <div class="card-header">
                       <i class="fas fa-edit"></i>
					   <strong>목록</strong>						
					</div>
					<div id="infoCollapse" class="card-body">
						<div class="row">
						<div style="width:28%; margin:0px 15px;">
							<div class="text-right sidebar-right-opened">
								<a class="btn btn-primary btn-flat" href="javascript:fn_addHead();">행추가</a>
								<a class="btn btn-primary btn-flat btn-danger" href="javascript:fn_del('head');">행삭제</a>
							</div><br/>
							
							<table id="dataTable" class="table table-sm table-bordered table-hover table-condensed">
						    	<thead>
						    		<tr class="success">
						    			<th class="text-center"><input type="checkbox" id="headAll"/></th>
	 					    			<th class="text-center">번호</th> 
	 					    			<th class="text-center">메뉴명</th> 
						    			<th style="width:15%;" class="text-center">순서</th>
						    			<th class="text-center">사용여부</th>
						    		</tr>
						    	</thead>
						    	<tbody id="headTr">
						    	</tbody>
						    </table>
	 						</div>
	 						
	 						<div style="width:28%; margin:0px 15px;">
							<div class="text-right sidebar-right-opened">
								<a class="btn btn-primary btn-flat" href="javascript:fn_addMiddle();">행추가</a>
								<a class="btn btn-primary btn-flat btn-danger" href="javascript:fn_del('middle');">행삭제</a>
							</div><br/>
							
							<table id="dataTable" class="table table-sm table-bordered table-hover table-condensed">
						    	<thead>
						    		<tr class="success">
						    			<th class="text-center"><input type="checkbox" id="middleAll"/></th>
	 					    			<th class="text-center">번호</th> 
	 					    			<th class="text-center">메뉴명</th> 
						    			<th style="width:15%;" class="text-center">순서</th>
						    			<th class="text-center">사용여부</th>
						    		</tr>
						    	</thead>
						    	<tbody id="middleTr">
						    	</tbody>
						    </table>
						    </div>
						
							<div style="width:38%; margin:0px 15px;">
							<div class="text-right sidebar-right-opened">
								<a class="btn btn-primary btn-flat"  href="javascript:fn_addLast();">행추가</a>
								<a class="btn btn-primary btn-flat btn-danger" href="javascript:fn_del('last');">행삭제</a>
							</div><br/>
							
							<table id="dataTable" class="table table-sm table-bordered table-hover table-condensed">
						    	<thead>
						    		<tr class="success">
						    			<th class="text-center"><input type="checkbox" id="lastAll"/></th>
						    			<th class="text-center">메뉴명</th>
						    			<th style="width:40%;" class="text-center">연결주소</th>
						    			<th style="width:15%;" class="text-center">순서</th>
						    			<th class="text-center">사용여부</th>
						    		</tr>
						    	</thead>
						    	<tbody id="lastTr">
						    	</tbody>
						    </table>
						    </div>
						</div>
					</div>
					<div class="card-footer text-right sidebar-right-opened">
							<a class="btn btn-primary btn-flat" href="javascript:fn_save();">저장</a>
					</div>
				</div>
			<!--12-->
			</div>
		</div>	
	</div>	
</div>

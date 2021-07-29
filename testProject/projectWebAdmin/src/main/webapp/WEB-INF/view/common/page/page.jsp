<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="page-selection"></div>

<script type="text/javascript">
	$(document).ready(function(){
		var formId = "${param.formId}";
		
		var totPage = Math.ceil(parseInt("${param.totCnt}") / parseInt("${param.cntPerPage}"));
		if(isNaN(totPage)) totPage = 0;
		
		$('#page-selection').bootpag({
			total: totPage, /* 총 페이지 수 */
		   	page: parseInt("${param.page}"), /* 현재 페이지 */
		   	maxVisible: 10, /* 하단에 보여주는 페이지버튼 수 / 페이지 당 보여주는 리스트 수 아님 */
		   	leaps: true
		}).on("page", function(event, /* page number here */ num){
			$('#page').val(num);
			
			 if(formId != null && formId != ''){
				$("#"+formId).submit();
			}else{
	        	$("form").submit();
			} 
		});
		
        $('#searchBtn').click(function(){
        	$('#page').val(1);       	
        });
        
        $('#page-selection ul').addClass('justify-content-center');
        $('#page-selection li').addClass('page-item');
        $('#page-selection a').addClass('page-link');
	});
</script>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>

<li class="nav-divider"></li>

<script type="text/javascript">
$(function(){
	var _menuList = ${su:json(user.menuInfos)};
	_composeMenu('${currTopMenuNo}', null, 0);
	var currMenuNo = '${currMenuNo}';
	var li = $("#menu li."+currMenuNo+"");
	if(li.data()){
		var pn = li.data().prtMenuNo;
		if(pn){
			$("#menu li."+pn).addClass('open');
		}
	}
	
	$(".nav-dropdown-toggle").on("click",function(){
		if($(this).parent().hasClass("open")){
			$(this).parent().removeClass("open");
		}else{
			$(".nav-dropdown").removeClass("open");
			$(this).parent().addClass("open");
		}
	});
	
	h = li.find("a").attr('href');
	$("#mainArea").attr("src", h);

	function _composeMenu(mno, pe, level){
		$.each(_menuList, function(){
			if(this.prtMenuNo == mno){
				var link = "javascript:;";

				if(this.linkAddr){
					link = "<%=request.getContextPath() %>/"+this.linkAddr+"?menuNo="+this.menuNo;
				}

				var m = null;
				if(level == 0){
					if(this.childCount == 0){
						m = $('<li class="_menus_ nav-item">'
							   + '<a class="nav-link" href="'+link+'" target="mainArea">'
							   + '<i class="fa fa-angle-right"></i>&nbsp; '+this.menuNm+' </a> </li>');
					}
					else{
						m = $('<li class="_menus_ nav-dropdown">'
						       + '<a class="nav-link nav-dropdown-toggle" href="'+link+'" target="mainArea" >'
						       + '<i class="fas fa-list-ul"></i>&nbsp;' + this.menuNm +'</a><ul class="nav-dropdown-items"></ul></li>');
					}

					if($("li._menus_").length == 0)
						$("li.nav-divider").after(m);
					else
						$("li._menus_:last").after(m);
				}
				else{
					m = $('<li class="nav-item"> <a class="nav-link" href="'+link+'" target="mainArea"> <i class="fa fa-angle-right"></i>&nbsp; '+this.menuNm+' </a> </li>');
					pe.append(m);
				}

				m.addClass(this.menuNo);
				m.data(this);
				_composeMenu(this.menuNo, m.find("ul"), level+1);
			}
		});
	}
});
</script>
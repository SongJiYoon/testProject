<%@page import="java.util.UUID"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String rid = UUID.randomUUID().toString().replaceAll("-", "");
request.setAttribute("rid", rid);
%>

<img id="${rid}_preview" src="" width="100px" />
<input type="hidden" name="${param.name }" id="${rid}_fildOid" class="plfile" />
<a id="${rid}_pickfiles" href="javascript:;" class="btn btn-primary btn-sm">파일업로드</a>

<script type="text/javascript">

var plfile_${rid} = ${f:nvl(param.file, "null")};

$(function(){
	var pl_fileOids = [];
	if(plfile_${rid}){
		pl_fileOids.push(plfile_${rid}.fileOid);
	}

	f_ajax("<%=request.getContextPath()%>/plfiles.json", pl_fileOids, function(resp){
		$.each(resp.resultData.data, function(){
			$("#${rid}_preview").attr("src", this.url);
			$("#${rid}_fildOid").val(this.fileOid);
			$("#${rid}_fildOid").data(this);
		});
	});
});

var uploader_conf = {
	runtimes : 'html5,flash,silverlight,html4',

	multi_selection : false,

    browse_button : '${rid}_pickfiles', // you can pass in id...
//    container: document.getElementById('container'), // ... or DOM Element itself
     
    url : "<%=request.getContextPath()%>/plupload.json",
     
    filters : {
        max_file_size : '10mb',
        mime_types: [
            {title : "Image files", extensions : "jpg,gif,png,jpeg"},
            {title : "Zip files", extensions : "zip"}
        ]
    },
 
    // Flash settings
    flash_swf_url : '/plupload/js/Moxie.swf',
 
    // Silverlight settings
    silverlight_xap_url : '/plupload/js/Moxie.xap',
     
 
    init: {
        PostInit: function() {
        },

        FilesAdded: function(up, files) {
            up.start();
        },

		FileUploaded: function(up, file, info) {
			resp = JSON.parse(info.response);
			cmsfile = resp.resultData.data;
			console.log(cmsfile);
			$("#${rid}_preview").attr("src", cmsfile.url);
			$("#${rid}_fildOid").val(cmsfile.fileOid);
			$("#${rid}_fildOid").data(cmsfile);
		},
 
        UploadProgress: function(up, file) {
            //document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
        },

		UploadComplete: function(up, files){
		},
 
        Error: function(up, err) {
            //document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
        }
    }
};

new plupload.Uploader(uploader_conf).init();
</script>
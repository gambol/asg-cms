<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>

  <script type="text/javascript"
   src="/js/swfupload.js">
</script>
  <script type="text/javascript"
   src="/js/handlers.js">
</script>
  <script type="text/javascript">
  
var swfu;
window.onload = function() {
 swfu = new SWFUpload(
   {
    upload_url : "/image/uploadImage.htm",
    post_params: {"photoName" : document.getElementById("photoName").value},
    // File Upload Settings
    file_size_limit : "100 MB", // 1000MB
    file_types : "*.jpg;*.jpeg;*.bmp;*.gif",
    file_types_description : "图片格式",
    file_upload_limit : "0",
              
    file_queue_error_handler : fileQueueError,
    file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
    file_queued_handler : fileQueued,
    upload_progress_handler : uploadProgress,
    upload_error_handler : uploadError,
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    // Button Settings
    button_image_url : "/gduser/resources/swfupload/images/SmallSpyGlassWithTransperancy_17x18.png",
    button_placeholder_id : "spanButtonPlaceholder",
    button_width : 180,
    button_height : 18,
    button_text : '<span class="button">选择图片 <span class="buttonSmall">(100 MB Max)</span></span>',
    button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
    button_text_top_padding : 0,
    button_text_left_padding : 18,
    button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
    button_cursor : SWFUpload.CURSOR.HAND,
    // Flash Settings
    flash_url : "/flash/swfupload.swf",
    custom_settings : {
     upload_target : "divFileProgressContainer"
    },
    // Debug Settings
    debug : false
   //是否显示调试窗口
   });
};
function startUploadFile() {
  swfu.startUpload();
}
</script>
 </head>
 <body>
  <div id="scrollContent">

   <div class="box2" panelWidth="700" panelHeight="400"
    panelTitle="照片上传" roller="true">
    <form id="myForm" action="/user/uploadImage.htm"  method="post">
    <input type="hidden" value="${photoName}" id="photoName" name="photoName" />
     <div class="box1" panelWidth="650" panelHeight="330" roller="true">
      <table>
       <tr>
        <td>
         <span id="spanButtonPlaceholder"></span>
        </td>
        <td>
         <input id="btnUpload" type="button" value="上  传"
          onclick="startUploadFile();" class="btn3_mouseout"
          onMouseUp="this.className='btn3_mouseup'"
          onmousedown="this.className='btn3_mousedown'"
          onMouseOver="this.className='btn3_mouseover'"
          onmouseout="this.className='btn3_mouseout'" />
        </td>
        <td>
         <input id="btnCancel" type="button" value="取消所有上传"
          onclick="cancelUpload();" disabled="disabled"
          class="btn3_mouseout"
          onMouseUp="this.className='btn3_mouseup'"
          onmousedown="this.className='btn3_mousedown'"
          onMouseOver="this.className='btn3_mouseover'"
          onmouseout="this.className='btn3_mouseout'" />
        </td>
       </tr>
      </table>
      <div id="divFileProgressContainer"></div>
      <div id="thumbnails">
       <table id="infoTable" border="0" width="530"
        style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px; margin-top: 8px;">
       </table>
      </div>
     </div>
    </form>
   </div>
  </div>
 </body>
</html>
 

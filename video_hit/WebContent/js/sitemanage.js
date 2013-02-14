$(document).ready(function(){
   
    $(".changeStatus").click(function(){
        var ajaxUrl = $(this).attr("aurl");
        var type = this.text;
        var changeStatusButton = this;
        $.get(ajaxUrl, {
            Action:"get"
        }, function (data){
           if (data.ret) {
               alert("操作成功");
               if (type == "隐藏") {
                   $(changeStatusButton).html("显示");
               } else {
                   $(changeStatusButton).html("隐藏");
               }
           } else {
               alert(data.errmsg);
           }
            
        });
    });
    
});
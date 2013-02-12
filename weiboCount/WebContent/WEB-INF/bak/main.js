

function changeCategory(categoryId){
    if(categoryId == 1){
        $("#rank-title-name").html("传奇私服排名");
        getRank(categoryId, 0, 50);
    }else if(categoryId == 2){
        $("#rank-title-name").html("魔兽世界私服排名");
        getRank(categoryId, 0, 50);
    }
    
}



function getRank(categoryId,start,size){
    
    
    
    
    $.ajax({
        url:"/rank/rankPage.htm",
        contentType : "application/json; charset=utf-8",
        data: "{\"categoryId\":\""+categoryId+"\",\"start\":\""+start+"\",\"size\":\""+size+"\"}",
        dateType: "json",
        type: "POST",
        success:function(response){       
            var json = eval(response);


            var htmlContent = "";
            var right_sb_height = 0;
            $.each(json.pageList,function(idx,item){
                var imgUrl = item.serverInfo.bannerUrl;
                if(imgUrl == null || imgUrl == ""){
                    imgUrl = "images/default.png";
                }
                right_sb_height += 186 +20;
                var nowRank = parseInt(start)+parseInt(idx)+1;
                var desc = item.serverInfo.description;
//                alert(desc.length > 60);
                if(desc.length > 70){
                    desc = desc.substring(0,70);
                    desc += "...";
                }
                var noClassIdx = "";
                if(idx <3){
                    noClassIdx = idx+1;
                }else{
                    noClassIdx = "other";
                }
                htmlContent +=
                '<div id="rank-content" class=" shadowed">'+
                '<div class="inner-boundary">' +
                '<div class="inner-border ">'+
                '<div class="rank-no-wrapper"><div class="rank-no rank-no-'+noClassIdx+'">'+nowRank+'</div></div>'+

                '<div class="rank-content-wrapper">' +
                '<a href="'+item.serverInfo.url+'" target="_blank">' +
                '<div class="rank-content-title">'+item.serverInfo.name+'</div>'+                                    
                '<img alt="'+item.serverInfo.name+'" src="'+imgUrl+'">'+
                '</a>'+

                '<div class ="rank-content-desc">'+
                desc+
                '</div>'+

                '</div>'+
                '<div class="rank-score">'+
                '<div class="score-desc">'+
                '网络线路：'+ item.serverInfo.line + '<br/>'+
//                '在线人数：100000<br/>论坛新帖：1020<br/>'+
                '<div class="score-desc-more">'+
                '<a href="'+item.serverInfo.url+'" target="_blank">[详情...]</a>'+
                '</div>'+                                                                     
                '</div>'+


                '<div class="sep"></div>'+
                '<div class="score-wrapper">'+
                '<span class="score-label">综合评分：</span><span class="score">9.1</span>'+
                ' </div>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '<div style="height: 20px"/>';
                
         
            })
            right_sb_height -=20;
             $("#rank-content-all").html(htmlContent);           
//            $("#right-side-bar .inner-border").height(right_sb_height+"px");
            var totalCount = json.totalCount;
            $("#totalCount").val(json.totalCount);
            
            var startIdx = 0;
            var rankPageHtmlContent = '<ul>';
            var end = parseInt(startIdx)+50;
            do{
                
                var startIdx1 = startIdx+1;
                var str = startIdx1 + '-' + end +'名';
                rankPageHtmlContent += '<li><a href="#" onclick="getRank('+categoryId+','+startIdx+',50);return false;">'+str+'</a></li>'
                startIdx +=50;
                end = parseInt(startIdx)+50;
            }while(startIdx < totalCount);
            rankPageHtmlContent += '</ul>';
            $("#rank-title-page .rank-page-dropdown").html(rankPageHtmlContent);
            
            
            
            var nowStartIdx = parseInt(start) +1;
            var nowEndIdx = parseInt(start) + 50;
            var nowTitlePageStr = nowStartIdx + '-' + nowEndIdx +'名';
            $("#rank-title-page .rank-page").html(nowTitlePageStr);
           
           
            $("#waitting-bar").hide();
            
        },
        beforeSend:function(){
//            alert("bb");
            $("#waitting-bar").show();           
        },
        error:function(){
//            alert("dd");
            $("#waitting-bar").hide();
                    
        }
    });
}


function reloadcmig_img_upload_div(divId,
                                   minWidth,
                                   minHeight,
                                   maxNum){
    var jqueryObj=$("#"+divId);
    jqueryObj.attr("data-minWidth",minWidth);
    jqueryObj.attr("data-minHeight",minHeight);
    jqueryObj.attr("data-maxNum",maxNum);
    var dataId=jqueryObj.attr("data-id");
    $("#"+dataId+"").val("");
    $("#"+dataId+"").attr("data-fastdfsImageServer","");
    $("#"+dataId+"").attr("data-thisOldAttachUrl","");

    jqueryObj.empty();
    jqueryObj.append('<input type="hidden" name="fastdfsImageServer" data-bind="value:model.fastdfsImageServer">');
    initcmig_img_upload_div(jqueryObj);
}

function initcmig_img_upload_div(jqueryObj){
    var dataId=jqueryObj.attr("data-id");
    var readonly=jqueryObj.attr("data-readonly");
    var minWidth=jqueryObj.attr("data-minWidth");
    var minHeight=jqueryObj.attr("data-minHeight");
    var showWidth=jqueryObj.attr("data-showWidth");
    var showHeight=jqueryObj.attr("data-showHeight");
    var maxNum=jqueryObj.attr("data-maxNum");
    var checkImageWidthHeightFlag=jqueryObj.attr("data-checkImageWidthHeightFlag");
    var maxSize=jqueryObj.attr("data-maxSize");
    var required=jqueryObj.attr("data-required");
    var callBakFun=jqueryObj.attr("data-callBakFun");
    var medioType=jqueryObj.attr("data-medioType");
    var params="dataId="+dataId
        +"&readonly="+readonly
        +"&minWidth="+minWidth
        +"&minHeight="+minHeight
        +"&showHeight="+showHeight
        +"&maxNum="+maxNum
        +"&checkImageWidthHeightFlag="+checkImageWidthHeightFlag
        +"&maxSize="+maxSize
        +"&required="+required
        +"&callBakFun="+callBakFun
        +"&medioType="+medioType;
    $.ajax({
        type:"GET",
        url:_basePath+"/module/base/image_upload_new.html",
        data: params,
        success:function(msg){
            jqueryObj.append(msg);
        },
        error:function(XMLHttpRequest, textStatus, thrownError){

        }
    });
}




$(document).ready(function(){


   $("div.cmig_img_upload_div").each(function(){
        initcmig_img_upload_div($(this));
    });


    /**
     * 新添加方法
     */
/*    (function($){
        $.extend({
            showPic:function(url,type,param){
                alert("lllllllll");
                //添加一个请求到队列中
                $("body").queue(function(){
                    alert("好像没有到这里来吗？？");
                    var data = "";
                    $.ajax({
                       // url: '${base.contextPath}/csp/ljh/app/config/function/get',
                        url:url,
                       // type : "GET",
                        type:type,
                        dataType : "json",
                        contentType : "application/json",
                        //data : {id:'${RequestParameters.id!0}'},
                        data:param,
                        success: function (args) {
                            var profile = args.dto||{};
                            data =  args.dto||{};
                            for(var i in profile){
                                if(i=="loginStatus"){
                                    for(var j = 0;j<viewModel.source.length;j++){
                                        if(profile[i]==viewModel.source[j].value){
                                            $("input[id='"+viewModel.source[j].value+"']").attr("checked",true);
                                        }
                                    }
                                }
                                viewModel.set("model."+i,profile[i]);
                            }
                        }
                    });
                    if(data!=null){
                        $("body").dequeue();
                        //如果为空就执行下一个ajax

                        $("body").queue(function(){
                            $("div.cmig_img_upload_div").each(function(){
                                initcmig_img_upload_div($(this));
                            })
                            $("body").dequeue();
                        })
                    }else{
                        $("div.cmig_img_upload_div").each(function(){
                            initcmig_img_upload_div($(this));
                        })
                    }
                });
            }
        })
    })(jQuery);*/
});
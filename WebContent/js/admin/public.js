function getHashString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}
function ajaxFileUpload(id,param) {
    $.ajaxFileUpload
    (
        {
            url: '/jos/item/uploadDetailImage.action', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: id, //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            data:param,
            success: function (data, status)  //服务器成功响应处理函数
            {

            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                alert(e);
            }
        }
    )
    return false;
}
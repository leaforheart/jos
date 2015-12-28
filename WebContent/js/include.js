/*正式代码中，请删掉下面这个JS文件*/
$(".include-page").each(function () {
    var _self = $(this);
    var url = _self.attr("data-src");
    var index = _self.length;
    $.ajax({
        url:url,
        success: function (data) {
            _self.get(0).outerHTML = data;
        }
    });
});

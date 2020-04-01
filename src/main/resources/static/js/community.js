/**
 * 展开二级评论
 * @param e
 */
function collapseComments(e){
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    comment.addClass("in");
}
function post() {
    //回复的问题编号
    var targetId = $("#question_id").val();
    //回复的问题内容
    var content = $("#comment_content").val();
    comment2target(targetId, 1, content);
}
function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        dataType:"json",
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        //请求成功的回调函数，查看response回来的json数据中的code值
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
            }else{
                if(response.code == 2003){
                    //如果code为2003，证明当前没有登录，需要进行登录，弹出是否进行登录的确认框
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=9786f3448892035cd7db&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }
                }else{
                    //如果是其他错误，弹出提示框，并将错误信息提示出来
                    alert(response.message);
                }
            }
        }
    });
}


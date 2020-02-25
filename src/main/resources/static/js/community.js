function post() {
    var targetId = $("#question_id").val();
    alert(targetId);
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
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
            }else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=9786f3448892035cd7db&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }
                }else{
                    alert(response.message);
                }
            }
        }
    });

}

/**
 * 展开二级评论
 * @param e
 */
function collapseComments(e){
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    //用于判断是折叠还是展开的状态
    var status = e.getAttribute("collapse_status");
    if (status){
        //如果这个状态不为空，证明已经展开了，需要折叠
        comment.removeClass("in");
        e.removeAttribute("collapse_status");
        e.classList.remove("active");
    }else {
        //如果没有这个状态，则证明现在是折叠状态，需要展开，此时先从数据库获取二级评论的数据
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1){
            comment.addClass("in");
            e.setAttribute("collapse_status", "in");
            e.classList.add("active");
        }else{
            $.getJSON( "/comment/" + id, function( data ) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {"class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": comment.gmtCreat
                    })));
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                })
            });
            comment.addClass("in");
            e.setAttribute("collapse_status", "in");
            e.classList.add("active");
        }
    }
}

/**
 * 进行二级评论
 */
function subComment(e) {
    var id = e.getAttribute("data-id");
    var content = $("#input-" + id).val();
    comment2target(id, 2, content);
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


function postComment() {
    // registerEventHandlers();
    $.ajax({
        url: "/commu/detail/comment",
        type: "POST",
        data: {
            commuCommentCommuId: document.getElementById("commuId").value,
            commuCommentCntnt: document.getElementById("cntnt").value
        },
        success: function (response) {

            // 답글 추가 후 원본 댓글의 답글 수 업데이트
            // updateReplyCount(formData.commuCommentRefId);
            // console.log("dsf")

        },
        error: function (err) {
            console.log(err);
            alert("댓글 작성 중 오류 발생");
        }
    });
}
function postReply(commentID) {
    $.ajax({
        url: "/commu/detail/reply",
        type: "POST",
        data: {
            commuCommentCommuId: document.getElementById("commuId").value,
            commuCommentCntnt: document.getElementById("commuCommentCntnt").value,
            commuCommentRefId: commentID
        },
        success: function (response) {

            // 답글 추가 후 원본 댓글의 답글 수 업데이트
            // updateReplyCount(formData.commuCommentRefId);
            // console.log("dsf")

        },
        error: function (err) {
            console.log(err);
            alert("댓글 작성 중 오류 발생");
        }
    });
}

function deleteComment(commentID,refID){
    $.ajax({
        url: "/commu/detail/delete",
        type: "POST",
        data: {
            commuCommentId: commentID,
            commuCommentRefId: refID
        },
        success: function (response) {

            // 답글 추가 후 원본 댓글의 답글 수 업데이트
            // updateReplyCount(formData.commuCommentRefId);
            // console.log("dsf")

        },
        error: function (err) {
            console.log(err);
            alert("댓글 작성 중 오류 발생");
        }
    });
}

$(document).ready(function () {
    // 페이지 로딩 시 댓글과 답글 관련 초기 설정
    initializeComments();

    // 이벤트 핸들러 등록
    registerEventHandlers();

    function initializeComments() {
        $(".replies").hide();
        updateAllReplyCounts();
        updateCommentCount();
    }

    function updateAllReplyCounts() {
        $(".single-comment").each(function () {
            var commuCommentId = $(this).data("id");
            updateReplyCount(commuCommentId);
        });
    }

    function updateCommentCount() {
        var comments = $(".single-comment").length;
        $(".comment-list-section h3 span").text(comments + '개');
    }

    function getTotalRepliesCount(commuCommentId) {
        var $comment = $(".single-comment[data-id='" + commuCommentId + "']");
        var directReplies = $comment.find(".replies .single-comment").length;

        var nestedReplies = 0;
        if (directReplies > 0) {
            $comment.find("> .replies > .single-comment").each(function () {
                nestedReplies += getTotalRepliesCount($(this).data('id'));
            });
        }

        return directReplies + nestedReplies;
    }

    function updateReplyCount(commuCommentId) {
        var totalReplies = getTotalRepliesCount(commuCommentId);
        $(".single-comment[data-id='" + commuCommentId + "']").find(".reply-count").text(totalReplies + '개');
        var $parentComment = $(".single-comment[data-id='" + commuCommentId + "']").closest('.replies').closest('.single-comment');
        if ($parentComment.length) {
            updateReplyCount($parentComment.data('id'));
        }
    }


    // 새로운 댓글/대댓글 HTML 생성
    function createCommentHTML(comment) {
        var replyButtonHTML = comment.commuCommentRefId ? '' : `<button class="view-replies-btn" style="display:none;">답글(<span class="reply-count">0개</span>)</button>`;

        return `
            <div class="single-comment" data-id="${comment.commuCommentId}">
                <strong>${comment.commuCommentMberId}</strong>
                <p>${comment.commuCommentCntnt}</p>
                <div class="comment-date">${comment.commuCommentRegistDate}</div>
                <div class="comment-buttons">
                    <button class="reply-to-comment">답글</button>
                    <button class="update-comment" data-id="${comment.commuCommentId}">수정</button>
                    <button class="delete-comment" data-id="${comment.commuCommentId}">삭제</button>
                    ${replyButtonHTML}
                    <div class="replyForm">
                        <form>
                            <input type="hidden" name="commuCommentRefId" value="${comment.commuCommentId}">
                            <textarea name="commuCommentCntnt" rows="2" placeholder="답글을 입력하세요."></textarea>
                            <button type="button" class="cancel-reply">취소</button>
                            <button type="button" class="post-reply">등록</button>
                        </form>
                    </div>
                <div class="replies"></div>
            </div>`;
    }


    // 초기 댓글 수 업데이트
    updateCommentCount();



    function registerEventHandlers() {
        $(".comment-list-section").on("click", ".view-replies-btn", function (e) {
            e.stopPropagation();
            var $repliesDiv = $(this).closest(".single-comment").find(".replies").first();
            $repliesDiv.toggle(); // 이것만으로는 모든 관련된 답글을 표시할 수 없습니다.

            // 추가: 원본 댓글에 관련된 모든 답글을 표시합니다.
            $repliesDiv.find(".replies").show();
        });
        $(".comment-list-section").on("click", ".cancel-reply", function (e) {
            e.stopPropagation(); // 이벤트 전파 중단
            var $replyForm = $(this).closest(".replyForm");
            $replyForm.css("display", "none");
        });
        $(".comment-list-section").on("click", ".single-comment", function (e) {
            e.stopPropagation();


            // 클릭한 댓글 또는 답글의 답글 폼만 보여줍니다.
            $(this).find(".replyForm").first().show();
        });


        $(".comment-list-section").on("click", ".reply-to-comment", function (e) {
            e.stopPropagation();
            // 모든 답글 폼을 숨깁니다.
            $(".replyForm").hide();
            // 클릭된 "답글" 버튼에 해당하는 답글 폼만 보여줍니다.
            $(this).closest(".single-comment").find(".replyForm").first().show();
        });
        // 댓글 등록
        $("#commentForm button").on("click", function (e) {
            e.preventDefault();
            var formData = {
                commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
                commuCommentCntnt: $("#commentForm").find("[name='commuCommentCntnt']").val()
            };

            postComment(formData, false, function () {
                $("#commentForm").find("[name='commuCommentCntnt']").val('');  // 댓글 폼 초기화
                updateCommentCount();
            });
        });
        // 답글 등록
        // $(".comment-list-section").on("click", ".post-reply", function () {
        //     var $replyForm = $(this).closest(".replyForm");
        //
        //     var formData = {
        //         commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
        //         commuCommentCntnt: $replyForm.find("[name='commuCommentCntnt']").val(),
        //         commuCommentRefId: $replyForm.find("[name='commuCommentRefId']").val()
        //     };
        //
        //     postComment(formData, true, function () {
        //         // 답글을 추가한 후 원본 댓글의 답글 수를 업데이트합니다.
        //         updateReplyCount(formData.commuCommentRefId);
        //
        //         // 전체 댓글 수를 업데이트합니다.
        //         updateCommentCount();
        //     });
        // });
        $(".comment-list-section").on("click", ".update-comment-textarea", function (e) {
            e.stopPropagation();  // textarea 클릭시 다른 요소는 클릭되지 않게 함
        });
        $(".comment-list-section").on("click", ".update-comment", function (e) {
            e.stopPropagation();  // 다른요소 못건들이게
            var $commentDiv = $(this).closest('.single-comment');
            if ($(this).text() === "수정") {
                var currentCommentText = $commentDiv.find('p').text();
                $commentDiv.find('p').hide(); // p 태그 숨기기 추가
                $commentDiv.find('.update-comment-textarea').val(currentCommentText).show(); // textarea 보여주기
                $(this).text("저장");
            } else {
                var updatedCommentText = $commentDiv.find('.update-comment-textarea').val();
                var commuCommentId = $commentDiv.data('id');

                $.ajax({
                    url: "/commu/comment/update/" + commuCommentId,
                    type: "POST",
                    data: JSON.stringify({
                        commuCommentId: commuCommentId,
                        commuCommentCntnt: updatedCommentText
                    }),
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function (response) {
                        $commentDiv.find('p').text(response.updatedComment).show(); // p 태그를 다시 보여주기
                        $commentDiv.find('.update-comment-textarea').hide(); // textarea 숨기기
                        $commentDiv.find('.comment-date').text(response.commuCommentUpdateDate);
                        $commentDiv.find('.update-comment').text("수정");
                    },
                    error: function (err) {
                        console.log(err);
                        alert("댓글 수정 중 오류 발생");
                    }
                });
            }
        });
        // 댓글 삭제
        // $(".comment-list-section").on("click", ".delete-comment", function () {
        //     var commuCommentId = $(this).data("id");
        //     /*var mainRefId = $(this).data("main-ref-id");*/
        //
        //     /*	var isOriginComment = commuCommentId === mainRefId; // 원본 댓글인 경우 확인*/
        //
        //     // 부모 댓글 ID를 초기화합니다.
        //     /*	var parentCommentId = $(this).closest('.single-comment').parent().closest('.single-comment').data('id');*/
        //
        //     var requestUrl = "/commu/comment/delete";
        //
        //     /*	if (!isOriginComment) { // 원본 댓글이 아닌 경우
        //             requestUrl += "?isSingleReply=true";
        //         } else {
        //             requestData.commuCommentMainRefId = mainRefId;
        //         }*/
        //
        //     $.ajax({
        //         url: requestUrl,
        //         type: "POST",
        //
        //         data: JSON.stringify(commuCommentId),// 데이터 전송
        //         contentType: 'application/json',
        //         dataType: 'json',
        //         success: function () {
        //             // 댓글을 삭제합니다.
        //             /*	$(".single-comment[data-id='" + commuCommentId + "']").remove();*/
        //
        //             // 만약 부모 댓글 ID가 존재하면, 해당 댓글의 답글 개수를 업데이트합니다.
        //             if (parentCommentId) {
        //                 updateReplyCount(parentCommentId);
        //             }
        //
        //             // 전체 댓글 개수를 업데이트합니다.
        //             updateCommentCount();
        //         },
        //         error: function (err) {
        //             console.log(err);
        //             alert("댓글 삭제 중 오류 발생");
        //         }
        //     });
        // });


    }
});

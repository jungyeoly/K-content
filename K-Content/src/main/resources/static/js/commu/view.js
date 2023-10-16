function maskUserId(userId) {
    if (userId.length <= 4) {
        return userId;
    }
    return userId.substring(0, 4) + '*'.repeat(userId.length - 4);
}

$(document).ready(function () {
    $('.single-comment strong').each(function () {
        var originalId = $(this).text();
        var maskedId = maskUserId(originalId);
        $(this).text(maskedId);
    });
    //페이지 로딩 시 댓글 페이지 들고오기
    $.ajax({
        url: "/commu/detail/comment",
        type: "GET",
        data: {commuId: document.getElementById("commuId").value},
        success: function (data) {
            let layout = $("#comment-Box");
            layout.find("#comment-Box").remove();
            layout.append(data);
        }
    });


});


//게시글 신고 기능
function showReportConfirmModal() {
    const modal = document.getElementById('commonModal');
    const modalTitle = modal.querySelector('.modal-title');
    const modalBody = modal.querySelector('.modal-body');

    modalTitle.textContent = "신고 확인";
    modalBody.textContent = "정말로 게시글을 신고하겠습니까?";

    // Bootstrap 5의 모달을 수동으로 표시하는 코드
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();

    // '확인' 버튼에 리스너 추가
    document.getElementById('confirmBtn').addEventListener('click', function () {
        const reportForm = document.querySelector('form[method="post"][th\\:action*="report"]');
        if (reportForm) {
            reportForm.submit();
        }
    });
}


// '확인' 버튼에 한 번만 이벤트 리스너 추가
document.getElementById('confirmBtn').addEventListener('click', function () {
    const deleteForm = document.querySelector('form[method="post"]');
    if (deleteForm) {
        deleteForm.submit();
    }
});

function showDeleteConfirmModal() {
    const modal = document.getElementById('commonModal');
    const modalTitle = modal.querySelector('.modal-title');
    const modalBody = modal.querySelector('.modal-body');

    modalTitle.textContent = "삭제 확인";
    modalBody.textContent = "정말로 삭제하시겠습니까?";

    // Bootstrap 5의 모달을 수동으로 표시하는 코드
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

function showConfirmModal(event, actionType) {
    event.preventDefault(); // form 제출을 방지

    // 모달 내용 변경
    document.querySelector(".modal-body").textContent = "정말로 삭제하시겠습니까?";

    // actionType에 따라 data-action 설정 (필요한 경우)
    document.getElementById('commonModal').dataset.action = actionType;
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

        });
        // 답글 등록
        $(".comment-list-section").on("click", ".post-reply", function () {
            var $replyForm = $(this).closest(".replyForm");

            var formData = {
                commuCommentMberId: $("#commentForm").find("[name='commuCommentMberId']").val(),
                commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
                commuCommentCntnt: $replyForm.find("[name='commuCommentCntnt']").val(),
                commuCommentRefId: $replyForm.find("[name='commuCommentRefId']").val()
            };

        });
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
        $(".comment-list-section").on("click", ".delete-comment", function () {
            var commuCommentId = $(this).data("id");
          

            var requestUrl = "/commu/comment/delete";

            

            $.ajax({
                url: requestUrl,
                type: "POST",
                data: JSON.stringify(commuCommentId),// 데이터 전송
                contentType: 'application/json',
                dataType: 'json',
                success: function () {
                
                    // 만약 부모 댓글 ID가 존재하면, 해당 댓글의 답글 개수를 업데이트합니다.
                    if (parentCommentId) {
                        updateReplyCount(parentCommentId);
                    }

                    // 전체 댓글 개수를 업데이트합니다.
                    updateCommentCount();
                },
                error: function (err) {
                    console.log(err);
                    alert("댓글 삭제 중 오류 발생");
                }
            });
        });


    }
});

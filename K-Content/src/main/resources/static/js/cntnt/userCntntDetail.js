const bkmk = $("#cntntBkmk");
// var star = $(".goodsBkmk");
// 컨텐츠 좋아요
bkmk.click(function () {
    if ($(this).hasClass("fa-regular")) {
        $(this).addClass("fa-solid").removeClass("fa-regular");
        $(this).css("color", "#14dbc8")
        cntntBkmk();
    } else {
        $(this).addClass("fa-regular").removeClass("fa-solid");
        $(this).css("color", "")
        cancleCntntBkmk();
    }
});

function cntntBkmk() {
    var cntntId = document.getElementById("cntntId").value;
    $.ajax({
        url: '/bkmk',
        type: 'post',
        data: {
            contentId: cntntId
        },
        success: function (data) {
            console.log("컨텐츠 좋아요: " + data)
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

function cancleCntntBkmk() {
    var cntntId = document.getElementById("cntntId").value;
    $.ajax({
        url: '/bkmk',
        type: 'delete',
        data: {
            contentId: cntntId
        },
        success: function (data) {
            console.log("컨텐츠 좋아요 취소: " + data)
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

// 상품 좋아요
function goodsBkmk(goodsId) {
    $.ajax({
        url: '/bkmk/goods',
        type: 'post',
        data: {
            goodsId: goodsId
        },
        success: function (data) {
            console.log("굿즈 좋아요: " + data)
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

function cancleGoodsBkmk(goodsId) {
    $.ajax({
        url: '/bkmk/goods',
        type: 'delete',
        data: {
            goodsId: goodsId
        },
        success: function (data) {
            console.log("굿즈 좋아요 cnlth: " + data)
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}



function clickGoods(goodsId) {
    target = document.getElementById(goodsId);
    if ($(target).hasClass("fa-regular")) {
        $(target).addClass("fa-solid").removeClass("fa-regular");
        $(target).css("color", "#14dbc8");
        goodsBkmk(goodsId);
    } else {
        $(target).addClass("fa-regular").removeClass("fa-solid");
        $(target).css("color", "");
        cancleGoodsBkmk(goodsId);

    }
}

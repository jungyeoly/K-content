$(document).ready(function () {
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

    // star.click(function (event, goodsId) {
    //     event.preventDefault();
    //     if ($(this).hasClass("fa-regular")) {
    //         $(this).addClass("fa-solid").removeClass("fa-regular");
    //         $(this).css("color", "#14dbc8");
    //         console.log(goodsId)
    //         goodsBkmk(goodsId);
    //     } else {
    //         $(this).addClass("fa-regular").removeClass("fa-solid");
    //         $(this).css("color", "");
    //         cancleGoodsBkmk(goodsId);
    //     }
    // })
})

function cntntBkmk() {
    var cntntId = document.getElementById("cntntId").value;
    $.ajax({
        url: '/bkmk',
        type: 'post',
        data: {
            contentId: cntntId
        }, // 데이터 객체 전달
        success: function (data) {
            console.log("컨텐츠 좋아요: "+data)
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
        }, // 데이터 객체 전달
        success: function (data) {
            console.log("컨텐츠 좋아요 취소: "+ data)
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

// 상품 좋아요
function goodsBkmk(goodsId) {
    console.log("상품 좋아요: " + goodsId)
}

function cancleGoodsBkmk(goodsId) {
    console.log("상품 지운다 " + goodsId)
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

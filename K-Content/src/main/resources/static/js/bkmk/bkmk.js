$(function () {
    $('ul.tab li').click(function () {
        var activeTab = $(this).attr('data-tab');
        $('ul.tab li').removeClass('on');
        $('.tabcont').removeClass('on');
        $(this).addClass('on');
        $('#' + activeTab).addClass('on');
    })

    $('.cntntI').click(function () {
        event.stopPropagation();
        const cntntId = $(this).parent().attr('cntntId');

        Swal.fire({
            title: '삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
            confirmButtonColor: '#14dbc8', // confrim 버튼 색깔 지정
            cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
            confirmButtonText: '확인', // confirm 버튼 텍스트 지정
            cancelButtonText: '취소', // cancel 버튼 텍스트 지정

            reverseButtons: true, // 버튼 순서 거꾸로

        }).then(result => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/bkmk',
                    type: 'DELETE',
                    data: {contentId: cntntId},
                    success: function () {
                        window.location.reload();
                    }
                })

            }
        });

    })

    $('.goodsI').click(function () {
        event.stopPropagation();
        const goodsId = $(this).parent().attr('goodsId');

        Swal.fire({
            title: '삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
            confirmButtonColor: '#14dbc8', // confrim 버튼 색깔 지정
            cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
            confirmButtonText: '확인', // confirm 버튼 텍스트 지정
            cancelButtonText: '취소', // cancel 버튼 텍스트 지정

            reverseButtons: true, // 버튼 순서 거꾸로

        }).then(result => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/bkmk/goods',
                    type: 'DELETE',
                    data: {goodsId: goodsId},
                    success: function () {
                        window.location.reload();
                    }
                })
            }
        });
    })
});

function cntntDetail(cntntId) {
    const formHtml = `<form id="contentDetail" action="/user/content/detail" method="get">
							<input  id="targetContentIdF" name="targetContentIdF"  />
						</form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('contentDetail').submit();
}
//상품클릭시 이동할건지 물어봄
function moveGoods(goodsUrl) {
    Swal.fire({
        title: '이동 여부',
        text: '해당 상품 판매 사이트로 이동하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        confirmButtonColor: '#14dbc8'
    }).then((result) => {
        if (result.isConfirmed) {
            window.open(goodsUrl);
        }
    });
}

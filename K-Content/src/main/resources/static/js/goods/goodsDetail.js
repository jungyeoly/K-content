//상품 삭제화면 요청
function deleteGoods() {


    Swal.fire({
        title: '정말 삭제하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#14dbc8',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        reverseButtons: true
    }).then(result => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/cs/cntnt/goods', type: 'patch',
                data: {
                    goodsId: document.getElementById("goodsId").value
                }, // 데이터 객체 전달
                success: function (data2) {

                    Swal.fire({
                        title: '상품이 삭제 되었습니다.',
                        icon: 'success',
                        confirmButtonColor: '#14dbc8',
                        confirmButtonText: '확인',
                        reverseButtons: true

                    }).then(result => {
                        if (result.isConfirmed) {
                            window.location.replace("/cs/cntnt/goods");
                        }
                    });

                }, error: function (error) {
                    console.error('에러 발생: ', error);
                }
            });
        }
    });
}

//상품 수정화면 요청
function updateGoods() {
    goodsID = document.getElementById('goodsId').value;

    const formHtml = `
                    <form id="updateGoods" action="/cs/cntnt/goods/modify-form" method="get">
                        <input  id="goodsIdf" name="goodsId"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("goodsIdf").value = goodsID;
    document.getElementById('updateGoods').submit();
}

let parentDiv = document.getElementById('goodsPurchsText');
let childDiv = document.getElementById('goodsPurchsLink');

let childHeight = childDiv.clientHeight;
parentDiv.style.height = childHeight + 'px';

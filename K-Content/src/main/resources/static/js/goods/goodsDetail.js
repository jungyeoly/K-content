//상품 삭제화면 요청
function deleteGoods() {
    if (confirm('상품을 삭제하시겠습니까?')) {
        $.ajax({
            url: '/cs/goods', type: 'patch',
            data: {
                goodsId: document.getElementById("goodsId").value
            }, // 데이터 객체 전달
            success: function (data2) {
                if (confirm('삭제가 완료 되었습니다')) {
                    window.location.replace("/cs/goods/main");
                } else {
                    window.location.replace("/cs/goods/main");
                }
            }, error: function (error) {
                console.error('에러 발생: ', error);
            }
        });
    }
}

//상품 수정화면 요청
function updateGoods() {
    goodsID = document.getElementById('goodsId').value;

    const formHtml = `
                    <form id="updateGoods" action="/cs/test/goods/modify-form" method="get">
                        <input  id="goodsIdf" name="goodsId"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("goodsIdf").value = goodsID;
    document.getElementById('updateGoods').submit();
}

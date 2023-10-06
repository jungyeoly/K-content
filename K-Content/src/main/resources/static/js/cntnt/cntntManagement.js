


function cntntDetail(cntntId) {
// 유저 콘텐츠 디테일 페이지로 넘어가
    const formHtml = `
                    <form id="contentDetail" action="/user/content/detail" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('contentDetail').submit();


}

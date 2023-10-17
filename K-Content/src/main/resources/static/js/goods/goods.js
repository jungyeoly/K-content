// $(document).ready(function () {
//
//     $.ajax({
//         url: '/cs/test/goods/list',
//         type: 'GET',
//         success: function (data) {
//             const element = document.getElementById('goodsPad');
//
//             for (var i = 0; i < data.length; i++) {
//                 inHtml = `<div class="col-xl-3 col-lg-6" >
//                         <div class="single-category mb-30" onclick="addList(${data[i].goodsId})">
//                             <div class="category-img">
//
//                                 <img style="width: 200px; height: 130px; margin:auto; display: block" src="/img/goods/${data[i].goodsFileId}" alt="">
//
//                                 <div class="category-caption">
//                                     <h6 style="text-align: center; " >${data[i].goodsName}</h6>
//                                 </div>
//                             </div>
//                         </div>
//                     </div>`;
//                 element.insertAdjacentHTML('beforeend', inHtml);
//             }
//         },
//         error: function (error) {
//             console.error('에러 발생: ', error);
//         }
//     });
// });
//
// const clickGoodsList = [];
//
// function selectEnd(){
//     const openerWindow = window.opener;
//     window.opener.postMessage(clickGoodsList, "*");
//
//     window.close();
// }
//
// function addList(goodsId) {
//     if (clickGoodsList.includes(goodsId)) {
//         clickGoodsList.pop(goodsId);
//     } else {
//         clickGoodsList.push(goodsId);
//     }
// }
//
// function searchKeyword() {
//     var requestData = {
//         search: document.getElementById("search").value
//     };
//     $.ajax({
//         url: '/cs/test/goods/search',
//         type: 'GET',
//         data: requestData,
//         success: function (data) {
//             const element = document.getElementById('goodsPad');
//             element.innerHTML = "";
//             for (var i = 0; i < data.length; i++) {
//                 inHtml = `<div class="col-xl-4 col-lg-6" >
//                         <div class="single-category mb-30"  onclick="addList(${data[i].goodsId})">
//                             <div class="category-img" style="width: 300px; height: 400px">
//                                 <img style="width: 300px; height: 400px" src="/img/goods/${data[i].goodsFileId}"  alt="">
//                                 <div class="category-caption">
//                                     <h6 style="text-align: center; " >${data[i].goodsName}</h6>
//                                 </div>
//                             </div>
//                         </div>
//                     </div>`;
//                 element.insertAdjacentHTML('beforeend', inHtml);
//             }
//         },
//         error: function (error) {
//             console.error('에러 발생: ', error);
//         }
//     });
//
// }
//

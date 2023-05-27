var clientKey = 'test_ck_O6BYq7GWPVvKaD0ZD0arNE5vbo1d'
var tossPayments = TossPayments(clientKey)
function order(method, requestOrder) {
    console.log("click");
    pay(method,requestOrder)
        .catch(e => {
            console.log(e)
        });
}
function pay(method, requestOrder) {
    tossPayments.requestPayment('카드', {
        amount: 15000,
        orderId: 'Kt8oaFk2Sca6aFikV-Rjl-abc',
        orderName: '토스 티셔츠 외 2건',
        customerName: '박토스',
        successUrl: 'http://localhost:8080/toss/success',
        failUrl: 'http://localhost:8080/toss/fail',
    });
}
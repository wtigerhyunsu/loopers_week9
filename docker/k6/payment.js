import http from 'k6/http';
import { check, sleep } from 'k6';

// 테스트 옵션: 5명의 가상 사용자가 30초 동안 테스트를 진행
export const options = {
    vus: 100,
    duration: '60s',
};

// API 주소 및 공통 헤더
const ORDER_API_URL = 'http://host.docker.internal:8080/api/v1/orders';
const PAYMENT_API_URL = 'http://host.docker.internal:8080/api/v1/payment';
const HEADERS = {
    'Content-Type': 'application/json',
    'X-USER-ID': 'testuser123',
};

export default function () {
    // 1. 주문 생성 요청 (POST /api/v1/orders)
    // 상품 3개를 사용하여 주문 데이터를 만듭니다.
    const orderRequestBody = {
        address: 'test address',
        items: [
            { productId: 1, quantity: 1 },
            { productId: 2, quantity: 2 },
            { productId: 3, quantity: 3 },
        ],
        memo: 'test order',
    };

    let orderRes = http.post(ORDER_API_URL, JSON.stringify(orderRequestBody), { headers: HEADERS });
    console.log("Response:", orderRes.body);
    // 응답 상태 코드가 200인지 확인하고, 실패 시 다음 단계를 건너뜁니다.
    if (!check(orderRes, { 'order creation is successful': (r) => r.status === 200 })) {
        console.error('Order creation failed. Status: ' + orderRes.status);
        return;
    }

    // 2. 주문 응답에서 orderNumber 추출
    const orderNumber = orderRes.json().orderNumber;
    if (!orderNumber) {
        console.error('Failed to get orderNumber from response.');
        return;
    }

    // 3. 결제 요청 (POST /api/v1/payment)
    const cardTypes = ['SAMSUNG', 'KB', 'HYUNDAI'];
    const randomCardType = cardTypes[Math.floor(Math.random() * cardTypes.length)];
    const randomCardNumber = '0000-' + Math.floor(Math.random() * 9000 + 1000) + '-0000-0000';

    // 60% 확률로 의도적인 실패 요청을 만듭니다.
    let paymentRequestBody;
    if (Math.random() < 0.6) {
        // 의도적인 실패 데이터: 유효하지 않은 카드사 사용
        paymentRequestBody = {
            orderNumber: orderNumber,
            paymentTool: 'CARD',
            cardInfo: {
                cardType: 'NONEXISTENT',
                cardNumber: randomCardNumber,
            },
            payment: 10000,
            description: 'failure test',
        };
    } else {
        // 성공적인 결제 데이터
        paymentRequestBody = {
            orderNumber: orderNumber,
            paymentTool: 'CARD',
            cardInfo: {
                cardType: randomCardType,
                cardNumber: randomCardNumber,
            },
            payment: 10000,
            description: 'success test',
        };
    }

    const paymentRes = http.post(PAYMENT_API_URL, JSON.stringify(paymentRequestBody), { headers: HEADERS });

    check(paymentRes, {
        'payment status is 200 or 4xx': (r) => r.status === 200 || (r.status >= 400 && r.status < 500),
    });

    // 1초 대기 후 다음 반복 실행
    sleep(1);
}

import http from 'k6/http';
import { check, sleep } from 'k6';

// 부하 설정
export const options = {
    vus: 10, // 동시에 실행할 가상 사용자 수
    duration: '60s', // 테스트 시간
};

export default function () {
    const url = 'http://host.docker.internal:8080/api/v1/products?currentPage=0&perPage=10&sort=LIKES_DESC'; // 엔드포인트 주소
    const res = http.get(url);

    // 응답 검증
    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1); // 요청 간 대기 시간
}

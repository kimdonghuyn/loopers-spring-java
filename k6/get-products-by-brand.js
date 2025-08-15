import http from 'k6/http';
import { check } from 'k6';

export const options = {
    stages: [
        { duration: '1m', target: 25 },
    ],
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
    const brandId = [1, 2, 3, 4, 5][Math.floor(Math.random() * 5)];
    const page = Math.floor(Math.random() * 3);   // 실제 존재하는 페이지 범위로 제한
    const size = 20;
    const sort = 'likeCount,desc';
    const url = `${BASE_URL}/api/v1/products?brandId=${brandId}&sort=${sort}&page=${page}&size=${size}`;

    const res = http.get(url, { tags: { name: 'GET /api/v1/products' }, responseType: 'none', timeout: '5s' });

    // 상태 코드 확인
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
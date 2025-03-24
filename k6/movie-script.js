import http from 'k6/http';
import { check, sleep } from 'k6';

const N = 500; // DAU
const dailyRequests = N * 5;
const avgRPS = dailyRequests / 86400; // 1일 평균 RPS
const peakRPS = Math.max(1, Math.round(avgRPS * 10)) // 1일 최대 RPS (소수점 제거, 최소값 1이상)

export let options = {
    scenarios: {
        constant_load: {
            executor: 'constant-arrival-rate',
            rate: peakRPS, // 초당 요청 수 (최대 RPS)
            timeUnit: '1s',
            duration: '1m',
            preAllocatedVUs: N,
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<200'], // 95% 요청이 200ms 이하
        http_req_failed: ['rate<0.01'], // 실패율 1% 이하
    },
};

const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
    let res = http.get(`${BASE_URL}/api/movies`); // 상영 중인 영화 목록 전체 조회
    //let res = http.get(`${BASE_URL}/api/movies?title=123&genre=Comedy); // 검색 파라미터 추가

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 200ms': (r) => r.timings.duration < 200,
    });

    sleep(1);
}

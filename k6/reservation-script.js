import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        gradual_uinque_users: {
            executor: 'per-vu-iterations',
            vus: 100,            // 100명의 가상 사용자
            iterations: 1,        // 각 사용자 1회 요청
            maxDuration: '5m',    // 최대 실행 시간
            startTime: '0s',
        },
    }
};

const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
    const userId = __VU;
    const scheduleId = 1;
    const seatIds = [1, 2, 3]

    let res = http.post(`${BASE_URL}/api/reservations`,
        JSON.stringify({
            userId: userId,
            scheduleId: scheduleId,
            seatIds: seatIds
        }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    check(res, {
        'reservation success': (r) => r.status === 201,
        'reservation conflict': (r) => r.status === 409,
        'response time < 200ms': (r) => r.timings.duration < 200,
    });

    sleep(1);
}
import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100,
    duration: '10s',
};

export default function () {

    const response = http.get(
        'http://127.0.0.1:8081/check',
        {
            headers: {
                'X-API-Key': 'key-123'
            }
        }
    );

    check(response, {
        'status is 200 or 429': (r) =>
            r.status === 200 || r.status === 429,
    });
}
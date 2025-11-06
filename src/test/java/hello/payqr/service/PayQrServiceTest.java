package hello.payqr.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class PayQrServiceTest {

    @Test
    void timeTest() {
        long timestamp = 1762411021321L;

        // KST 기준으로 변환
        ZonedDateTime dateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.of("Asia/Seoul"));

        // 원하는 포맷으로 출력
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("변환된 시간 (KST): {}", formatted);

    }

}
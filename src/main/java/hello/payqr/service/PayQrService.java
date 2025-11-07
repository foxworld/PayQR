package hello.payqr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import hello.payqr.domain.QrToken;
import hello.payqr.dto.PayloadDto;
import hello.payqr.repository.PayQrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayQrService {

    private final PayQrRepository repository;
    private QrToken currentToken;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public QrToken createNewToken(String token, int amount, String item) {
        LocalDateTime now = LocalDateTime.now();
        currentToken =QrToken.builder()
                .token(token)
                .amount(amount)
                .item(item)
                .createdAt(now)
                .expiresAt(now.plus(Duration.ofMinutes(10)))
                .build();

        String resultToken = repository.save(currentToken);
        QrToken finidQrToken = repository.findById(token);
        log.info("findQrToken={}", finidQrToken);

        return currentToken;
    }

    public byte[] generateQrImage(String token, int amount, String item) throws Exception {

        currentToken = createNewToken(token, amount, item);

        String content = buildPayload(currentToken);
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8); // 한글 인코딩 설정
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);

        return out.toByteArray();
    }

    private String buildPayload(QrToken qrToken) throws JsonProcessingException {
        long createdAt = qrToken.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();
        long expiresAt = qrToken.getExpiresAt().plus(Duration.ofMinutes(10)).atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();

        // 검증
        ZonedDateTime createdTime = Instant.ofEpochSecond(createdAt).atZone(ZoneId.systemDefault());
        ZonedDateTime expiresTime = Instant.ofEpochSecond(expiresAt).atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("createdAt={},{}", createdAt, createdTime.format(formatter));
        log.info("expiresAt={},{}", expiresAt, expiresTime.format(formatter));


        PayloadDto payload = PayloadDto.builder()
                .token(qrToken.getToken())
                .amount(qrToken.getAmount())
                .item(qrToken.getItem())
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();

        // 객체를 JSON 문자열로 변환
        log.info("payload={}", payload);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payload);
        log.info("json={}", jsonString);

        return jsonString;
    }

    public boolean validateToken(String token) {
        return currentToken != null && !currentToken.isExpired() && currentToken.getToken().equals(token);
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행 (600,000ms)
    public void refreshQrToken(String token, int amount, String item) {

        LocalDateTime now = LocalDateTime.now();
        currentToken = QrToken.builder()
                .token(UUID.randomUUID().toString())
                .amount(1)
                .item("아메리카노")
                .createdAt(now)
                .expiresAt(now.plusMinutes(10))
                .build();

        log.info("QR 자동 갱신됨: " + currentToken.getToken());
    }

}

package hello.payqr.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import hello.payqr.config.WebSocketSessionManager;
import hello.payqr.domain.QrToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrScheduler {
    private final SimpMessagingTemplate messagingTemplate;
    private final PayQrService service;
    private final WebSocketSessionManager sessionManager;

    @Scheduled(fixedRate = 60000)
    public void refreshQr() throws JsonProcessingException {
        if (!sessionManager.hasActiveSessions()) {
            log.info("⏸️ WebSocket 연결 없음. QR 전송 생략.");
            return;
        }

        String token = service.generateToken();
        QrToken qr = service.createNewToken(token, 3000, "아메리카노");

        String jsonString = service.buildPayload(qr);

        messagingTemplate.convertAndSend("/topic/qr", jsonString); // 클라이언트에 실시간 전송
        log.info("🔄 QR 자동 갱신됨: {}", qr.getToken());
    }

}

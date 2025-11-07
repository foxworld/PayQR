package hello.payqr.service;


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

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void refreshQr() {
        String token = service.generateToken();
        QrToken qr = service.createNewToken(token, 3000, "아메리카노");

        messagingTemplate.convertAndSend("/topic/qr", qr); // 클라이언트에 실시간 전송
        log.info("🔄 QR 자동 갱신됨: {}", qr.getToken());
    }

}

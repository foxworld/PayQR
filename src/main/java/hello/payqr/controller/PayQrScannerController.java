package hello.payqr.controller;

import hello.payqr.service.PayQrScannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PayQrScannerController {

    private final PayQrScannerService qrScannerService;

    @GetMapping("/scan")
    public String scanQr() {
        log.info("📥 QR 스캔 요청 수신됨");

        new Thread(() -> {
            try {
                qrScannerService.startScanning();
            } catch (IOException e) {
                log.error("QR 스캔 중 오류 발생", e);
            }
        }).start();

        return "scanner"; // Thymeleaf 템플릿 이름 (qr.html)
    }


    @PostMapping("/api/qr/result")
    public ResponseEntity<Void> receiveQr(@RequestBody Map<String, String> payload) {
        String qrData = payload.get("qrData");
        log.info("QR 수신됨: {}", qrData);
        return ResponseEntity.ok().build();
    }

}

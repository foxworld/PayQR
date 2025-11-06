package hello.payqr.controller;

import hello.payqr.service.PayQrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/pay/qr/")
@RequiredArgsConstructor
public class PayQrApiController {

    private final PayQrService service;

    @GetMapping({"","/"})
    public String Home() {
        log.info("basic 출력");
        return "Copyright© 2025. KSNET INC, All rights reserved. <br/>\n"
                + "@Project: PayQR API Project!! <br/>\n"
                + "@Company: KSNET INC. <br/>\n"
                + "@Name: PETER HOON LEE <br/>\n"
                //+ "@TelNo: 02-3420-5844 <br/>\n"
                + "@Email: foxworld@ksnet.co.kr <br/>\n"
                + "@Created: 2025-11-06 <br/>\n";
    }

    @GetMapping({"/image","/image/"})
    public ResponseEntity<byte[]> getQrImage(@RequestParam int amount, @RequestParam String item) throws Exception {
        String token = service.generateToken();
        byte[] image = service.generateQrImage(token, amount, item);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateQr(@RequestParam String token) {
        boolean valid = service.validateToken(token);
        return valid
                ? ResponseEntity.ok("QR 유효함")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("QR 만료 또는 잘못된 토큰");
    }

}

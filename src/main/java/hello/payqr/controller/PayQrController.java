package hello.payqr.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PayQrController {
    @GetMapping("/qr")
    public String showQrPage(Model model) {
        model.addAttribute("token", "abc123");
        return "qr"; // templates/qr.html 렌더링
    }
}

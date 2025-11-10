package hello.payqr.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketSessionManager sessionManager;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 클라이언트 구독 경로
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS(); // WebSocket 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:8080", "http://foxworld.ksnet.co.kr:8080") // 명시적 도메인 설정
                .withSockJS();
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        String sessionId = Objects.requireNonNull(event.getMessage().getHeaders().get("simpSessionId")).toString();
        sessionManager.addSession(sessionId);

        log.info("WebSocket 연결됨: {}", event.getMessage());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        sessionManager.removeSession(event.getSessionId());
        log.info("WebSocket 연결 종료: {}", event.getSessionId());
    }
}

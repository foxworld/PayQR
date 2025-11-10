package hello.payqr.config;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketSessionManager {
    private final Set<String> connectedSessionIds = Collections.synchronizedSet(new HashSet<>());

    public void addSession(String sessionId) {
        connectedSessionIds.add(sessionId);
    }

    public void removeSession(String sessionId) {
        connectedSessionIds.remove(sessionId);
    }

    public boolean hasActiveSessions() {
        return !connectedSessionIds.isEmpty();
    }
}

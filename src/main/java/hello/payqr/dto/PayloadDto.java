package hello.payqr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDto {
    private String token;
    private int amount;
    private String item;
    private long createdAt;
    private long expiresAt;
}

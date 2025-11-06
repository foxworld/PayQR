package hello.payqr.repository;

import hello.payqr.domain.QrToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPayQrRepository extends JpaRepository<QrToken, String> {
}

package hello.payqr.repository;

import hello.payqr.domain.QrToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class PayQrRepository {

    private final JpaPayQrRepository repository;

    public String save(QrToken qrToken) {
        return repository.save(qrToken).getToken();
    }

    public QrToken findById(String id) {
        return repository.findById(id).orElse(null);
    }


}

package app.repository;

import app.model.PayCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayCardRepository extends JpaRepository<PayCard, String> {
    List<PayCard> findAllByOwnerId(String id);

    List<PayCard> findAllByType(String type);

    List<PayCard> findAllByCurrency(String currency);
}

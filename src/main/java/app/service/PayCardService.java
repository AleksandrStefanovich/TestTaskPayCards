package app.service;

import app.model.PayCard;
import app.repository.PayCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayCardService {

    @Autowired
    PayCardRepository payCardRepository;
    public List<PayCard> readAll() {
        return payCardRepository.findAll();
    }

    public PayCard findPayCardByID(String id) {
        return payCardRepository.findById(id).orElse(null);
    }

    public PayCard saveNewPayCard(PayCard payCard) {
        return payCardRepository.save(payCard);
    }

    public boolean delete(String id) {
        if(payCardRepository.existsById(id)){
            payCardRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public List<PayCard> findPayCardByClientID(String id) {
        return payCardRepository.findAllByOwnerId(id);
    }

    public List<PayCard> findPayCardByType(String type) {
        return payCardRepository.findAllByType(type);
    }

    public List<PayCard> findPayCardByCurrency(String currency) {
        return payCardRepository.findAllByCurrency(currency);
    }
}

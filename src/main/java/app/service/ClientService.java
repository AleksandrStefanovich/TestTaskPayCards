package app.service;

import app.model.Client;
import app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public List<Client> readAll() {
        return clientRepository.findAll();
    }

    public Client findClientByID(String id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client saveNewClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(String id, Client client) {
        Client savedClient = clientRepository.findById(id).orElseThrow();

        if(savedClient.equals(client)){
            return savedClient;
        }else {
            client.setId(id);
            clientRepository.save(client);
            return clientRepository.findById(id).orElseThrow();
        }
    }

    public boolean delete(String id) {
        if(clientRepository.existsById(id)){
            clientRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}

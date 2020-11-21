package app.controller;

import app.model.Client;
import app.model.PayCard;
import app.service.ClientService;
import app.service.PayCardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    PayCardService payCardService;

    @GetMapping("/clients")
    @ApiOperation(value = "Read all clients",  notes = "Will get all the clients")
    public List<Client> clients(){
        return clientService.readAll();
    }

    @GetMapping("/clients/{id}")
    @ApiOperation(value = "Read client by Id", notes = "Will get client with given Id")
    public Client getClient(@PathVariable String id){
        return clientService.findClientByID(id);
    }

    @GetMapping("/clients/phones&{type}&{currency}&{amountPerPage}&{pageNumber}")
    @ApiOperation(value = "Read phones matching given filters", notes = "Will get client phones matching filters and amount")
    public List<String> getPhones(@RequestParam String type, String currency,
                                  @PositiveOrZero int amountPerPage,
                                  @PositiveOrZero int pageNumber){
        List<String> userIds = new ArrayList<>();
        List<PayCard> payCardsByType = payCardService.findPayCardByType(type);
        List<PayCard> payCardsByCurrency = payCardService.findPayCardByCurrency(currency);
        for (PayCard payCard : payCardsByType) {
            if(!userIds.contains(payCard.getOwnerId())){
            userIds.add(payCard.getOwnerId());}
        }
        for (PayCard payCard : payCardsByCurrency) {
            if(!userIds.contains(payCard.getOwnerId())){
            userIds.add(payCard.getOwnerId());}
        }
        List<String> phones = new ArrayList<>();
        for (String userId : userIds) {
            phones.add(clientService.findClientByID(userId).getPhoneNumber());
        }
        if(pageNumber==0){pageNumber = 1;}
        if(amountPerPage == 0) {
                 return phones;
        }else{
            if(phones.size()<(pageNumber)*amountPerPage - 1){
                if (phones.size()<=(pageNumber-1)*amountPerPage){
                    return null;
                } else{
                    return phones.subList((pageNumber - 1) * amountPerPage, phones.size() - 1);
                }
            } else {
                return phones.subList((pageNumber - 1) * amountPerPage, (pageNumber) * amountPerPage - 1);
            }
        }
    }

    @PostMapping("/clients")
    @ApiOperation(value = "Create new client", notes = "Creates new client. param id and status shouldn't be changed")
    public Client createClient(@Valid @RequestBody Client client){
        return clientService.saveNewClient(client);
    }

    @PutMapping("/clients/{id}")
    @ApiOperation(value = "Update client", notes = "Updates client with given id")
    public Client updateClient(@PathVariable String id,
                               @Valid @RequestBody Client client){
        return clientService.updateClient(id, client);
    }

    @GetMapping("/clients/{id}/paycards")
    @ApiOperation(value = "Read client cards", notes = "Reads cards belonging to client with given id")
    public List<PayCard> getClientPayCards(@PathVariable String id){
        return payCardService.findPayCardByClientID(id);
    }


    @PostMapping("/clients/{id}/paycards")
    @ApiOperation(value = "Create new card", notes = "Adds new card, updates client status. param id and ownerId shouldn't be filled")
    public Client addClientPayCard(@PathVariable String id,
                                @Valid @RequestBody PayCard payCard){
        Client client = clientService.findClientByID(id);
        payCard.setOwnerId(id);
        String type = payCard.getType();
        switch (type){
            case "Classic": {if(client.getStatus()<=0)client.setStatus(0); break;}
            case "Gold": {if(client.getStatus()<1) client.setStatus(1); break;}
            case "Platinum": {client.setStatus(2); break;}
        }
        payCardService.saveNewPayCard(payCard);
        return clientService.updateClient(id, client);
    }


    @DeleteMapping("/clients/{id}")
    @ApiOperation(value = "Delete client", notes = "Deletes client with given id")
    public ResponseEntity deleteClient(@PathVariable String id){
        boolean result = clientService.delete(id);
        return result ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validation(MethodArgumentNotValidException e){
        final Map<String,String> messages = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(objectError ->
                messages.put(((FieldError)objectError).getField(), objectError.getDefaultMessage())
        );
        return messages;
    }
}

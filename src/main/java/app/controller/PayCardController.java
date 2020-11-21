package app.controller;

import app.model.PayCard;
import app.service.PayCardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PayCardController {
    @Autowired
    PayCardService payCardService;

    @GetMapping("/paycards")
    @ApiOperation(value = "Read all cards", notes = "Reads all cards")
    public List<PayCard> payCards(){
        return payCardService.readAll();
    }

    @GetMapping("/paycard/{id}")
    @ApiOperation(value = "Read card by id", notes = "Reads card with given id")
    public PayCard getPayCard(@PathVariable String id){
        return payCardService.findPayCardByID(id);
    }

    @DeleteMapping("/paycards/{id}")
    @ApiOperation(value = "Delete card", notes = "Deletes card with given id")
    public ResponseEntity deletePayCard(@PathVariable String id){
        boolean result = payCardService.delete(id);
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

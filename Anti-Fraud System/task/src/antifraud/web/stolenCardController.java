package antifraud.web;

import antifraud.annonations.phoneValidator.validCardNumber;
import antifraud.business.security.stolenCardService;
import antifraud.persitence.models.security.stolenCard;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@Controller
@RequestMapping("/api/antifraud/stolencard")
@Validated
public class stolenCardController {

    private final stolenCardService stolenCardService;

    @Autowired
    stolenCardController(stolenCardService sCS) {
        stolenCardService = sCS;
    }

    @PostMapping
    public ResponseEntity<?> PostStolenCard(@RequestBody @Valid stolenCard sC) {

        stolenCard sCA = stolenCardService.add(sC);

        return ResponseEntity.ok().body(sCA);
    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<?> deleteStolenCard(@PathVariable
                                              @validCardNumber
                                              String cardNumber) {

        stolenCardService.delete(cardNumber);

        return ResponseEntity.ok().body(Map.of("status","Card " + cardNumber + " successfully removed!"));
    }

    @GetMapping
    public ResponseEntity<?> getStolenCard() {
        return ResponseEntity.ok().body(stolenCardService.getAll());
    }


}

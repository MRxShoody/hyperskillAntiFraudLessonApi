package antifraud.web;

import antifraud.business.transaction.transactionService;
import antifraud.business.user.roleService;
import antifraud.business.user.userService;
import antifraud.configuration.authentication.userDetailsImpl;
import antifraud.models.requests.*;
import antifraud.models.responses.changeRoleResponse;
import antifraud.models.responses.registerResponse;
import antifraud.models.responses.unlockLockResponse;
import antifraud.persitence.repos.transaction.transactionRepo;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Controller
@Validated
public class mainController {

    private final transactionService transactionService;
    private final userService userService;
    private final roleService roleService;

    @Autowired
    public mainController(transactionService ts, userService us, roleService rs) {
        this.transactionService = ts;
        this.userService = us;
        this.roleService = rs;
    }


    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> transaction(@RequestBody @Valid transactionRequest request, @AuthenticationPrincipal userDetailsImpl user) {
        return ResponseEntity.ok().body(transactionService.processTransaction(request, user.getUser()));
    }

    /***********************/

    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<?> transactionFeedback(@RequestBody @Valid transactionsFeedbackRequest request){
        return ResponseEntity.ok().body(transactionService.processTransactionFeedback(request));
    }


    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<?> getTransaction(@PathVariable @CreditCardNumber  String number) {
        return transactionService.findAllByNumber(number);
    }

    @GetMapping("/api/antifraud/history")
    public ResponseEntity<?> getTransactions() {
        return transactionService.findAll();
    }

    /***********************/

    @PostMapping("/api/auth/user")
    public ResponseEntity<?> registration(@RequestBody registerRequest request) {
        registerResponse rR = userService.registerUser(request);
        return ResponseEntity.created(URI.create("/api/auth/user")).body(rR);
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<?> deletion(@PathVariable String username) {
        Map<String, String> m = userService.deleteUser(username);
        return ResponseEntity.status(200).body(m);
    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<?> setRole(@RequestBody changeRoleRequest request) {
        changeRoleResponse cRR = roleService.setRole(request);
        return ResponseEntity.ok().body(cRR);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<?> setAccess(@RequestBody unlockLockRequest request) {
        unlockLockResponse uLR = userService.setAccess(request);
        return ResponseEntity.ok().body(uLR);
    }


}


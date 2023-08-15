package antifraud.web;

import antifraud.annonations.ipValidator.validIP;
import antifraud.business.security.suspiciousIpService;
import antifraud.persitence.models.security.suspiciousIP;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/antifraud/suspicious-ip")
@Validated
public class suspiciousIpController {

    private final suspiciousIpService suspiciousIpService;

    @Autowired
    public suspiciousIpController(suspiciousIpService sIP) {
        this.suspiciousIpService = sIP;
    }

    @PostMapping
    public ResponseEntity<?> postSuspiciousIp(@RequestBody @Valid suspiciousIP sI) {

        suspiciousIP sIA = suspiciousIpService.add(sI);


        return ResponseEntity.ok().body(sIA);
    }

    @DeleteMapping("/{ip}")
    public ResponseEntity<?> deleteSuspiciousIp(@PathVariable
                                                @validIP
                                                String ip) {

        suspiciousIpService.delete(ip);

        return ResponseEntity.ok().body(Map.of("status","IP " + ip + " successfully removed!"));
    }

    @GetMapping
    public ResponseEntity<?> getSuspiciousIp() {
        return ResponseEntity.ok().body(suspiciousIpService.getAll());
    }

}

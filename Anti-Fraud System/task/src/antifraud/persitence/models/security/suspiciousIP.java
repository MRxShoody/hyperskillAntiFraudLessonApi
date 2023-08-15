package antifraud.persitence.models.security;

import antifraud.annonations.ipValidator.validIP;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class suspiciousIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    public Long id;

    @Column(unique = true)
    @validIP
    public String ip;

}

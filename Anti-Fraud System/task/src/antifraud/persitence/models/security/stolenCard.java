package antifraud.persitence.models.security;

import antifraud.annonations.phoneValidator.validCardNumber;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class stolenCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    public Long id;

    @Column(unique = true)
    @validCardNumber
    public String number;

}

package app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PayCard {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    @Size(min = 16,max = 16, message = "Must be 16 characters, only digits")
    @Pattern(regexp = "^[0-9]{16}$",message = "Only digits, length must be 16")
    private String number;
    @Pattern(regexp = "BYN|USD|EUR", message = "Must be BYN or USD or EUR")
    private String currency;
    @Pattern(regexp = "Classic|Gold|Platinum", message = "Must be Classic or Gold or Platinum")
    private String type;
    private String ownerId;
}

package app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @Min(value = 0, message = "Must be one digit from 0 to 2")
    @Max(value = 2, message = "Must be one digit from 0 to 2")
    private int status;
}

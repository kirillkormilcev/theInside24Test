package info.theinside.token.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "authentications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotNull
    @Column(unique = true, length = 50)
    String name;
    @NotNull
    @Size(min = 8)
    @Column(length = 50)
    String password;
    @OneToOne(cascade = CascadeType.ALL)
    Token token;
}

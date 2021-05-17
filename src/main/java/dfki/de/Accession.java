package dfki.de;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class Accession {
    String prefix;
    Set<Types> types;

}

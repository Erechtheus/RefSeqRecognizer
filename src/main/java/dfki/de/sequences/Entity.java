package dfki.de.sequences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
@AllArgsConstructor
public class Entity {

    /** First character, inclusive, zero-based. */
    private int start;

    /** Last character, exclusive, zero-based. */
    private int stop;

    /** Accession type */
    Accession accession;
}

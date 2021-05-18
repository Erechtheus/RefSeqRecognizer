package dfki.de.sequences.accessions;

import dfki.de.sequences.Accession;
import dfki.de.sequences.Entity;
import dfki.de.sequences.Types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://www.uniprot.org/help/accession_numbers/
 */
public class UniProtExtractor {

    final private static String prefix="(^|[\\s\\(\\)\\[\\'\"/,])"; //>
    final private static String suffix="(?=([\\.,\\s\\)\\(\\]\\'\":;\\-/]|$))";//|:?[ATGC]>[ATGC]
    final private Pattern pattern;

    public UniProtExtractor() {
        pattern  = Pattern.compile(prefix +"(?<match>[OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2})" +suffix);
    }

    public List<Entity> findRefSeqMentions(String text){

        List<Entity> matches = new ArrayList<>();
        final Matcher matcher = pattern.matcher(text);

        while(matcher.find()){
            matches.add(new Entity(matcher.start("match"), matcher.end("match"), new Accession("todo", new HashSet<Types>()))); //Accession has to be compiled
        }

        return matches;
    }

    public static void main(String[] args) {
        String text =" A2BC19, P12345, A0A023GPI8";
        UniProtExtractor uniProtExtractor = new UniProtExtractor();

        List<Entity> entities = uniProtExtractor.findRefSeqMentions(text);

        for(Entity entity : entities){
            System.out.println(text.substring(entity.getStart(), entity.getStop()));
        }
    }
}

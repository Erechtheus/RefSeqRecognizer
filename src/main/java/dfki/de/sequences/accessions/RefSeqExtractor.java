package dfki.de.sequences.accessions;

import dfki.de.sequences.Accession;
import dfki.de.sequences.Entity;
import dfki.de.sequences.Types;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Exctracts reference sequences
 * https://www.ncbi.nlm.nih.gov/books/NBK21091/table/ch18.T.refseq_accession_numbers_and_mole/?report=objectonly/
 */
public class RefSeqExtractor {

    final private HashMap<Pattern, Accession> patterns;

    public RefSeqExtractor() {
        List<Accession> accessions = new ArrayList<>();

        accessions.add(new Accession("AC", new HashSet(Arrays.asList(Types.type.genomic))));
        accessions.add(new Accession("NC", new HashSet(Arrays.asList(Types.type.genomic))));
        accessions.add(new Accession("NG", new HashSet(Arrays.asList(Types.type.genomic))));
        accessions.add(new Accession("NT", new HashSet(Arrays.asList(Types.type.genomic))));
        accessions.add(new Accession("NW", new HashSet(Arrays.asList(Types.type.genomic))));

        accessions.add(new Accession("NM", new HashSet(Arrays.asList(Types.type.RNA))));
        accessions.add(new Accession("NR", new HashSet(Arrays.asList(Types.type.RNA))));

        accessions.add(new Accession("AP", new HashSet(Arrays.asList(Types.type.protein))));
        accessions.add(new Accession("NP", new HashSet(Arrays.asList(Types.type.protein))));
        accessions.add(new Accession("WP", new HashSet(Arrays.asList(Types.type.protein))));

        patterns = new HashMap<>();
        for(Accession accession : accessions){
            patterns.put(Pattern.compile(accession.getPrefix() +"_[0-9]+(?<version>\\.[1-9][0-9]*)?"), accession);
        }
    }

    public List<Entity> findRefSeqMentions(String text){

        List<Entity> matches = new ArrayList<>();

        for(Pattern pattern : patterns.keySet()){
            final Matcher matcher = pattern.matcher(text);

            while(matcher.find()){
                matches.add(new Entity(matcher.start(), matcher.end(), patterns.get(pattern)));
            }
        }

        return matches;
    }

    public static void main(String[] args) {


        RefSeqExtractor refSeqExtractor = new RefSeqExtractor();

        List<Entity> entities = refSeqExtractor.findRefSeqMentions("Hallo NM_123 ist cool");

        for(Entity entity : entities){
            System.out.println(entity);
        }

    }
}

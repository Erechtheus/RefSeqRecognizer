package dfki.de.sequences.accessions;

import dfki.de.sequences.Entity;

import java.util.ArrayList;
import java.util.List;

public class OverallExtractor {
    private GenBankExtractor extractor1 = new GenBankExtractor();
    private RefSeqExtractor extractor2 = new RefSeqExtractor();
    private UniProtExtractor extractor3 = new UniProtExtractor();

    public List<Entity> findRefSeqMentions(String text){
        final List<Entity> resultSet = new ArrayList<>();

        resultSet.addAll(extractor1.findRefSeqMentions(text));
        resultSet.addAll(extractor2.findRefSeqMentions(text));
        resultSet.addAll(extractor3.findRefSeqMentions(text));

        return resultSet;
    }

    public static void main(String[] args) {


        OverallExtractor refSeqExtractor = new OverallExtractor();

        List<Entity> entities = refSeqExtractor.findRefSeqMentions("Hallo NM_123 und AA12345 sind cool");

        for(Entity entity : entities){
            System.out.println(entity);
        }

    }

}

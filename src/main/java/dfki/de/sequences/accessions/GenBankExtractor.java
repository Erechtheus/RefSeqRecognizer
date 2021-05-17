package dfki.de.sequences.accessions;

import dfki.de.sequences.Accession;
import dfki.de.sequences.Entity;
import dfki.de.sequences.Types;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://community.gep.wustl.edu/repository/course_materials_WU/annotation/Genbank_Accessions.pdf
 */
public class GenBankExtractor {

    final private List<Pattern> patterns;


    public GenBankExtractor() {

        patterns = new ArrayList<>();
        //Nucleotides
        patterns.add(Pattern.compile("([a-zA-Z]\\d{5}|[a-zA-Z]{2}\\d{6}|[a-zA-Z]{2}\\d{8})")); //1 letter + 5 numerals or 2 letters + 6 numerals or 2 letters + 8 numerals

        //proteins
        patterns.add(Pattern.compile("([a-zA-Z]{3}\\d{5}|[a-zA-Z]{3}\\d{7})")); //3 letters + 5 numerals or 3 letters + 7 numerals

        //WGS
        patterns.add(Pattern.compile("([a-zA-Z]{4}(\\d{2}|\\d{6,}))")); //        4 letters + 2 numerals for WGS assembly version + 6 or more numerals or
        patterns.add(Pattern.compile("([a-zA-Z]{6}(\\d{2}|\\d{7,}))")); //        6 letters + 2 numerals for WGS assembly version + 7 or more numerals

    }

    public List<Entity> findRefSeqMentions(String text){

        List<Entity> matches = new ArrayList<>();

        for(Pattern pattern : patterns){
            final Matcher matcher = pattern.matcher(text);

            while(matcher.find()){
                matches.add(new Entity(matcher.start(), matcher.end(), new Accession("todo", new HashSet<Types>()))); //Accession has to be compiled
            }
        }

        return matches;
    }

    public static void main(String[] args) {


        GenBankExtractor refSeqExtractor = new GenBankExtractor();

        List<Entity> entities = refSeqExtractor.findRefSeqMentions("AA12345");

        for(Entity entity : entities){
            System.out.println(entity);
        }

    }
}

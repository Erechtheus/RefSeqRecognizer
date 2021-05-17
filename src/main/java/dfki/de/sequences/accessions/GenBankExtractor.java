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
    final private static String prefix="(^|[\\s\\(\\)\\[\\'\"/,])"; //>
    final private static String suffix="(?=([\\.,\\s\\)\\(\\]\\'\":;\\-/]|$))";//|:?[ATGC]>[ATGC]


    public GenBankExtractor() {

        patterns = new ArrayList<>();
        //Nucleotides
        patterns.add(Pattern.compile(prefix +"(?<match>[A-Z]\\d{5}|[A-Z]{2}\\d{6}|[A-Z]{2}\\d{8})" +suffix)); //1 letter + 5 numerals or 2 letters + 6 numerals or 2 letters + 8 numerals

        //proteins
        patterns.add(Pattern.compile(prefix +"(?<match>[A-Z]{3}\\d{5}|[A-Z]{3}\\d{7})" +suffix)); //3 letters + 5 numerals or 3 letters + 7 numerals

        //WGS
        patterns.add(Pattern.compile(prefix +"(?<match>[A-Z]{4}(\\d{2}|\\d{6,}))" +suffix)); //        4 letters + 2 numerals for WGS assembly version + 6 or more numerals or
        patterns.add(Pattern.compile(prefix +"(?<match>[A-Z]{6}(\\d{2}|\\d{7,}))" +suffix)); //        6 letters + 2 numerals for WGS assembly version + 7 or more numerals

    }

    public List<Entity> findRefSeqMentions(String text){

        List<Entity> matches = new ArrayList<>();

        for(Pattern pattern : patterns){
            final Matcher matcher = pattern.matcher(text);

            while(matcher.find()){
                matches.add(new Entity(matcher.start("match"), matcher.end("match"), new Accession("todo", new HashSet<Types>()))); //Accession has to be compiled
            }
        }

        return matches;
    }

    public static void main(String[] args) {


        String text = "(AA12345678)";
        GenBankExtractor refSeqExtractor = new GenBankExtractor();

        List<Entity> entities = refSeqExtractor.findRefSeqMentions(text);

        for(Entity entity : entities){
            System.out.println(text.substring(entity.getStart(), entity.getStop()));
        }

    }
}

package dfki.de.sequences.accessions;

import dfki.de.sequences.Accession;
import dfki.de.sequences.Entity;
import dfki.de.sequences.Types;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts GenBank,EMBL, DDBJ
 * https://community.gep.wustl.edu/repository/course_materials_WU/annotation/Genbank_Accessions.pdf
 * https://www.ncbi.nlm.nih.gov/genbank/acc_prefix/
 */
public class GenBankExtractor {

    final private List<Pattern> patterns;
    final private static String prefix="(^|[\\s\\(\\)\\[\\'\"/,])"; //>
    final private static String suffix="(?=([\\.,\\s\\)\\(\\]\\'\":;\\-/]|$))";//|:?[ATGC]>[ATGC]

    final private static String oneLetter ="[C|F|H|N|T|R|W|D|V|X|Y|Z|U|B|E|A|I|G|S|J|K|L|M|N]";
    final private static String twoLetter="(" +
            "BA|DF|DG|LD|" +
            "AN|" +
            "CH|CM|DS|EM|EN|EP|EQ|FA|GG|GL|JH|KB|KD|KE|KI|KK|KL|KN|KQ|KV|KZ|ML|MU|" +
            "AT|AU|AV|BB|BJ|BP|BW|BY|CI|CJ|DA|DB|DC|DK|FS|FY|HX|HY|LU|OH|" +
            "AA|AI|AW|BE|BF|BG|BI|BM|BQ|BU|CA|CB|CD|CF|CK|CN|CO|CV|CX|DN|DR|DT|DV|DW|DY|EB|EC|EE|EG|EH|EL|ES|EV|EW|EX|EY|FC|FD|FE|FF|FG|FK|FL|GD|GE|GH|GO|GR|GT|GW|HO|HS|JG|JK|JZ|" +
            "AB|LC|" +
            "AJ|AM|FM|FN|HE|HF|HG|FO|LK|LL|LM|LN|LO|LR|LS|LT|OA|OB|OC|OD|OE|OU|OV|OW|OX|OY|OZ|" +
            "AF|AY|DQ|EF|EU|FJ|GQ|GU|HM|HQ|JF|JN|JQ|JX|KC|KF|KJ|KM|KP|KR|KT|KU|KX|KY|MF|MG|MH|MK|MN|MT|MW|MZ|" +
            "AP|" +
            "BS|" +
            "AL|BX|CR|CT|CU|FP|FQ|FR|" +
            "AE|CP|CY|" +
            "AG|DE|DH|FT|GA|LB|" +
            "AQ|AZ|BH|BZ|CC|CE|CG|CL|CW|CZ|DU|DX|ED|EI|EJ|EK|ER|ET|FH|FI|GS|HN|HR|JJ|JM|JS|JY|KG|KO|KS|MJ|" +
            "AK|" +
            "AC|DP|" +
            "BD|DD|DI|DJ|DL|DM|FU|FV|FW|FZ|GB|HV|HW|HZ|LF|LG|LV|LX|LY|LZ|MA|MB|MC|MD|ME|OF|OG|" +
            "AX|CQ|CS|FB|GM|GN|HA|HB|HC|HD|HH|HI|JA|JB|JC|JD|JE|LP|LQ|MP|MQ|MR|MS|" +
            "AR|DZ|EA|GC|GP|GV|GX|GY|GZ|HJ|HK|HL|KH|MI|MM|MO|MV|MX|MY|" +
            "BV|GF|" +
            "BR|" +
            "BN|" +
            "BK|" +
            "HT|HU|" +
            "BL|GJ|GK|" +
            "EZ|HP|JI|JL|JO|JP|JR|JT|JU|JV|JW|KA|" +
            "FX|LA|LE|LH|LI|LJ |" +
            "AD|" +
            "AH|" +
            "AS|" +
            "BC|" +
            "BT" +
            ")";

    final private static String threeLetter= "(" +
            "B[A-Z]{2}|" +
            "C[A-Z]{2}|S[A-Z]{2}|V[A-Z]{2}|" +
            "A[A-Z]{2}|Q[A-Z]{2}|" +
            "AAE|" +
            "F[A-Z]{2}|" +
            "D[A-Z]{2}|" +
            "G[A-Z]{2}|" +
            "E[A-Z]{2}|K[A-Z]{2}|O[A-Z]{2}|P[A-Z]{2}|R[A-Z]{2}|T[A-Z]{2}|" +
            "H[A-Z]{2}|" +
            "I[A-Z]{2}|" +
            "L[A-Z]{2}|" +
            "J[A-Z]{2}|" +
            "M[A-Z]{2}|N[A-Z]{2}" +
            ")";

    final private static String fourLetter="(" +
            "A[A-Z]{3}|J[A-Z]{3}|L[A-Z]{3}|M[A-Z]{3}|N[A-Z]{3}|P[A-Z]{3}|Q[A-Z]{3}|R[A-Z]{3}|S[A-Z]{3}|V[A-Z]{3}|W[A-Z]{3}|X[A-Z]{3}|" +
            "B[A-Z]{3}|" +
            "C[A-Z]{3}|F[A-Z]{3}|O[A-Z]{3}|U[A-Z]{3}|" +
            "D[A-Z]{3}|" +
            "E[A-Z]{3}|" +
            "G[A-Z]{3}|" +
            "H[A-Z]{3}|" +
            "I[A-Z]{3}|" +
            "T[A-Z]{3}|" +
            "K[A-Z]{3}|" +
            "Y[A-Z]{3}|" +
            "Z[A-Z]{3}" +
            ")";


    final private static String sixLetter="(" +
            "A[A-Z]{5}|J[A-Z]{5}|" +
            "B[A-Z]{5}|" +
            "C[A-Z]{5}|" +
            "D[A-Z]{5}" +
            ")";

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

package com.venkat.resumeparser.parser.opennlp;

import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;
import static java.lang.System.out;

public class OpenNLPTraindata {

    static final String[] SENTENCES = {"Venkatram was taller than Ram. ",
            "However, Mr. Srijan was taller than both of them. ",
            "The same could be said for Shamanthaka and Anitha. ",
            "Srijan Veerareddy was the tallest."};

    public static void main(String[] args) {

        out.println("---OpenNLP NER Training Example---");
        TokenNameFinderModel tokenNameFinderModel = getTrainingModel(getTrainingText());
        testModel(tokenNameFinderModel);
    }

    private static String getTrainingText() {

        String names[] = {"Bill", "Sue", "Mary Anne", "John Henry", "Patty",
                "Jones", "Smith", "Albertson", "Henry", "Robertson"};
        String prefixes[] = {"", "Mr. ", "", "Dr. ", "", "Mrs. ", "", "Ms. "};

        Random nameIndex = new Random();
        Random prefixIndex = new Random();
        StringBuilder sentences = new StringBuilder();
        for (int i = 0; i < 15000; i++) {
            String line;
            line = "<START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)]
                    + " <END> is <START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)] + " <END> cousin.\n ";

            sentences.append(line);

            line = "Also, <START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)]
                    + " <END> is a parent.\n ";
            sentences.append(line);

            line = "It turns out that <START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)]
                    + " <END> and <START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)] + " <END> are not related.\n ";
            sentences.append(line);

            line = "<START:PERSON> "
                    + prefixes[prefixIndex.nextInt(prefixes.length)]
                    + names[nameIndex.nextInt(names.length)]
                    + " <END> comes from Canada.\n ";
            sentences.append(line);

        }
        return sentences.toString();
    }

    private static TokenNameFinderModel getTrainingModel(final String trainText) {

        InputStreamFactory inputStreamfactory
                = () -> new ByteArrayInputStream(trainText.getBytes());
        try (ObjectStream<String> lineStream = new PlainTextByLineStream(
                inputStreamfactory, Charset.forName("UTF-8"));) {

            ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

            return NameFinderME.train("en", "person", sampleStream,
                    TrainingParameters.defaultParams(), new TokenNameFinderFactory());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void testModel(TokenNameFinderModel tokenNameFinderModel) {
        try (InputStream tokenModelStream = new FileInputStream(
                new File("en-token.bin"));) {

            TokenizerModel tokenizerModel = new TokenizerModel(tokenModelStream);
            Tokenizer tokenizer = new TokenizerME(tokenizerModel);

            NameFinderME nameFinder = new NameFinderME(tokenNameFinderModel);

            for (String sentence : SENTENCES) {
                out.println("Sentence: [" + sentence + "]");
                String tokens[] = tokenizer.tokenize(sentence);
                Span nameSpans[] = nameFinder.find(tokens);
                double[] spanProbabilities = nameFinder.probs(nameSpans);

                for (int i = 0; i < nameSpans.length; i++) {
                    out.println("Span: " + nameSpans[i].toString());
                    if ((nameSpans[i].getEnd() - nameSpans[i].getStart()) == 1) {
                        out.println("Entity: "
                                + tokens[nameSpans[i].getStart()]);
                    } else {
                        out.println("Entity: "
                                + tokens[nameSpans[i].getStart()] + " "
                                + tokens[nameSpans[i].getEnd() - 1]);
                    }
                    out.println("Probability: " + spanProbabilities[i]);
                }
                out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

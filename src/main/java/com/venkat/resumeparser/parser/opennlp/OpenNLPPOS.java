package com.venkat.resumeparser.parser.opennlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OpenNLPPOS {

    public static void main(String[] args) {
        usingOpenNLPPOSModel();
    }

    private static final String[] SENTENCE = "The cow jumped over the moon.".split(" ");

    private static void usingOpenNLPPOSModel() {
        System.out.println("OpenNLP POSModel Examples");
        System.out.println();

        try (InputStream modelIn = new FileInputStream(
                new File("en-pos-maxent.bin"));) {
            POSModel posModel = new POSModel(modelIn);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);

            String tags[] = posTaggerME.tag(SENTENCE);

            for (int i = 0; i < SENTENCE.length; i++) {
                System.out.print(SENTENCE[i] + "/" + tags[i] + " ");
            }
            System.out.println();
            System.out.println("\nTop Sequences");
            Sequence topSequences[] = posTaggerME.topKSequences(SENTENCE);
            for (Sequence topSequence : topSequences) {
                System.out.println(topSequence);
            }
            System.out.println();

            System.out.println("Probabilities");
            for (Sequence topSequence : topSequences) {
                List<String> outcomes = topSequence.getOutcomes();
                double[] probabilities = topSequence.getProbs();
                for (int j = 0; j < outcomes.size(); j++) {
                    System.out.printf("%s/%5.3f ", outcomes.get(j), probabilities[j]);
                }
                System.out.println();
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

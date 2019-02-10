package com.venkat.resumeparser.parser.opennlp;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

import java.io.*;

public class SentimentAnalysis {

    public static void main(String[] args) {
        trainSentimentAnalysisClassifier();
        useSentimentAnalysisClassifier();
    }

    // Sentiment Analysis Example
    private static final String[] CATEGORIES = {"neg","pos"};
    private static final int NGRAMSIZE = 6;
    private static final DynamicLMClassifier<NGramProcessLM> classifier =
            DynamicLMClassifier.createNGramProcess(CATEGORIES, NGRAMSIZE);

    private static void trainSentimentAnalysisClassifier() {
        File trainingDirectory = new File("txt_sentoken");
        System.out.println("\nTraining.");
        for (int i = 0; i < CATEGORIES.length; ++i) {
            Classification classification
                    = new Classification(CATEGORIES[i]);
            File file = new File(trainingDirectory, CATEGORIES[i]);
            File[] trainingFiles = file.listFiles();
            for (int j = 0; j < trainingFiles.length; ++j) {
                try {
                    String review = Files.readFromFile(trainingFiles[j], "ISO-8859-1");
                    Classified<CharSequence> classified =
                            new Classified<>((CharSequence) review, classification);
                    classifier.handle(classified);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void useSentimentAnalysisClassifier() {
        String review = null;
        try (FileReader fr = new FileReader("review.txt");
             BufferedReader br = new BufferedReader(fr);) {
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
            review = sb.toString();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Text: " + review);
        Classification classification
                = classifier.classify(review);
        String bestCategory = classification.bestCategory();
        System.out.println("Best Category: " + bestCategory);
    }
}

package com.venkat.resumeparser.parser.opennlp;

import opennlp.tools.postag.*;
import opennlp.tools.util.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import static java.lang.System.out;

public class OpenNLPPOSTraining {

    public static void main(String[] args) {

        out.println("Training Example");
        trainOpenNLPPOSModel();

    }

    private static final String[] SENTENCE = "The cow jumped over the moon.".split(" ");


    private static void trainOpenNLPPOSModel() {
        try {
            InputStream inputData = new FileInputStream("sample.train");
            StringBuilder stringBuilder = new StringBuilder();
            while (inputData.available() != 0) {
                stringBuilder.append((char) inputData.read());
            }
            final String trainingText = stringBuilder.toString();

            InputStreamFactory inputStreamfactory
                    = () -> new ByteArrayInputStream(trainingText.getBytes());
            ObjectStream<String> lineStream = new PlainTextByLineStream(
                    inputStreamfactory, Charset.forName("UTF-8"));
            ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

            POSModel model = POSTaggerME.train("en", sampleStream,
                    TrainingParameters.defaultParams(), new POSTaggerFactory());
            POSTaggerME posTaggerME = new POSTaggerME(model);

            String tags[] = posTaggerME.tag(SENTENCE);

            System.out.println();
            for (int i = 0; i < SENTENCE.length; i++) {
                System.out.print(SENTENCE[i] + "_" + tags[i] + " ");
            }
            System.out.println();
            System.out.println();
            System.out.println("Top Sequences");
            Sequence topSequences[] = posTaggerME.topKSequences(SENTENCE);
            for (Sequence topSequence : topSequences) {
                System.out.println(topSequence);
            }
            System.out.println();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

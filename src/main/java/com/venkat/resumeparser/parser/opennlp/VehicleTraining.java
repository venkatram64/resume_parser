package com.venkat.resumeparser.parser.opennlp;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.*;
import java.util.Random;

import static java.lang.System.out;

public class VehicleTraining {

    public static void main(String[] args) {
        generateTrainingData();
        trainingOpenNLPClassificationModel();
        useOpenNLPClassificationModel();
    }


    private static void generateTrainingData() {
        out.println("--- Generate Training Data ---");

        try (OutputStream trainingDataInputStream = new FileOutputStream("en-vehicle.train");
             BufferedOutputStream out = new BufferedOutputStream(trainingDataInputStream)) {
            String engines[] = {"", "V8", "V6", "4"};
            String vins[] = {"", "v1203", "v0093", "v11", "v203"};
            String available[] = {"", "na"};

            Random makeRNG = new Random();
            Random engineRNG = new Random();
            Random vinRNG = new Random();
            Random naRNG = new Random();

            for (int i = 0; i < 50000; i++) {
                switch (makeRNG.nextInt(3)) {
                    case 0:
                        out.write(("100 "
                                + available[naRNG.nextInt(available.length)] + " Ford "
                                + available[naRNG.nextInt(available.length)] + " "
                                + engines[engineRNG.nextInt(engines.length)] + " "
                                + vins[vinRNG.nextInt(vins.length)]).getBytes());
                        break;
                    case 1:
                        out.write(("200 "
                                + available[naRNG.nextInt(available.length)] + " Toyota "
                                + available[naRNG.nextInt(available.length)] + " "
                                + engines[engineRNG.nextInt(engines.length)] + " "
                                + vins[vinRNG.nextInt(vins.length)]).getBytes());
                        break;
                    case 2:
                        out.write(("300 "
                                + available[naRNG.nextInt(available.length)] + " Honda "
                                + available[naRNG.nextInt(available.length)] + " "
                                + engines[engineRNG.nextInt(engines.length)] + " "
                                + vins[vinRNG.nextInt(vins.length)]).getBytes());
                        break;
                }
                out.write((engines[engineRNG.nextInt(engines.length)] + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void trainingOpenNLPClassificationModel() {
        out.println("--- Train Model ---");

        try (InputStream trainingDataInputStream = new FileInputStream("en-vehicle.train");
             OutputStream modelOutStream = new FileOutputStream("en-vehicle.model");) {
            ObjectStream<String> lineStream
                    = new PlainTextByLineStream((InputStreamFactory) trainingDataInputStream, "UTF-8");
            ObjectStream<DocumentSample> documentSampleStream = new DocumentSampleStream(lineStream);
            DoccatModel model = null;//DocumentCategorizerME.train("en", documentSampleStream);

            // Save the model
            OutputStream modelOut = new BufferedOutputStream(modelOutStream);
            model.serialize(modelOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void useOpenNLPClassificationModel() {
        out.println("--- Use Model ---");

        try (InputStream modelInputStream = new FileInputStream(
                new File("en-vehicle.model"));) {
            DoccatModel model = new DoccatModel(modelInputStream);
            DocumentCategorizerME categorizer = new DocumentCategorizerME(model);

            double[] outcomes = {};//categorizer.categorize("Toyota v234 V6 corolla ");

            out.println("--- Categories ---");
            for (int i = 0; i < categorizer.getNumberOfCategories(); i++) {
                out.println("Category: " + categorizer.getCategory(i) + " - " + outcomes[i]);
            }
            out.println("\nBest Category: " + categorizer.getBestCategory(outcomes));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

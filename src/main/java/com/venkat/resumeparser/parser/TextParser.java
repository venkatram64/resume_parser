package com.venkat.resumeparser.parser;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

public class TextParser {

    public static void tokenize(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, This is Venkatram Veerareddy";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList){
            System.out.println(coreLabel.originalText());
        }
    }

    public static void readSentence(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, This is Venkatram Veerareddy. I am Software engineer.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreSentence> coreSentences = coreDocument.sentences();
        for (CoreSentence coreSentence : coreSentences){
            System.out.println(coreSentence.toString());
        }
    }

    public static void posRecognizer(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, This is Venkatram Veerareddy";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList){
            String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            System.out.println(coreLabel.originalText() + "=" + pos);
        }
    }

    public static void lemmaRecognizer(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, I am planning to learn data science technologies.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList){
            String lemma = coreLabel.lemma();
            System.out.println(coreLabel.originalText() + "=" + lemma);
        }
    }

    public static void nerRecognizer(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, This is Venkatram Veerareddy.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList){
            String ner = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            System.out.println(coreLabel.originalText() + "=" + ner);
        }
    }

    public static void sentimentAnalysis(){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hi, this is Richard. This is too bad to watch this movie.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreSentence> sentences = coreDocument.sentences();
        for (CoreSentence sentence : sentences){
            String sentiment = sentence.sentiment();
            System.out.println(sentiment + "\t" + sentiment);
        }
    }



    public static void main(String[] args) {
        //tokenize();
        //readSentence();
        //posRecognizer();
        //lemmaRecognizer();
        //nerRecognizer();
        sentimentAnalysis();
    }
}

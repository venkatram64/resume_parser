package com.venkat.resumeparser.controller;

import com.venkat.resumeparser.model.Type;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/api/v1")
public class ParserController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    @PostMapping
    @RequestMapping(value = "/ner")
    public Set<String> ner(@RequestBody final String input, @RequestParam final Type type){

        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        if(type.getName().equalsIgnoreCase("PhoneNumber")){
            isPhoneNumber("input");
            Set<String> set = new HashSet<>();
            set.add(input);
            return set;
        }
        return new HashSet<>(collectList(coreLabels, type));
    }

    private List<String> collectList(List<CoreLabel> coreLabels, final Type type){
        return coreLabels
                .stream().
                filter(coreLabel -> type.getName()
                        .equalsIgnoreCase(coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel :: originalText)
                .collect(Collectors.toList());
    }

    private static boolean isPhoneNumber(String phoneNumber) {

        if (phoneNumber.matches("\\([+]\\d{2}\\)\\s+[0-9]{10}")) {
            return true;
        }
        if (phoneNumber.matches("[+]\\d{2}[-]\\s+[0-9]{10}")) {
            return true;
        }
        if (phoneNumber.matches("[0-9]{10}")) {
            return true;
        }
        if (phoneNumber.matches("\\d{3}\\s\\d{3}\\s\\d{4}")) {
            return true;
        }if (phoneNumber.matches("\\d{3}[-]\\d{3}[-]\\d{4}")) {
            return true;
        } if (phoneNumber.matches("\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
            return true;
        }

        return false;

    }
}

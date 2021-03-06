package org.intermine.configurator.source.project;

import io.swagger.model.DataFile;
import io.swagger.model.DataFileProperties;
import io.swagger.model.DataFilePropertiesAnswerOption;
import io.swagger.model.DataFilePropertiesQuestion;
import io.swagger.model.Organism;
;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastaSource implements AbstractSource {

    Map<String, String> selectedAnswers = new HashMap<>();
    DataFileProperties dataFileProperties;


    public FastaSource() {
        // constructor
    }

    public String getProjectXML(DataFileProperties dataFileProperties, String fileLocation) {
        if (dataFileProperties == null) {
            return null;
        }

        List<DataFilePropertiesQuestion> questions = dataFileProperties.getQuestions();
        for (DataFilePropertiesQuestion question : questions) {
            List<DataFilePropertiesAnswerOption> possibleAnswers = question.getPossibleAnswers();
            for (DataFilePropertiesAnswerOption possibleAnswer : possibleAnswers) {
                if (possibleAnswer.isIsSelected()) {
                    selectedAnswers.put(question.getQuestionId(), possibleAnswer.getAnswerId());
                }
            }
        }

        DataFile dataFile = dataFileProperties.getDataFile();
        Organism organism = dataFile.getOrganism();
        String taxonId = organism.getTaxonID().toString();
        String dataType = "org.intermine.model.bio." + selectedAnswers.get("featureType");
        String classAttribute = selectedAnswers.get("identifierType");
        if (classAttribute == null || dataType == null) {
            return null;
        }

        String snippet = " <source name=\"fasta\" type=\"fasta\" >"
                + "<property name=\"fasta.taxonId\" value=\"" + taxonId + "\"/>"
                + "<property name=\"fasta.dataSetTitle\" value=\"FASTA data set\"/>"
                + "<property name=\"fasta.dataSourceName\" value=\"InterMine import\"/>"
                // "org.intermine.model.bio.Gene"
                + "<property name=\"fasta.className\" value=\"" + dataType + "\"/>"
                + "<property name=\"fasta.classAttribute\" value=\"" + classAttribute + "\"/>"
                + "<property name=\"\"src.data.dir\" location=\"" + fileLocation + "\"/>"
                + "</source>\n";
        return snippet;
    }

    public String getDataModel() {
        return null;
    }


}

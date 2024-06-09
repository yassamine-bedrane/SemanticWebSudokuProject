package org.example;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SudokuSolver {

    private final Cell[][] grid;
    private final String ontologyTemplateContent;

    public SudokuSolver(Cell[][] grid){
        this.grid = grid;
        ClassLoader classLoader = EntryPoint.class.getClassLoader();
        String filepath = Objects.requireNonNull(classLoader.getResource("6x6-f.owl")).getFile();
        this.ontologyTemplateContent = readFileToString(filepath);

    }

    private String generateSameAsAxioms() {

        StringBuilder sameAsAxioms = new StringBuilder();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                if (cell.isManuallySet) {
                    sameAsAxioms.append(cell.generateSameAsAxiom()).append("\n");
                }
            }
        }
        return sameAsAxioms.toString();
    }

    private OWLOntology generateOntology(String content) throws OWLOntologyCreationException {
        InputStream inputStream = createInputStreamFromString(this.ontologyTemplateContent.replace(Config.REPLACEMENT_TEMPLATE_STRING,content));
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        return m.loadOntologyFromOntologyDocument(inputStream);
    }


    public void solve(ArrayList<String> manuallySetCellsLabels) throws OWLOntologyCreationException {


        String content = this.generateSameAsAxioms();
        OWLOntology ontology = this.generateOntology(content);

        try{
            Reasoner hermit =new Reasoner(ontology);

            hermit.precomputeInferences(InferenceType.CLASS_HIERARCHY);
            hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
            hermit.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
            hermit.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
            hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
            hermit.precomputeInferences(InferenceType.SAME_INDIVIDUAL);

            printInferredAssertions(hermit, ontology,manuallySetCellsLabels);


        }catch (Exception error){
            JOptionPane.showMessageDialog(this.grid[0][0].getParent(), error.getMessage(), "Inconsistency", JOptionPane.ERROR_MESSAGE);

        }



    }
    private  String readFileToString(String filepath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return stringBuilder.toString();
    }

    public boolean isStringInArrayList(ArrayList<String> arrayList, String searchString) {
        for (String str : arrayList) {
            if (str.equals(searchString)) {
                return true;
            }
        }
        return false;
    }
    public  String getLabelFromURI(String input) {
        int startIndex = input.indexOf('#');
        int endIndex = input.indexOf('>');
        return input.substring(startIndex + 1, endIndex);
    }


    public int getNumberFromURI(String input){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }

    public  int[] getCoordinatesFromLabel(String label)
    {
        String[] parts = label.split("_");

        List<Integer> numbersList = new ArrayList<>();
        for (String part : parts) {
            String numericPart = part.replaceAll("[^0-9]", "");
            for (char c : numericPart.toCharArray()) {
                numbersList.add(Character.getNumericValue(c));
            }
        }
        int[] numbers = new int[numbersList.size()];
        for (int i = 0; i < numbersList.size(); i++) {
            numbers[i] = numbersList.get(i);
        }
        return numbers;
    }

    private  InputStream createInputStreamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }


    private  void printInferredAssertions(OWLReasoner reasoner, OWLOntology ontology, ArrayList<String> manuallySetCellsLabels) {
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            String uri = individual.toString();
            String labelOfIndividual = getLabelFromURI(uri);
            if(uri.contains("http://projet.org#c")&&!isStringInArrayList(manuallySetCellsLabels,labelOfIndividual)  ){
                int[] relativeCoordinates = this.getCoordinatesFromLabel(labelOfIndividual);
                int[] absoluteCoordinates = SudokuUtils.getIndexesInOriginalMatrix(relativeCoordinates[0],relativeCoordinates[1],relativeCoordinates[2]);
                Cell currentCell = this.grid[absoluteCoordinates[0]-1][absoluteCoordinates[1]-1];
                currentCell.setNumber(-1);
                Node<OWLNamedIndividual> sameIndividualsNode = reasoner.getSameIndividuals(individual);
                for (OWLNamedIndividual sameIndividual : sameIndividualsNode.getEntities()) {
                    if (!sameIndividual.equals(individual) && !sameIndividual.toString().contains("http://projet.org#c") ) {
                        int number = this.getNumberFromURI(sameIndividual.toString());
                        currentCell.setNumber(number);
                        break;
                    }
                }
            }
        }
    }
}
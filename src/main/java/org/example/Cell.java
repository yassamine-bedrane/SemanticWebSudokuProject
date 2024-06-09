package org.example;


import javax.swing.*;


public class Cell  extends JTextField  {

    int block;
    int row;
    int column;
    int number = 0;
    boolean isManuallySet = false;

    public Cell(){

    }

    public Cell(int block,int row,int column ){
        super();
        this.block = block;
        this.row = row;
        this.column = column;

        this.addKeyListener(new CellKeyListener(this));

    }


    public int getNumber(){
        return this.number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public boolean isCellSet() {
        return this.number!=0;
    }

    public void setManuallySet(boolean manuallySet) {
        isManuallySet = manuallySet;
    }

    public String getLabel(){
        return "c"+block+"_"+row+""+column;
    }

    public String generateSameAsAxiom(){
        return String.format("""
                <SameIndividual>
                    <NamedIndividual IRI="#%s"/>
                    <NamedIndividual abbreviatedIRI=":%s"/>
                </SameIndividual>
                """,this.number,this.getLabel());
    }


}

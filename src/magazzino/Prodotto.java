/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzino;

/**
 *
 * @author taboada.taddeo
 */
public class Prodotto {
    // Attributi
    private int id; 
    private String nome;
    private int prezzoA;
    private int prezzoV;
    private int scorta;
    private int scortaMin;
    private int numVendite;

    public Prodotto(int id, String nome, int prezzoA, int prezzoV, int scorta, int scortaMin) {
        this.id = id;
        this.nome = nome;
        this.prezzoA = prezzoA;
        this.prezzoV = prezzoV;
        this.scorta = scorta;
        this.scortaMin = scortaMin;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPrezzoA() {
        return prezzoA;
    }

    public int getPrezzoV() {
        return prezzoV;
    }

    public int getScorta() {
        return scorta;
    }

    public int getScortaMin() {
        return scortaMin;
    }

    public int getNumVendite() {
        return numVendite;
    }

    public void setNumVendite(int numVendite) {
        this.numVendite = numVendite;
    }
    
}

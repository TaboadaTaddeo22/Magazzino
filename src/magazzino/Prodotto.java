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
    private float prezzoA;
    private float prezzoV;
    private int scorta;
    private int scortaMin;
    private int numVendite;

    public Prodotto(int id, String nome, float prezzoA, float prezzoV, int scorta, int scortaMin) {
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

    public float getPrezzoA() {
        return prezzoA;
    }

    public float getPrezzoV() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzoA(float prezzoA) {
        this.prezzoA = prezzoA;
    }

    public void setPrezzoV(float prezzoV) {
        this.prezzoV = prezzoV;
    }

    public void setScortaMin(int scortaMin) {
        this.scortaMin = scortaMin;
    }
    
    public void setScorta(int scorta) {
        this.scorta = scorta;
    }

    public void setNumVendite(int numVendite) {
        this.numVendite = numVendite;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzino;

import java.util.*;

/**
 *
 * @author taboada.taddeo
 */
public class RaccoltaProdotti {
    private ArrayList<Prodotto> listaProdotti;
    private HashSet<Integer> insiemeId;
    private HashMap<Integer, Prodotto> mappaProdotti;

    /**
     * Costruttore di RaccoltaProdotti
     */
    public RaccoltaProdotti() {
        listaProdotti = new ArrayList<>();
        insiemeId = new HashSet<>();
        mappaProdotti = new HashMap<>();
    }

    /**
     * Metodo che aggiunge un prodotto
     * @param p il prodotto da aggiungere
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean aggiungiProdotto(Prodotto p) {
        if (p == null || insiemeId.contains(p.getId())) {
            return false;
        }
        listaProdotti.add(p);
        insiemeId.add(p.getId());
        mappaProdotti.put(p.getId(), p);
        return true;
    }

    /**
     * Metodo che rimuove un prodotto usando la matricola
     * @param id l'id del prodotto da rimuovere
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean eliminaProdotto(int id) {
        if (!insiemeId.contains(id)) {
            return false;
        }
        Prodotto s = mappaProdotti.remove(id);
        insiemeId.remove(id);
        listaProdotti.remove(s);
        return true;
    }
    
    /**
     * Metodo che rimuove tutti i prodotti dalla raccolta
     */
    public void svuota() {
        listaProdotti.clear();
        insiemeId.clear();
        mappaProdotti.clear();
    }

    /**
     * Metodo che restituisce un prodotto
     * @param id l'id del Prodotto
     * @return il Prodotto
     */
    public Prodotto cercaProdotto(int id) {
        return mappaProdotti.get(id);
    }

    /**
     * Metodo get di listaProdotti che la restituisce sottoforma di Collection
     * @return listaProdotti
     */
    public Collection<Prodotto> getTuttiProdotti() {
        return Collections.unmodifiableList(listaProdotti);
    }

    /**
     * Metodo che restituisce il numero di prodotti
     * @return la dimensione di listaProdotti
     */
    public int numeroProdotti() {
        return listaProdotti.size();
    }

    /**
     * Metodo get di listaProdotti
     * @return listaProdotti
     */
    public ArrayList<Prodotto> getListaProdotti() {
        return listaProdotti;
    }

    /**
     * Metodo get di insiemeId
     * @return insiemeId
     */
    public HashSet<Integer> getInsiemeId() {
        return insiemeId;
    }

    /**
     * Metodo get di mappaProdotti
     * @return mappaProdotti
     */
    public HashMap<Integer, Prodotto> getMappaProdotti() {
        return mappaProdotti;
    }

}

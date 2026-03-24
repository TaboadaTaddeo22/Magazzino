/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package magazzino;

import java.util.Collection;

/**
 *
 * @author taboada.taddeo
 */
public class Statistiche extends javax.swing.JDialog {
     private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Statistiche.class.getName());
    
    private RaccoltaProdotti rP;

    /**
     * Creates new form Statistiche
     */
    public Statistiche(java.awt.Frame parent, boolean modal, RaccoltaProdotti rP) {
        super(parent, modal);
        this.rP = rP;
        initComponents();
        inizializzaLista();
    }

    /**
     * Popola la JList con le categorie statistiche disponibili e aggiunge
     * il listener per aggiornare la TextArea alla selezione.
     */
    private void inizializzaLista() {
        String[] categorie = {
            "Prodotto più venduto",
            "Prodotto meno venduto",
            "Prodotto con più scorta",
            "Prodotto con meno scorta",
            "Prodotto più costoso (vendita)",
            "Prodotto meno costoso (vendita)",
            "Prodotto più costoso (acquisto)",
            "Prodotto meno costoso (acquisto)",
            "Prodotti sotto scorta minima"
        };
        LSTSelezioneProdotto.setListData(categorie);

        LSTSelezioneProdotto.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selezione = LSTSelezioneProdotto.getSelectedValue();
                if (selezione != null) {
                    aggiornaStatistica(selezione);
                }
            }
        });
    }

    /**
     * Calcola e mostra nella TextArea la statistica corrispondente alla
     * categoria selezionata.
     *
     * @param categoria la voce selezionata dalla JList
     */
    private void aggiornaStatistica(String categoria) {
        Collection<Prodotto> prodotti = rP.getTuttiProdotti();

        if (prodotti.isEmpty()) {
            TXTStatistiche.setText("Nessun prodotto presente in magazzino.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        switch (categoria) {

            case "Prodotto più venduto": {
                Prodotto best = null;
                for (Prodotto p : prodotti) {
                    if (best == null || p.getNumVendite() > best.getNumVendite()) {
                        best = p;
                    }
                }
                sb.append("=== Prodotto più venduto ===\n\n");
                sb.append(dettagliProdotto(best));
                break;
            }

            case "Prodotto meno venduto": {
                Prodotto worst = null;
                for (Prodotto p : prodotti) {
                    if (worst == null || p.getNumVendite() < worst.getNumVendite()) {
                        worst = p;
                    }
                }
                sb.append("=== Prodotto meno venduto ===\n\n");
                sb.append(dettagliProdotto(worst));
                break;
            }

            case "Prodotto con più scorta": {
                Prodotto max = null;
                for (Prodotto p : prodotti) {
                    if (max == null || p.getScorta() > max.getScorta()) {
                        max = p;
                    }
                }
                sb.append("=== Prodotto con più scorta ===\n\n");
                sb.append(dettagliProdotto(max));
                break;
            }

            case "Prodotto con meno scorta": {
                Prodotto min = null;
                for (Prodotto p : prodotti) {
                    if (min == null || p.getScorta() < min.getScorta()) {
                        min = p;
                    }
                }
                sb.append("=== Prodotto con meno scorta ===\n\n");
                sb.append(dettagliProdotto(min));
                break;
            }

            case "Prodotto più costoso (vendita)": {
                Prodotto max = null;
                for (Prodotto p : prodotti) {
                    if (max == null || p.getPrezzoV() > max.getPrezzoV()) {
                        max = p;
                    }
                }
                sb.append("=== Prodotto più costoso (vendita) ===\n\n");
                sb.append(dettagliProdotto(max));
                break;
            }

            case "Prodotto meno costoso (vendita)": {
                Prodotto min = null;
                for (Prodotto p : prodotti) {
                    if (min == null || p.getPrezzoV() < min.getPrezzoV()) {
                        min = p;
                    }
                }
                sb.append("=== Prodotto meno costoso (vendita) ===\n\n");
                sb.append(dettagliProdotto(min));
                break;
            }

            case "Prodotto più costoso (acquisto)": {
                Prodotto max = null;
                for (Prodotto p : prodotti) {
                    if (max == null || p.getPrezzoA() > max.getPrezzoA()) {
                        max = p;
                    }
                }
                sb.append("=== Prodotto più costoso (acquisto) ===\n\n");
                sb.append(dettagliProdotto(max));
                break;
            }

            case "Prodotto meno costoso (acquisto)": {
                Prodotto min = null;
                for (Prodotto p : prodotti) {
                    if (min == null || p.getPrezzoA() < min.getPrezzoA()) {
                        min = p;
                    }
                }
                sb.append("=== Prodotto meno costoso (acquisto) ===\n\n");
                sb.append(dettagliProdotto(min));
                break;
            }

            case "Prodotti sotto scorta minima": {
                sb.append("=== Prodotti sotto scorta minima ===\n\n");
                boolean trovati = false;
                for (Prodotto p : prodotti) {
                    if (p.getScorta() < p.getScortaMin()) {
                        sb.append(dettagliProdotto(p));
                        sb.append("----------------------------\n");
                        trovati = true;
                    }
                }
                if (!trovati) {
                    sb.append("Nessun prodotto è sotto la scorta minima.");
                }
                break;
            }

            default:
                sb.append("Categoria non riconosciuta.");
                break;
        }

        TXTStatistiche.setText(sb.toString());
        TXTStatistiche.setCaretPosition(0);
    }

    /**
     * Restituisce una stringa formattata con i dettagli di un prodotto.
     *
     * @param p il prodotto
     * @return stringa con i dettagli
     */
    private String dettagliProdotto(Prodotto p) {
        if (p == null) return "Nessun prodotto trovato.\n";
        return  "ID:              " + p.getId()          + "\n" +
                "Nome:            " + p.getNome()         + "\n" +
                "Prezzo acquisto: " + p.getPrezzoA()      + " €\n" +
                "Prezzo vendita:  " + p.getPrezzoV()      + " €\n" +
                "Scorta:          " + p.getScorta()       + "\n" +
                "Scorta minima:   " + p.getScortaMin()    + "\n" +
                "Prodotti venduti:" + p.getNumVendite()   + "\n";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTitolo = new javax.swing.JPanel();
        lblTitolo = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        PNLSelezioneStatistica = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LSTSelezioneProdotto = new javax.swing.JList<>();
        PNLStatisticheProdotti = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TXTStatistiche = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Statistiche Magazzino");
        setPreferredSize(new java.awt.Dimension(900, 525));

        pnlTitolo.setBackground(new java.awt.Color(0, 77, 51));
        pnlTitolo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlTitolo.setLayout(new java.awt.BorderLayout());

        lblTitolo.setFont(new java.awt.Font("Georgia Pro", 1, 48)); // NOI18N
        lblTitolo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitolo.setText("Statistiche Magazzino");
        pnlTitolo.add(lblTitolo, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlTitolo, java.awt.BorderLayout.PAGE_START);

        pnlCentro.setLayout(new java.awt.BorderLayout());

        PNLSelezioneStatistica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selezione statistica", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N
        PNLSelezioneStatistica.setPreferredSize(new java.awt.Dimension(300, 376));
        PNLSelezioneStatistica.setLayout(new java.awt.GridLayout(1, 0));

        LSTSelezioneProdotto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jScrollPane1.setViewportView(LSTSelezioneProdotto);

        PNLSelezioneStatistica.add(jScrollPane1);

        pnlCentro.add(PNLSelezioneStatistica, java.awt.BorderLayout.LINE_START);

        PNLStatisticheProdotti.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistiche prodotti", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N
        PNLStatisticheProdotti.setEnabled(false);
        PNLStatisticheProdotti.setLayout(new java.awt.GridLayout(1, 0));

        TXTStatistiche.setColumns(20);
        TXTStatistiche.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TXTStatistiche.setRows(5);
        TXTStatistiche.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TXTStatistiche.setEnabled(false);
        jScrollPane3.setViewportView(TXTStatistiche);

        PNLStatisticheProdotti.add(jScrollPane3);

        pnlCentro.add(PNLStatisticheProdotti, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCentro, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> LSTSelezioneProdotto;
    private javax.swing.JPanel PNLSelezioneStatistica;
    private javax.swing.JPanel PNLStatisticheProdotti;
    private javax.swing.JTextArea TXTStatistiche;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTitolo;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlTitolo;
    // End of variables declaration//GEN-END:variables
}

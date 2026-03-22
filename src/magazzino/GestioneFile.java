/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzino;

import java.io.*;
import java.util.*;

/**
 * Classe per la gestione della persistenza dei prodotti tramite file ad accesso diretto.
 *
 * FILE UTILIZZATI:
 *   prodotti.dat  →  file ad accesso diretto (RandomAccessFile) con record a lunghezza fissa.
 *   key.txt       →  file di testo con le coppie  id=posizione  (un indice per record).
 *
 * STRUTTURA DI UN RECORD IN prodotti.dat  (124 byte totali):
 *   [  4 byte ] int   → id
 *   [100 byte ] char* → nome  (50 caratteri Unicode a 2 byte, padding con spazi)
 *   [  4 byte ] int   → prezzoA
 *   [  4 byte ] int   → prezzoV
 *   [  4 byte ] int   → scorta
 *   [  4 byte ] int   → scortaMin
 *   [  4 byte ] int   → numVendite
 *
 * STRUTTURA DI key.txt:
 *   Ogni riga ha il formato:   id=posizioneByte
 *   Esempio:
 *     1=0
 *     2=124
 *     5=248
 *
 * @author taboada.taddeo
 */
public class GestioneFile {

    // -----------------------------------------------------------------------
    // Costanti
    // -----------------------------------------------------------------------

    /** Percorso del file ad accesso diretto. */
    private static final String FILE_DAT  = "prodotti.dat";

    /** Percorso del file degli indici/chiavi. */
    private static final String FILE_KEYS = "key.txt";

    /**
     * Lunghezza fissa (in caratteri) riservata al campo nome.
     * Ogni char viene scritto come 2 byte (writeChar), quindi occupa
     * NOME_LEN * 2 = 100 byte nel file.
     */
    private static final int NOME_LEN = 50;

    /**
     * Dimensione in byte di un singolo record:
     *   4 (id) + NOME_LEN*2 (nome) + 4*5 (5 campi int) = 124 byte.
     */
    private static final int RECORD_SIZE = 4 + NOME_LEN * 2 + 4 * 5;

    /** Logger di classe. */
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(GestioneFile.class.getName());


    // -----------------------------------------------------------------------
    // Metodi pubblici
    // -----------------------------------------------------------------------

    /**
     * Salva tutti i prodotti presenti in {@code rP} nel file ad accesso diretto
     * {@value #FILE_DAT} e aggiorna il file indici {@value #FILE_KEYS}.
     *
     * <p>Ogni prodotto occupa esattamente {@value #RECORD_SIZE} byte;
     * la posizione del record nel file è calcolata come {@code indice * RECORD_SIZE}
     * e memorizzata in key.txt nella forma {@code id=posizioneByte}.
     *
     * @param rP la raccolta di prodotti da salvare
     */
    public void salvaTutto(RaccoltaProdotti rP) {

        // Mappa  id → posizioneByte  che verrà scritta su key.txt
        Map<Integer, Long> indici = new LinkedHashMap<>();

        try (RandomAccessFile raf = new RandomAccessFile(FILE_DAT, "rw")) {

            // Azzera il file prima di riscriverlo da capo
            raf.setLength(0);

            long posizione = 0; // puntatore al byte corrente nel file

            for (Prodotto p : rP.getListaProdotti()) {

                // Spostamento esplicito alla posizione calcolata
                raf.seek(posizione);

                // Scrittura dei campi del prodotto
                raf.writeInt(p.getId());
                scriviStringaFissa(raf, p.getNome(), NOME_LEN);
                raf.writeInt(p.getPrezzoA());
                raf.writeInt(p.getPrezzoV());
                raf.writeInt(p.getScorta());
                raf.writeInt(p.getScortaMin());
                raf.writeInt(p.getNumVendite());

                // Registra l'indice per questo prodotto
                indici.put(p.getId(), posizione);

                posizione += RECORD_SIZE;
            }

        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE,
                    "Errore durante il salvataggio del file " + FILE_DAT, e);
            return; // Non aggiorniamo key.txt se prodotti.dat è fallito
        }

        // Scrittura del file indici key.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_KEYS))) {

            for (Map.Entry<Integer, Long> entry : indici.entrySet()) {
                pw.println(entry.getKey() + "=" + entry.getValue());
            }

        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE,
                    "Errore durante il salvataggio del file " + FILE_KEYS, e);
        }
    }


    /**
     * Carica tutti i prodotti dal file ad accesso diretto {@value #FILE_DAT}
     * usando le posizioni memorizzate in {@value #FILE_KEYS} e li inserisce
     * nella raccolta {@code rP} (dopo averla svuotata).
     *
     * <p>Il metodo legge prima key.txt per ottenere la mappa id → posizione,
     * poi accede direttamente a ciascun record in prodotti.dat tramite
     * {@link RandomAccessFile#seek(long)}.
     *
     * @param rP la raccolta in cui inserire i prodotti caricati
     */
    public void caricaTutto(RaccoltaProdotti rP) {

        // ── 1. Lettura del file indici key.txt ──────────────────────────────
        Map<Integer, Long> indici = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_KEYS))) {

            String riga;
            while ((riga = br.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty()) continue;

                String[] parti = riga.split("=", 2);
                if (parti.length != 2) {
                    logger.warning("Riga non valida in " + FILE_KEYS + ": " + riga);
                    continue;
                }

                int  id        = Integer.parseInt(parti[0].trim());
                long posizione = Long.parseLong(parti[1].trim());
                indici.put(id, posizione);
            }

        } catch (FileNotFoundException e) {
            logger.warning("File " + FILE_KEYS + " non trovato. Nessun prodotto caricato.");
            return;
        } catch (IOException | NumberFormatException e) {
            logger.log(java.util.logging.Level.SEVERE,
                    "Errore durante la lettura di " + FILE_KEYS, e);
            return;
        }

        // ── 2. Lettura dei record da prodotti.dat ────────────────────────────
        rP.svuota(); // Pulizia prima di ricaricare

        try (RandomAccessFile raf = new RandomAccessFile(FILE_DAT, "r")) {

            for (Map.Entry<Integer, Long> entry : indici.entrySet()) {

                long posizione = entry.getValue();

                // Accesso diretto al record tramite la posizione memorizzata
                raf.seek(posizione);

                int    id        = raf.readInt();
                String nome      = leggiStringaFissa(raf, NOME_LEN);
                int    prezzoA   = raf.readInt();
                int    prezzoV   = raf.readInt();
                int    scorta    = raf.readInt();
                int    scortaMin = raf.readInt();
                int    numVend   = raf.readInt();

                Prodotto p = new Prodotto(id, nome, prezzoA, prezzoV, scorta, scortaMin);
                // Ripristino del numero di vendite tramite setter dedicato
                p.setNumVendite(numVend);

                rP.aggiungiProdotto(p);
            }

        } catch (FileNotFoundException e) {
            logger.warning("File " + FILE_DAT + " non trovato. Nessun prodotto caricato.");
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE,
                    "Errore durante la lettura del file " + FILE_DAT, e);
        }
    }


    // -----------------------------------------------------------------------
    // Metodi privati di utilità
    // -----------------------------------------------------------------------

    /**
     * Scrive una stringa a lunghezza fissa di {@code lunghezza} caratteri.
     * Se {@code s} è più corta viene completata con spazi;
     * se è più lunga viene troncata.
     * Ogni carattere è scritto come 2 byte (UTF-16, big-endian) con
     * {@link RandomAccessFile#writeChar(int)}.
     *
     * @param raf      il file ad accesso diretto già aperto
     * @param s        la stringa da scrivere
     * @param lunghezza il numero fisso di caratteri da occupare
     * @throws IOException in caso di errore di I/O
     */
    private void scriviStringaFissa(RandomAccessFile raf, String s, int lunghezza)
            throws IOException {

        // Tronca se necessario
        if (s.length() > lunghezza) {
            s = s.substring(0, lunghezza);
        }

        // Scrittura carattere per carattere
        for (int i = 0; i < s.length(); i++) {
            raf.writeChar(s.charAt(i));
        }

        // Padding con spazi fino a lunghezza fissa
        for (int i = s.length(); i < lunghezza; i++) {
            raf.writeChar(' ');
        }
    }

    /**
     * Legge una stringa a lunghezza fissa di {@code lunghezza} caratteri
     * e rimuove gli spazi di padding in coda.
     *
     * @param raf      il file ad accesso diretto già aperto
     * @param lunghezza il numero fisso di caratteri da leggere
     * @return la stringa letta, senza spazi finali
     * @throws IOException in caso di errore di I/O
     */
    private String leggiStringaFissa(RandomAccessFile raf, int lunghezza)
            throws IOException {

        StringBuilder sb = new StringBuilder(lunghezza);

        for (int i = 0; i < lunghezza; i++) {
            sb.append(raf.readChar());
        }

        // Rimozione del padding di spazi a destra
        return sb.toString().stripTrailing();
    }
}

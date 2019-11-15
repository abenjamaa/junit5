package fr.hm.hapiour.adherent.metier.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * données de carte TP d'un contrat
 * Created by lhomme-a on 20/06/2017.
 */
public class CarteTpInfoArchive implements Comparable<CarteTpInfoArchive> {

    private LocalDateTime dateEdition;
    private int annee;
    private boolean renouvellement;
    private String idDocument;

    public CarteTpInfoArchive() {
    }

    public CarteTpInfoArchive(int annee, LocalDateTime dateEdition, boolean renouvellement, String idDocument) {
        this.dateEdition = dateEdition;
        this.annee = annee;
        this.renouvellement = renouvellement;
        this.idDocument = idDocument;
    }

    /**
     * dateEdition getter
     * @return dateEdition
     */
    public LocalDateTime getDateEdition() {
        return dateEdition;
    }

    /**
     * dateEdition setter
     * @param dateEdition the new dateEdition value
     */
    public void setDateEdition(LocalDateTime dateEdition) {
        this.dateEdition = dateEdition;
    }

    /**
     * annee getter
     * @return annee
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * annee setter
     * @param annee the new annee value
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * idDocument getter
     * @return idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * idDocument setter
     * @param idDocument the new idDocument value
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /**
     * idDocument getter
     * @return idDocument
     */
    public boolean getRenouvellement() {
        return renouvellement;
    }

    /**
     * renouvellement setter
     * @param renouvellement the new renouvellement value
     */
    public void setRenouvellement(boolean renouvellement) {
        this.renouvellement = renouvellement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarteTpInfoArchive that = (CarteTpInfoArchive) o;
        if (idDocument == null) return that.idDocument == null;
        return idDocument.equals(that.idDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocument);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * @param o autre objet
     * @return Returns a negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(CarteTpInfoArchive o) {
        return o.getDateEdition().compareTo(this.getDateEdition());
    }

    @Override
    public String toString() {
        return "CarteTpInfoArchive{" +
                "dateEdition=" + dateEdition +
                ", annee=" + annee +
                ", renouvellement=" + renouvellement +
                ", idDocument='" + idDocument + '\'' +
                '}';
    }
}

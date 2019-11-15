package fr.hm.hapiour.adherent.metier.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Contrat
 * Created by mortier-j on 02/06/2017.
 */
public class Contrat implements Comparable<Contrat> {

    private TypeContrat typeContrat;
    private Date radiationDate;
    private Date dateSouscription;
    private EtatContrat etatContrat;
    private String numeroContrat;
    private String numeroContratCollectif;
    private BigInteger numeroPersonne;
    private int rangAssure;
    private List<Integer> rangsAssureVisible;

    /**
     * typeContrat getter
     * @return typeContrat
     */
    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    /**
     * typeContrat setter
     * @param typeContrat the new typeContrat value
     */
    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    /**
     * radiationDate getter
     * @return radiationDate
     */
    public Date getRadiationDate() {
        return radiationDate; //NOSONAR
    }

    /**
     * radiationDate setter
     * @param radiationDate the new radiationDate value
     */
    public void setRadiationDate(Date radiationDate) {
        this.radiationDate = radiationDate; //NOSONAR
    }

    /**
     * numeroContrat getter
     * @return numeroContrat
     */
    public String getNumeroContrat() {
        return numeroContrat;
    }

    /**
     * numeroContrat setter
     * @param numeroContrat the new numeroContrat value
     */
    public void setNumeroContrat(String numeroContrat) {
        this.numeroContrat = numeroContrat;
    }

    /**
     * numeroPersonne getter
     * @return numeroPersonne
     */
    public BigInteger getNumeroPersonne() {
        return numeroPersonne;
    }

    /**
     * numeroPersonne setter
     * @param numeroPersonne the new numeroPersonne value
     */
    public void setNumeroPersonne(BigInteger numeroPersonne) {
        this.numeroPersonne = numeroPersonne;
    }

    /**
     * numeroContratCollectif getter
     * @return numeroContratCollectif
     */
    public String getNumeroContratCollectif() {
        return numeroContratCollectif;
    }

    /**
     * numeroContratCollectif setter
     * @param numeroContratCollectif the new numeroContratCollectif value
     */
    public void setNumeroContratCollectif(String numeroContratCollectif) {
        this.numeroContratCollectif = numeroContratCollectif;
    }

    /**
     * rangAssure getter
     * @return rangAssure
     */
    public int getRangAssure() {
        return rangAssure;
    }

    /**
     * rangAssure setter
     * @param rangAssure the new rangAssure value
     */
    public void setRangAssure(int rangAssure) {
        this.rangAssure = rangAssure;
    }

    /**
     * dateSouscription getter
     * @return dateSouscription
     */
    public Date getDateSouscription() {
        return dateSouscription;
    }

    /**
     * dateSouscription setter
     * @param dateSouscription the new dateSouscription value
     */
    public void setDateSouscription(Date dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    /**
     * etatContrat getter
     * @return etatContrat
     */
    public EtatContrat getEtatContrat() {
        return etatContrat;
    }

    /**
     * etatContrat setter
     * @param etatContrat the new etatContrat value
     */
    public void setEtatContrat(EtatContrat etatContrat) {
        this.etatContrat = etatContrat;
    }

    /**
     * on trie par état du contrat puis par rang puis numéro de contrat
     * @param o contrat comparé
     * @return -1 si on est avant
     */
    @Override
    public int compareTo(Contrat o) {
        int c = this.getEtatContrat().getOrdre() - o.getEtatContrat().getOrdre();
        if (c == 0) {
            c = this.getRangAssure() - o.getRangAssure();
        }
        if (c == 0) {
            return this.getNumeroContrat().compareTo(o.getNumeroContrat());
        }
        return c;
    }

    /**
     * rangsAssureVisible getter
     * @return rangsAssureVisible
     */
    public List<Integer> getRangsAssureVisible() {
        return rangsAssureVisible;
    }

    /**
     * rangsAssureVisible setter
     * @param rangsAssureVisible the new rangsAssureVisible value
     */
    public void setRangsAssureVisible(List<Integer> rangsAssureVisible) {
        this.rangsAssureVisible = rangsAssureVisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contrat contrat = (Contrat) o;
        return rangAssure == contrat.rangAssure &&
                Objects.equals(numeroContrat, contrat.numeroContrat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(numeroContrat, rangAssure);
    }
}

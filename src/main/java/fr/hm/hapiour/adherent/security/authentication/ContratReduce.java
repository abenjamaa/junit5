package fr.hm.hapiour.adherent.security.authentication;

import fr.hm.hapiour.adherent.metier.dto.Contrat;
import fr.hm.hapiour.adherent.metier.dto.EtatContrat;
import fr.hm.hapiour.adherent.metier.dto.TypeContrat;
import fr.hm.hapiour.util.exception.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * part of a contrat stored in jwt token
 * Created by lhomme-a on 09/06/2017.
 */
public class ContratReduce implements Serializable {

    private static final String SEPARATOR = ";";
    private static final Logger log = LoggerFactory.getLogger(ContratReduce.class);
    private static final int NB_INFO_CONTRAT = 10;

    // type de contrat
    private TypeContrat typeContrat;

    // date de radiation
    private Date radiationDate;

    // numero de contrat
    private String numeroContrat;

    // numero de contrat collectif
    private String numeroContratCollectif;

    // numero de personne
    private BigInteger numeroPersonne;

    // date de l'action asynchrone de chargement de remboursement
    private Date dateChargementRemboursement;

    // rang de l'assuré
    private List<Integer> rangsAssure;

    private EtatContrat etatContrat;

    private boolean remboursementSynchroTermine;

    private boolean assurePrincipal;

    /**
     * private empty constructor
     */
    private ContratReduce() {
    }

    /**
     * Construct a ContratReduce from a business Contrat*
     * @param contrat                     contrat
     * @param dateChargementRemboursement date de chargement  des remboursement
     */
    public ContratReduce(Contrat contrat, Date dateChargementRemboursement) {
        this.typeContrat = contrat.getTypeContrat();
        this.radiationDate = contrat.getRadiationDate() == null ? null : (Date) contrat.getRadiationDate().clone();
        this.numeroContrat = contrat.getNumeroContrat();
        this.numeroPersonne = contrat.getNumeroPersonne();
        this.dateChargementRemboursement = dateChargementRemboursement;
        this.rangsAssure = contrat.getRangsAssureVisible();
        this.assurePrincipal = contrat.getRangAssure() == 1;
        this.setEtatContrat(contrat.getEtatContrat());
    }

    /**
     * Recreate a contrat from a string value
     * @param contratStr contract string conversion
     * @return rconstructed contrat
     */
    public static ContratReduce valueOf(String contratStr) {

        // virer les cas de nullité
        if (StringUtils.isEmpty(contratStr)) {
            return null;
        }
        // on split les champs
        String[] split = contratStr.split(SEPARATOR);

        // verif du nombre d'éléments
        if (split.length != NB_INFO_CONTRAT) {
            throw new ConversionException("Deserialization problem for DTO Contrat");
        }
        int index = 0;
        // allocation
        ContratReduce contrat = new ContratReduce();
        // reconstruction
        TypeContrat typeContrat = TypeContrat.valueOf(split[index++]);

        contrat.setTypeContrat(typeContrat);

        // traitement de la date
        try {
            contrat.setRadiationDate(StringUtils.isEmpty(split[index++]) ? null : new Date(Long.parseLong(split[index - 1])));
        } catch (NumberFormatException e) {
            log.error("Problem while parsing radiation date", e);
        }

        contrat.setNumeroContrat(split[index++]);
        String tp = split[index++];
        contrat.setNumeroContratCollectif(StringUtils.isEmpty(tp) ? null : tp);

        // traitement de la date
        try {
            contrat.setDateChargementRemboursement(StringUtils.isEmpty(split[index++]) ? null : new Date(Long.parseLong(split[index - 1])));
        } catch (NumberFormatException e) {
            log.error("Problem while parsing dateChargementRemboursement", e);
        }
        String sNumPersonne = split[index++];


        BigInteger numPersonne = sNumPersonne.isEmpty() ? null : new BigInteger(sNumPersonne);
        contrat.setNumeroPersonne(numPersonne);

        String strRangAssure = split[index++];
        extraitRangAssure(contrat, strRangAssure);

        String strOrdreEtat = split[index++];
        int ordre = 0;
        try {
            ordre = Integer.parseInt(strOrdreEtat);
        } catch (NumberFormatException e) {
            log.error("Problem while parsing EtatContrat", e);
        }
        Optional<EtatContrat> etatContrat = EtatContrat.etatContratByOrdre(ordre);
        if (etatContrat.isPresent()) {
            contrat.setEtatContrat(etatContrat.get());
        } else {
            log.error("Problem while parsing EtatContrat");
        }

        contrat.setRemboursementSynchroTermine(!(split[index++].equals("0")));

        contrat.setAssurePrincipal(!(split[index].equals("0")));

        return contrat;
    }

    private static void extraitRangAssure(ContratReduce contrat, String strRangAssure) {
        if (!StringUtils.isEmpty(strRangAssure)) {
            String[] strRangs = strRangAssure.split("@");
            contrat.setRangsAssure(Arrays.stream(strRangs).map(rangAssure -> {
                try {
                    return Integer.parseInt(rangAssure);
                } catch (NumberFormatException e) {
                    log.error("Problem while parsing rangAssure", e);
                }
                return 0;
            }).collect(Collectors.toList()));
        } else {
            contrat.setRangsAssure(new ArrayList<>());
        }
    }

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
    private void setTypeContrat(TypeContrat typeContrat) {
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
    private void setRadiationDate(Date radiationDate) {
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
    private void setNumeroContrat(String numeroContrat) {
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
     * convert into a string for jwt claims
     * @return String conversion
     */
    @Override
    public String toString() {
        return (typeContrat == null ? "" : typeContrat.toString()) + SEPARATOR +
                (radiationDate == null ? "" : Long.toString(radiationDate.getTime())) +
                SEPARATOR + numeroContrat + SEPARATOR +
                (numeroContratCollectif == null ? "" : numeroContratCollectif)
                + SEPARATOR + (dateChargementRemboursement == null ? "" : Long.toString(dateChargementRemboursement.getTime()))
                + SEPARATOR + (numeroPersonne == null ? "" : numeroPersonne) + SEPARATOR + this.rangsToString()
                + SEPARATOR + (etatContrat == null ? "0" : etatContrat.getOrdre())
                + SEPARATOR + (remboursementSynchroTermine ? "1" : "0")
                + SEPARATOR + (assurePrincipal ? "1" : "0");
    }

    private String rangsToString() {
        return CollectionUtils.isEmpty(this.rangsAssure) ? "" :
                String.join("@", this.rangsAssure.stream().map(i -> Integer.toString(i)).collect(Collectors.toList()));
    }

    /**
     * dateChargementRemboursement getter
     * @return dateChargementRemboursement
     */
    public Date getDateChargementRemboursement() {
        return dateChargementRemboursement;
    }

    /**
     * dateChargementRemboursement setter
     * @param dateChargementRemboursement the new dateChargementRemboursement value
     */
    public void setDateChargementRemboursement(Date dateChargementRemboursement) {
        this.dateChargementRemboursement = dateChargementRemboursement;
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
     * equal implementation
     * @param o other object
     * @return true if other object is equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContratReduce that = (ContratReduce) o;

        if (typeContrat != that.typeContrat) return false;
        return numeroContrat != null ? numeroContrat.equals(that.numeroContrat) : that.numeroContrat == null;
    }

    /**
     * Returns a hash code value for the object.
     * @return hash code value for the object.
     */
    @Override
    public int hashCode() {
        int result = typeContrat != null ? typeContrat.hashCode() : 0;
        result = 31 * result + (numeroContrat != null ? numeroContrat.hashCode() : 0);
        return result;
    }

    /**
     * remboursementSynchroTermine getter
     * @return remboursementSynchroTermine
     */
    public boolean isRemboursementSynchroTermine() {
        return remboursementSynchroTermine;
    }

    /**
     * remboursementSynchroTermine setter
     * @param remboursementSynchroTermine the new remboursementSynchroTermine value
     */
    public void setRemboursementSynchroTermine(boolean remboursementSynchroTermine) {
        this.remboursementSynchroTermine = remboursementSynchroTermine;
    }

    /**
     * assurePrincipal getter
     * @return assurePrincipal
     */
    public boolean isAssurePrincipal() {
        return assurePrincipal;
    }

    /**
     * assurePrincipal setter
     * @param assurePrincipal the new assurePrincipal value
     */
    public void setAssurePrincipal(boolean assurePrincipal) {
        this.assurePrincipal = assurePrincipal;
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
     * rangsAssure getter
     * @return rangsAssure
     */
    public List<Integer> getRangsAssure() {
        return rangsAssure;
    }

    /**
     * rangsAssure setter
     * @param rangsAssure the new rangsAssure value
     */
    public void setRangsAssure(List<Integer> rangsAssure) {
        this.rangsAssure = rangsAssure;
    }

    public boolean isRangVisible(int rang) {
        return CollectionUtils.isEmpty(this.rangsAssure) || this.rangsAssure.contains(rang);
    }
}

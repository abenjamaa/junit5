package fr.hm.hapiour.adherent.metier.document;

import java.io.Serializable;

public class EditiqueDocumentCriteres implements Serializable {

    private String cle;

    private String valeur;

    private String operateur;

    public EditiqueDocumentCriteres(String cle, String valeur, String operateur) {
        this.cle = cle;
        this.valeur = valeur;
        this.operateur = operateur;
    }

    /**
     * cle getter
     * @return cle
     */
    public String getCle() {
        return cle;
    }

    /**
     * cle setter
     * @param cle the new cle value
     */
    public void setCle(String cle) {
        this.cle = cle;
    }

    /**
     * valeur getter
     * @return valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * valeur setter
     * @param valeur the new valeur value
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    /**
     * operateur getter
     * @return operateur
     */
    public String getOperateur() {
        return operateur;
    }

    /**
     * operateur setter
     * @param operateur the new operateur value
     */
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
}

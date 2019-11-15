package fr.hm.hapiour.adherent.metier.document;

import java.io.Serializable;
import java.util.List;

public class EditiqueDocumentRequest implements Serializable {
    private String codeFond;
    private List<EditiqueDocumentCriteres> critere;


    /**
     * codeFond getter
     * @return codeFond
     */
    public String getCodeFond() {
        return codeFond;
    }

    /**
     * codeFond setter
     * @param codeFond the new codeFond value
     */
    public void setCodeFond(String codeFond) {
        this.codeFond = codeFond;
    }

    /**
     * critere getter
     * @return critere
     */
    public List<EditiqueDocumentCriteres> getCritere() {
        return critere;
    }

    /**
     * critere setter
     * @param critere the new critere value
     */
    public void setCritere(List<EditiqueDocumentCriteres> critere) {
        this.critere = critere;
    }
}

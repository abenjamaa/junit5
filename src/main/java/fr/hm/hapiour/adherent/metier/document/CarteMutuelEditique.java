package fr.hm.hapiour.adherent.metier.document;

public class CarteMutuelEditique {

    private String dateEdition;

    private String id;

    private String nomDocument;

    public CarteMutuelEditique() {
    }

    public CarteMutuelEditique(String dateEdition, String id, String nomDocument) {
        this.dateEdition = dateEdition;
        this.id = id;
        this.nomDocument = nomDocument;
    }

    /**
     * dateEdition getter
     * @return dateEdition
     */
    public String getDateEdition() {
        return dateEdition;
    }

    /**
     * dateEdition setter
     * @param dateEdition the new dateEdition value
     */
    public void setDateEdition(String dateEdition) {
        this.dateEdition = dateEdition;
    }

    /**
     * id getter
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * id setter
     * @param id the new id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * nomDocument getter
     * @return nomDocument
     */
    public String getNomDocument() {
        return nomDocument;
    }

    /**
     * nomDocument setter
     * @param nomDocument the new nomDocument value
     */
    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }
}

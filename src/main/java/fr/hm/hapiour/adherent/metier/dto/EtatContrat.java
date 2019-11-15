package fr.hm.hapiour.adherent.metier.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum pour qualifier l'état d'un contrat côté métier
 * Created by mortier-j on 06/11/2017
 */
public enum EtatContrat {

    EN_INSTANCE(0),
    VALIDE(1),
    RESILIE(3),
    SUSPENDU(2);

    int ordre;

    EtatContrat(int ordre) {
        this.ordre = ordre;
    }

    public static Optional<EtatContrat> etatContratByOrdre(int ordre) {
        return Arrays.stream(EtatContrat.values()).filter(etatContrat1 -> etatContrat1.getOrdre() == ordre).findFirst();
    }

    /**
     * ordre getter
     * @return ordre
     */
    public int getOrdre() {
        return ordre;
    }


}

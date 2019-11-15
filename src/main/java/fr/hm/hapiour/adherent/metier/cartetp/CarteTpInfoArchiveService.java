package fr.hm.hapiour.adherent.metier.cartetp;

import fr.hm.hapiour.adherent.metier.document.CarteMutuelEditique;
import fr.hm.hapiour.adherent.metier.document.CourrierSanteDocumentService;
import fr.hm.hapiour.adherent.metier.dto.CarteTpInfoArchive;
import fr.hm.hapiour.adherent.metier.parameter.ParameterService;
import fr.hm.hapiour.adherent.security.authentication.ContratReduce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarteTpInfoArchiveService {

    public static final String YYYY_MM_DD_HH_MM = "uuuu-MM-dd+HH:mm";
    public static final String YYYY_MM_DD = "uuuu-MM-dd";
    public static final String ANNUEL = "ANNUEL";
    public static final String MERGE_CM_R = "%23merge:CM:R";
    public static final int DATE_PIVOT = 21;
    public static final String DUREE_APROXIMATIVE_ENTRE_EDITION_ET_ACTIVATION_CARTE_MUTUALISTE = "duree.aproximative.entre.edition.et.activation.carte.mutualiste";
    private static final Logger log = LoggerFactory.getLogger(CarteTpInfoArchiveService.class);
    private final ParameterService parameterService;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM);
    @Autowired
    private CourrierSanteDocumentService courrierSanteDocumentService;


    public CarteTpInfoArchiveService(CourrierSanteDocumentService courrierSanteDocumentService, ParameterService parameterService) {
        this.courrierSanteDocumentService = courrierSanteDocumentService;
        this.parameterService = parameterService;
    }

    /**
     * charge la liste des carte TP disponible
     * @param contratReduce contrat
     * @param now           date à laquelle rechercher
     * @return liste des cartes disponibles
     */
    public List<CarteTpInfoArchive> getListeCarteTpDisponibles(ContratReduce contratReduce, LocalDateTime now) {

        List<CarteTpInfoArchive> response = new ArrayList<>();
        List<CarteMutuelEditique> carteMutualistesEditique =
                courrierSanteDocumentService.getCarteMutualisteList(now.format(DateTimeFormatter.ofPattern(YYYY_MM_DD)),
                        contratReduce.getNumeroContrat());
        List<CarteTpInfoArchive> carteAtraiter = carteMutuelEditiqueListToCarteTpInfoArchiveList(carteMutualistesEditique);
        List<CarteTpInfoArchive> nextYearCards = carteAtraiter.stream().filter(cm -> cm.getAnnee() > now.getYear()).collect(Collectors.toList());

        if (!nextYearCards.isEmpty()) {
            response.add(nextYearCards.get(0));
        }

        List<CarteTpInfoArchive> currentYearCards =
                carteAtraiter.stream().filter(cm -> cm.getAnnee() <= now.getYear()).collect(Collectors.toList());

        Comparator<CarteTpInfoArchive> compareByName = getCarteTpInfoArchiveComparatorByAnneeAndDateEdition();

        List<CarteTpInfoArchive> sortedCurrentYearsCards = currentYearCards
                .stream()
                .sorted(compareByName)
                .collect(Collectors.toList());

        if (!sortedCurrentYearsCards.isEmpty()) {
            CarteTpInfoArchive newerCard = sortedCurrentYearsCards.get(0);
            response.add(newerCard);

            long carteEditeDepuisCombienDeJour = ChronoUnit.DAYS.between(newerCard.getDateEdition(), now);
            int nombreJour = parameterService.getIntProperty(DUREE_APROXIMATIVE_ENTRE_EDITION_ET_ACTIVATION_CARTE_MUTUALISTE, DATE_PIVOT);

            if (sortedCurrentYearsCards.size() > 1 && carteEditeDepuisCombienDeJour < nombreJour) {
                response.add(sortedCurrentYearsCards.get(1));
            }
        }
        return response;
    }

    private Comparator<CarteTpInfoArchive> getCarteTpInfoArchiveComparatorByAnneeAndDateEdition() {
        return Comparator
                .comparing(CarteTpInfoArchive::getAnnee)
                .thenComparing(CarteTpInfoArchive::getDateEdition)
                .reversed();
    }


    /**
     * @param carteMutuelEditique
     * @return
     */
    private CarteTpInfoArchive carteMutuelEditiqueToCarteTpInfoArchive(CarteMutuelEditique carteMutuelEditique) {
        CarteTpInfoArchive carteTpInfoArchive = new CarteTpInfoArchive();
        LocalDateTime dateEditionLDT = stringToLocalDateTime(carteMutuelEditique.getDateEdition());
        carteTpInfoArchive.setDateEdition(dateEditionLDT);
        carteTpInfoArchive.setIdDocument(carteMutuelEditique.getId() + carteMutuelEditique.getDateEdition());
        if (isRenouvellementCarte(carteMutuelEditique.getNomDocument(), carteMutuelEditique.getDateEdition())) {
            carteTpInfoArchive.setAnnee(dateEditionLDT.getYear() + 1);
            carteTpInfoArchive.setRenouvellement(true);
        } else {
            carteTpInfoArchive.setAnnee(dateEditionLDT.getYear());
            carteTpInfoArchive.setRenouvellement(false);
        }
        return carteTpInfoArchive;
    }

    /**
     * @param carteMutuelEditique
     * @return
     */
    private List<CarteTpInfoArchive> carteMutuelEditiqueListToCarteTpInfoArchiveList(List<CarteMutuelEditique> carteMutuelEditique) {
        List<CarteTpInfoArchive> carteTpInfoArchiveList = new ArrayList<>();
        carteMutuelEditique.forEach(cme -> carteTpInfoArchiveList.add(carteMutuelEditiqueToCarteTpInfoArchive(cme)));
        return carteTpInfoArchiveList;
    }


    /**
     * @param dateEdition
     * @return
     */
    private LocalDateTime stringToLocalDateTime(String dateEdition) {
        return LocalDateTime.parse(dateEdition, dateTimeFormatter);
    }


    /**
     * @param nomDocument
     * @param dateEdition
     * @return
     */
    private boolean isRenouvellementCarte(String nomDocument, String dateEdition) {
        LocalDateTime dateEditionLDT = stringToLocalDateTime(dateEdition);
        LocalDateTime dateEditionNouveauDocument = LocalDateTime.of(dateEditionLDT.getYear(), 10, 14, 0, 0);
        return nomDocument.contains(ANNUEL) && dateEditionLDT.isAfter(dateEditionNouveauDocument);
    }
}

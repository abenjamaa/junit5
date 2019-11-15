package fr.hm.hapiour.adherent.metier.cartetp;


import fr.hm.hapiour.adherent.metier.document.CarteMutuelEditique;
import fr.hm.hapiour.adherent.metier.document.CourrierSanteDocumentService;
import fr.hm.hapiour.adherent.metier.dto.CarteTpInfoArchive;
import fr.hm.hapiour.adherent.metier.dto.Contrat;
import fr.hm.hapiour.adherent.metier.dto.TypeContrat;
import fr.hm.hapiour.adherent.metier.parameter.ParameterService;
import fr.hm.hapiour.adherent.security.authentication.ContratReduce;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Tests Unitaires sur l'algorithme de sélection des cartes TP selon leur année effective.
 *
 * NOTES
 *
 * Pas besoin de monter un contexte Spring, ainsi les tests seront plus rapide.
 *
 * Les tests ont été écrit pour faire émerger un DSL given/when/then qui devrait rendre leur lecture plus aisée.
 *
 * On utilise la classe de test comme objet stateful conservant le contexte du test, donc un @Before pour bien remettre
 * à zéro cet état entre 2 tests.
 */
 class CarteTpInfoArchiveServiceTest {

    private static final String idDocument = "132121231321231qsqsqs";
    private static final String MERGE_CM_R = "%23merge:CM:R";
    private static final String CURRENTCARDID = "currentcardid";
    private static final String DATE_EDITION_APRES_15_OCTOBRE_INFERIEUR_21 = "DateEditionApres15OctobreInferieur21";
    private static final String CARTE_MUTUEL_DATE_EDITION_INFERIEUR_21 = "CarteMutuelDateEditionInferieur21";

    private List<CarteMutuelEditique> inputCards;
    private List<CarteTpInfoArchive> outputCards;
    private CarteTpInfoArchiveService carteTpInfoArchiveService;
    private LocalDateTime effectiveDate;
    private ContratReduce contratReduce;

    @BeforeEach
     void givenCommonInitialEmptyState() {
        inputCards = new ArrayList<>();
        outputCards = new ArrayList<>();
        effectiveDate = null;
        contratReduce = createContratReduceFixture();

        final CourrierSanteDocumentService mockCourrierSanteDocumentService = Mockito.mock(CourrierSanteDocumentService.class);
        final ParameterService mockParameterService = Mockito.mock(ParameterService.class);
        carteTpInfoArchiveService = new CarteTpInfoArchiveService(mockCourrierSanteDocumentService, mockParameterService);

        Mockito.when(mockParameterService.getIntProperty(Mockito.anyString(), Mockito.anyInt())).thenReturn(21);
        Mockito.when(mockCourrierSanteDocumentService.getCarteMutualisteList(Mockito.any(), Mockito.any())).thenReturn(inputCards);
    }

    @Test
     void itSouldReturnsTheLast2CardsWhenTheEditionDateOfTheLastCardIsLessThan21DaysComparedToTodaysDate() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-02-02+00:00", CURRENTCARDID + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-07-05+02:00", CARTE_MUTUEL_DATE_EDITION_INFERIEUR_21 + MERGE_CM_R)
        );
        givenEffectiveDate(2020, 7 ,15);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-07-05T02:00"),false, CARTE_MUTUEL_DATE_EDITION_INFERIEUR_21 + MERGE_CM_R + "2020-07-05+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-02-02T00:00"), false, CURRENTCARDID + MERGE_CM_R + "2020-02-02+00:00")
        );
    }

    @Test
     void itShouldReturnsTheLast2CardsWhenTheEditionDateOfTheLastCardIsLessThan21DaysComparedToTodaysDateAndNextYearCard() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2020-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-02-02+00:00", CURRENTCARDID + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-11-11+02:00", DATE_EDITION_APRES_15_OCTOBRE_INFERIEUR_21 + MERGE_CM_R)
        );
        givenEffectiveDate(2020, 11, 15);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2021, LocalDateTime.parse("2020-10-15T02:00"), true, idDocument + MERGE_CM_R + "2020-10-15+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-11-11T02:00"), false, DATE_EDITION_APRES_15_OCTOBRE_INFERIEUR_21 + MERGE_CM_R + "2020-11-11+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-02-02T00:00"), false, CURRENTCARDID + MERGE_CM_R + "2020-02-02+00:00")
        );
    }

    @Test
     void itShouldReturnCurrentRenewedCard() {
        givenInputCardList(createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R));
        givenEffectiveDate(2020, 3, 4);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2019-10-15T02" + ":00"), true, idDocument + MERGE_CM_R + "2019-10-15+02:00")
        );
    }

    @Test
     void itShouldReturnLatestCurrentCard() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-02-02+00:00", CURRENTCARDID + MERGE_CM_R)
        );
        givenEffectiveDate(2020, 9, 4);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-02-02T00:00"), false, CURRENTCARDID + MERGE_CM_R + "2020-02-02+00:00")
        );
    }

    @Test
     void itShouldReturnCurrentAndNextCards() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R), //carteMutuelEditeEnOctobre2019PourRenouvellement2020
                createCarteMutuelleEditiqueRenouvellementFixture("2018-11-01+00:00", CURRENTCARDID + MERGE_CM_R) // currentCard
        );
        givenEffectiveDate(2019, 11, 4);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2019-10-15T02:00"), true, idDocument + MERGE_CM_R + "2019-10-15+02:00"),
                new CarteTpInfoArchive(2019, LocalDateTime.parse("2018-11-01T00:00"), true, CURRENTCARDID + MERGE_CM_R + "2018-11-01+00:00")
        );
    }

    @Test
     void itShouldReturnTheTwoCardForYear2020() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2019-11-11+00:00", CURRENTCARDID + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-07-05+02:00", CARTE_MUTUEL_DATE_EDITION_INFERIEUR_21 + MERGE_CM_R)
        );
        givenEffectiveDate(2020, 7, 15);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-07-05T02:00"), false, CARTE_MUTUEL_DATE_EDITION_INFERIEUR_21 + MERGE_CM_R + "2020-07-05+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2019-10-15T02:00"), true, idDocument + MERGE_CM_R + "2019-10-15+02:00")
        );
    }

    @Test
     void itShouldReturnThreeCards() {
        givenInputCardList(
                createCarteMutuelleEditiqueRenouvellementFixture("2019-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleEditiqueRenouvellementFixture("2020-10-15+02:00", idDocument + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2020-11-11+02:00", DATE_EDITION_APRES_15_OCTOBRE_INFERIEUR_21 + MERGE_CM_R),
                createCarteMutuelleAnneeEnCoursFixture("2019-11-11+00" + ":00", CURRENTCARDID + MERGE_CM_R)
        );
        givenEffectiveDate(2020, 11, 15);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(
                new CarteTpInfoArchive(2021, LocalDateTime.parse("2020-10-15T02:00"), true, idDocument + MERGE_CM_R + "2020-10-15+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2020-11-11T02:00"), false, DATE_EDITION_APRES_15_OCTOBRE_INFERIEUR_21 + MERGE_CM_R + "2020-11-11+02:00"),
                new CarteTpInfoArchive(2020, LocalDateTime.parse("2019-10-15T02:00"), true, idDocument + MERGE_CM_R + "2019-10-15+02:00")
        );
    }

    @Test
     void itShouldReturnNothingWhenNoCarteMutuelleExist() {
        givenInputCardList(/* empty! */);
        givenEffectiveDate(2020, 11, 15);

        whenWeCallGetListeCarteTpDisponibles();

        thenOutputCardsShouldMatch(/* empty! */);
    }

    private void givenInputCardList(CarteMutuelEditique ...cards) {
        inputCards.clear();
        inputCards.addAll(Arrays.asList(cards));
    }

    private void givenEffectiveDate(int year, int month, int day) {
        effectiveDate = LocalDateTime.of(year, month, day, 2, 0);
    }

    private ContratReduce createContratReduceFixture() {
        Contrat contrat = new Contrat();
        Long numeroPersonne = 12345L;
        contrat.setTypeContrat(TypeContrat.CONTRAT_SANTE);
        contrat.setNumeroPersonne(BigInteger.valueOf(numeroPersonne));
        contrat.setNumeroContrat("123456");
        contrat.setRangAssure(1);
        return new ContratReduce(contrat, null);
    }

    private CarteMutuelEditique createCarteMutuelleEditiqueRenouvellementFixture(String dateEdition, String idDocument) {
        return createCarteMutuelleFixture(dateEdition, idDocument, true);
    }

    private CarteMutuelEditique createCarteMutuelleAnneeEnCoursFixture(String dateEdition, String idDocument) {
        return createCarteMutuelleFixture(dateEdition, idDocument, false);
    }

    private CarteMutuelEditique createCarteMutuelleFixture(String dateEdition, String idDocument, boolean renouvellement) {
        return new CarteMutuelEditique(
                dateEdition,
                idDocument,
                "peu_importe____" + (renouvellement?"ANNUEL":"xxxxx") + "____peu_importe.pdf"
        );
    }

    private void whenWeCallGetListeCarteTpDisponibles() {
        outputCards = carteTpInfoArchiveService.getListeCarteTpDisponibles(contratReduce, effectiveDate);
    }

    private void thenOutputCardsShouldMatch(CarteTpInfoArchive ...expectedCards) {
        final List<CarteTpInfoArchive> cards = outputCards;
        assertEquals(cards.size(),expectedCards.length);
        for(int i = 0; i < cards.size(); i++) {
            final CarteTpInfoArchive card = cards.get(i);
            final CarteTpInfoArchive expectedCard = expectedCards[i];
            Assertions.assertAll("",
                    () -> assertEquals(card.getRenouvellement(), expectedCard.getRenouvellement()),
                    ()-> assertEquals(card.getAnnee(), expectedCard.getAnnee()),
            ()-> assertEquals(card.getDateEdition(), expectedCard.getDateEdition()),
            ()-> assertEquals(card.getIdDocument(),expectedCard.getIdDocument()));

//            assertThat(card.getRenouvellement()).isEqualTo(expectedCard.getRenouvellement());
//            assertThat(card.getAnnee()).isEqualTo(expectedCard.getAnnee());
//            assertThat(card.getDateEdition()).isEqualTo(expectedCard.getDateEdition());
//            assertThat(card.getIdDocument()).isEqualTo(expectedCard.getIdDocument());
        }
    }

}
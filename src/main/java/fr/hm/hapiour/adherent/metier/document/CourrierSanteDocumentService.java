package fr.hm.hapiour.adherent.metier.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.hm.hapiour.adherent.metier.dto.TypeDocument;
import fr.hm.hapiour.util.RestTemplateUtils;
import fr.hm.hapiour.util.exception.RuntimeApiErrorException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourrierSanteDocumentService implements InitializingBean {

    public static final String EDITIQUE = "EDITIQUE";
    public static final String DATE_EDITION = "DateEdition";
    public static final String NUMERO_CONTRAT_INDIVIDUEL = "NumeroContratIndividuel";
    public static final String EQUAL = "Equal";
    public static final String LESS_OR_EQUAL = "LessOrEqual";
    public static final String FAMILLE_DOCUMENT = "FamilleDocument";
    public static final String CM = "CM";
    public static final String GREATER_OR_EQUAL = "GreaterOrEqual";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String SEPARATOR = ";";
    private RestTemplate restTemplate;

    @Value("${gestion.documentaire.documents.consultation.url}")
    private String ConsultationDocumenturl;


    @Value("${gestion.documentaire.documents.recherche.url}")
    private String rechercheDocumentsurl;

    @Value("${gestion.documentaire.documents.user}")
    private String user;
    @Value("${gestion.documentaire.documents.password}")
    private String password;

    private String authent;

    /**
     * init du proxy
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        String tp = user + ":" + password;
        this.authent = Base64.getEncoder().encodeToString(tp.getBytes());
        this.restTemplate = RestTemplateUtils.initRestTemplate("gestion.documentaire.documents", true);
    }

    public void loadCourrierSante(HttpServletResponse response, String dataFromAutoSignedId) {
        String idDoc = dataFromAutoSignedId.substring(TypeDocument.COURRIER_SANTE.name().length() + SEPARATOR.length());
        loadCourrierFromId(response, idDoc);
    }

    public void loadCourrierFromId(HttpServletResponse response, String idDoc) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("FOND", EDITIQUE);
        uriParams.put("ID", idDoc);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ConsultationDocumenturl);

        restTemplate.execute(builder.buildAndExpand(uriParams).toUriString(), HttpMethod.GET,
                request -> {
                    request.getHeaders().set("Authorization", "Basic " + CourrierSanteDocumentService.this.authent);
                    request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
                },
                clientresponse -> {
                    // bour
                    response.setStatus(clientresponse.getRawStatusCode());

                    response.setContentType(clientresponse.getHeaders().getContentType().toString());
                    List<String> dispo = clientresponse.getHeaders().get(CONTENT_DISPOSITION);
                    if (CollectionUtils.isEmpty(dispo)) {
                        dispo.forEach(head -> response.addHeader(CONTENT_DISPOSITION, head));
                    }

                    try (InputStream inputStream = clientresponse.getBody()) {
                        //On écrit le document lié à l'id autosigné dans la response
                        StreamUtils.copy(inputStream, response.getOutputStream());
                    }

                    response.flushBuffer();
                    return Boolean.TRUE;
                });
    }

    public List<CarteMutuelEditique> getCarteMutualisteList(String dateEdition, String numeroContratIndividuel) {

        EditiqueDocumentRequest request = new EditiqueDocumentRequest();
        List<CarteMutuelEditique> response;

        ObjectMapper mapper = new ObjectMapper();


        request.setCodeFond(EDITIQUE);
        request.setCritere(getCriteres(dateEdition, numeroContratIndividuel));
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        ResponseEntity<String> responseEntity = null;
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", "Basic " + CourrierSanteDocumentService.this.authent);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            HttpEntity httpEntity = new HttpEntity(ow.writeValueAsString(request), headers);
            responseEntity = restTemplate.exchange(rechercheDocumentsurl, HttpMethod.POST, httpEntity, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeApiErrorException(e);
        }

        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new CarteMutualisteDeserializer(CarteMutuelEditique.class));
        mapper.registerModule(module);

        try {
            response = mapper.readValue(responseEntity.getBody(), List.class);
        } catch (IOException e) {
            throw new RuntimeApiErrorException("", e);
        }

        return response;
    }

    private List<EditiqueDocumentCriteres> getCriteres(String dateEdition, String numeroContratIndividuel) {

        List<EditiqueDocumentCriteres> criteres = new ArrayList<>();
        criteres.add(new EditiqueDocumentCriteres(DATE_EDITION, dateEdition, LESS_OR_EQUAL));
        String debutPeriodeEditionAnneePrecedente = LocalDate.now().getYear() - 1 + "-10-15";
        criteres.add(new EditiqueDocumentCriteres(DATE_EDITION, debutPeriodeEditionAnneePrecedente, GREATER_OR_EQUAL));
        criteres.add(new EditiqueDocumentCriteres(NUMERO_CONTRAT_INDIVIDUEL, numeroContratIndividuel, EQUAL));
        criteres.add(new EditiqueDocumentCriteres(FAMILLE_DOCUMENT, CM, EQUAL));

        return criteres;
    }
}

package fr.hm.hapiour.adherent.metier.document;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarteMutualisteDeserializer extends StdDeserializer<List<CarteMutuelEditique>> {
    protected CarteMutualisteDeserializer(Class<?> vc) {
        super(vc);
    }

    public CarteMutualisteDeserializer() {
        this(null);
    }

    @Override
    public List<CarteMutuelEditique> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        JsonNode productNode = jsonParser.getCodec().readTree(jsonParser);

        List<CarteMutuelEditique> carteMutuelEditiqueList = new ArrayList<CarteMutuelEditique>();
        productNode.get("documents").get("document").forEach(jsonNode -> {
            CarteMutuelEditique carteMutuelEditique = new CarteMutuelEditique();
            carteMutuelEditique.setId(jsonNode.get("fixe").get("id").textValue());
            carteMutuelEditique.setNomDocument(jsonNode.get("fixe").get("nomcontenu").textValue());
            carteMutuelEditique.setDateEdition(jsonNode.get("variable").get("dates").get("date_edition").textValue());
            carteMutuelEditiqueList.add(carteMutuelEditique);
        });

        return carteMutuelEditiqueList;
    }
}

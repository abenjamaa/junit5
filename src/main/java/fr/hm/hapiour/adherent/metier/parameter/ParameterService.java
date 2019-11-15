package fr.hm.hapiour.adherent.metier.parameter;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des parametres de l'application
 * Created by lhomme-a on 12/07/2017.
 */
@Service
@Transactional(transactionManager = "adherentApiTransactionManager")
public class ParameterService {


    public int getIntProperty(String str, int any){
        return 1;
    }

}

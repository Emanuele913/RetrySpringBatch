package com.Batch20.controller;

import com.Batch20.service.BackendAdapter;
import com.Batch20.exception.RemoteServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class MyRestController {

    @Autowired
    BackendAdapter backendAdapter;
    // Esempio n3 , Utilizziamo una struttura per sfruttare il retry attraverso chiamata a service
    
    // chiamata per testare il retry
    // http://localhost:8080/retry?simulateretry=true&simulateretryfallback=false

    //simultanetry: parametro per simulare lo scenario di eccezione , in modo che spring possa riprovare
    //simulateretryfallback: poichè stiamo simulando l'eccezione , dopo aver riprovato alcune volte possiamo
    //aspettarci una chiamata di backend riuscita o tutti i tentativi codono. In questo caso andremo al metodo
    //FallBack per ottenere una risposta hardcoded . Questo garantisce che tutti i tentativi falliti andranno sul
    //percoso di fallback

    /*
    @GetMapping("/retry")
    @ExceptionHandler({ Exception.class })
    public String validateSPringRetryCapability(@RequestParam(required = false) boolean simulateretry,
                                                @RequestParam(required = false) boolean simulateretryfallback) throws RemoteServiceNotAvailableException {
        System.out.println("===============================");
        System.out.println("Inside RestController mathod..");

        return backendAdapter.getBackendResponse(simulateretry, simulateretryfallback);
    }

     */
}
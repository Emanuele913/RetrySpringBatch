package com.Batch20.service;

import com.Batch20.exception.RemoteServiceNotAvailableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface BackendAdapter {

// interfaccia per simulare scenari di successo/fallimento

    //@Retrayable questa è una delle annotazioni principali. Ci dice che se otteniamo l'eccezione , riprovare al massimo 3 volte
    //prima di inviare la risposta. inoltre introduciamo anche un secondo di ritardo con backoff
    @Retryable(value = { RemoteServiceNotAvailableException.class } , maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String getBackendResponse(boolean simulateretry, boolean simulateretryfallback) throws RemoteServiceNotAvailableException;

    //@Recover nel metodo di fallback indica che se dopo 3 tentativi non abbiamo alcuna risposta di successo , la risposta
    //verrà da questo metodo di fallback.Assicurandosi di passare l'eccezione prevista come parametro , spring troverà il metodo
    @Recover
    public String getBackendResponseFallback(RuntimeException e);

}
package com.xhesiballa.crawler;

import com.xhesiballa.crawler.clients.MangaReader;
import com.xhesiballa.crawler.clients.MangafoxClient;
import com.xhesiballa.crawler.interfaces.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientFactory {

    private Utils utils;
    private List<Client> registeredClients;

    ClientFactory(Utils utils) {
        this.utils = utils;

        registeredClients = new ArrayList<>();
        registeredClients.add(new MangafoxClient(utils));
        registeredClients.add(new MangaReader(utils));
    }

    public List<Client> getRegisteredClients() {
        return registeredClients;
    }
}

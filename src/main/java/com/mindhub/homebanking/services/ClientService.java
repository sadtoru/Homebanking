package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> findAll();
    Client findById(Long id);
    ClientDTO getClientDTO(Long id);
    void save(Client client);
    Client findByEmail(String email);
}

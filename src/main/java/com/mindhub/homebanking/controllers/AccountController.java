package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }
    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
        if(clientRepository.findByEmail(authentication.getName()).getAccounts().size() < 3){
            try {
                Client client = clientRepository.findByEmail(authentication.getName());
                String random;

                do{
                    random = "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));

                }while (accountRepository.existsByNumber(random));

                Account account = new Account(random, 0.0);
                client.addAccount(account);
                accountRepository.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>("Error creating account: " + e.getMessage() , HttpStatus.FORBIDDEN);
            }

        }else{
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.GET)
    public List<AccountDTO> getAccount(Authentication auth){
        return clientRepository.findByEmail(auth.getName()).getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
}

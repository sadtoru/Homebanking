package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        return new AccountDTO(accountService.findById(id));
    }
    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
        if(clientService.findByEmail(authentication.getName()).getAccounts().size() < 3){
            try {
                Client client = clientService.findByEmail(authentication.getName());
                String random;

                do{
                    random = "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));

                }while (accountService.existsByNumber(random));

                Account account = new Account(random, 0.0);
                client.addAccount(account);
                accountService.save(account);
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
        return clientService.findByEmail(auth.getName()).getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                 @RequestParam Double amount, @RequestParam String description, Authentication authentication){
        Account originAccount = accountRepository.findByNumber(fromAccountNumber);
        Account destinyAccount = accountRepository.findByNumber(toAccountNumber);
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        if (originAccount == null || destinyAccount == null){
            return new ResponseEntity<>("esa cuenta no existe", HttpStatus.FORBIDDEN);
        }
        Client destinyClient = destinyAccount.getClient();
        if (destinyClient == null){
            return new ResponseEntity<>("cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        if(fromAccountNumber.isBlank() || toAccountNumber.isBlank() || description.isBlank()){
            return new ResponseEntity<>("algo esta faltando completar", HttpStatus.FORBIDDEN);
        }
        if (amount.isNaN() || amount <= 0.0){
            return new ResponseEntity<>("revisa el monto", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("revisa las cuentas", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(fromAccountNumber))){
            return new ResponseEntity<>("la cuenta no coincide con el cliente actual", HttpStatus.FORBIDDEN );
        }
        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("monto no disponible", HttpStatus.FORBIDDEN);
        }
        Transaction originTransaction = new Transaction(amount,description, LocalDateTime.now(), TransactionType.DEBIT);
        Transaction destinyTransaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);

        transactionRepository.save(destinyTransaction);
        transactionRepository.save(originTransaction);

        originAccount.addTransactions(originTransaction);
        destinyAccount.addTransactions(destinyTransaction);
        originAccount.setBalance(originAccount.getBalance() - amount);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);

        return new ResponseEntity<>("transaccion realizada", HttpStatus.CREATED);
    }

}

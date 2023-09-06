package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        List<Loan> loans =  loanService.findAll();
        return loans.stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication auth){
        if (loanApplicationDTO == null){
            return new ResponseEntity<>("There are empty fields", HttpStatus.FORBIDDEN);
        }
        Client currentClient = clientService.findByEmail(auth.getName());
        if (loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getToAccountNumber().isBlank() || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("There are incorrect fields." , HttpStatus.FORBIDDEN);
        }
        Loan currentLoan = loanService.findLoanById(loanApplicationDTO.getLoanId());
        if(currentLoan == null){
            return new ResponseEntity<>("Please provide a correct loan.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > currentLoan.getMaxAmount() || loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Please verify the provided amount.", HttpStatus.FORBIDDEN);
        }
        if (currentLoan.getPayments().stream().noneMatch(payment -> payment.equals(loanApplicationDTO.getPayments()))){
            return new ResponseEntity<>("Incorrect payment.", HttpStatus.FORBIDDEN);
        }
        if(accountService.findByNumber(loanApplicationDTO.getToAccountNumber()) == null){
            return new ResponseEntity<>("Invalid destination account.", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(loanApplicationDTO.getToAccountNumber()))){
            return new ResponseEntity<>("The destination account is invalid.", HttpStatus.FORBIDDEN);
        }
        try{
            double percentage = loanApplicationDTO.getAmount() + ((double) 20 / 100 ) * loanApplicationDTO.getAmount();
            String message = currentLoan.getName().concat(" Loan approved");
            ClientLoan clientLoan = new ClientLoan(percentage, loanApplicationDTO.getPayments());
            Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), message, LocalDateTime.now(), TransactionType.CREDIT);
            Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            account.addTransactions(transaction);
            transactionService.save(transaction);
            currentLoan.addClientLoan(clientLoan);
            currentClient.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);
            return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("Please try again later", HttpStatus.FORBIDDEN);
        }

    }
}

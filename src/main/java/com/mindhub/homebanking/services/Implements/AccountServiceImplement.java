package com.mindhub.homebanking.services.Implements;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> findAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return new AccountDTO(this.findById(id));
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByNumber(String account) {
        return accountRepository.findByNumber(account);
    }
}

package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();
    Account findById(Long id);
    AccountDTO getAccountDTO(Long id);
    boolean existsByNumber(String number);
    void save(Account account);
    Account findByNumber(String account);
}

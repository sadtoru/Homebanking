package com.mindhub.homebanking.services.Implements;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}

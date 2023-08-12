package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return (args) -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Pepe", "Morel", "pepe@email.com");

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.00);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDate.now(), 8000.00);

			clientRepository.save(melba);
			clientRepository.save(client2);

			melba.addAccount(account1);
			melba.addAccount(account2);
			client2.addAccount(account3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction(700.00, "shopping", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(1000.00, "coffe", LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction3 = new Transaction(2500.00, "subscription", LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction4 = new Transaction(300.00, "phone", LocalDateTime.now(), TransactionType.DEBIT);

			account1.addTransactions(transaction1);
			account2.addTransactions(transaction2);
			account2.addTransactions(transaction3);
			account1.addTransactions(transaction4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			Loan mortgage = new Loan("Mortgage", 500000.00, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000.00, List.of(6,12,24));
			Loan automotive = new Loan("Automotive", 300000.00, List.of(6,12,24,36));

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			ClientLoan clientMelba1 = new ClientLoan(400000.00,60);
			ClientLoan clientMelba2 = new ClientLoan(50000.00,12);
			ClientLoan clientLoan1 = new ClientLoan(100000.00,24);

			mortgage.addClientLoan(clientMelba1);
			personal.addClientLoan(clientMelba2);
			personal.addClientLoan(clientLoan1);
			melba.addClientLoan(clientMelba1);
			melba.addClientLoan(clientMelba2);
			client2.addClientLoan(clientLoan1);

			clientLoanRepository.save(clientMelba1);
			clientLoanRepository.save(clientMelba2);
			clientLoanRepository.save(clientLoan1);



		};
	}

}

package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args) -> {
			Client client = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Pepe", "Morel", "pepe@email.com");

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.00);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDate.now(), 8000.00);

			clientRepository.save(client);
			clientRepository.save(client2);

			client.addAccount(account1);
			client.addAccount(account2);
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

		};
	}

}

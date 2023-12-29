package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

/*	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args) -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("asd1234"));
			Client client2 = new Client("Pepe", "Morel", "pepe@email.com", passwordEncoder.encode("asd4321"));
			Client admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("admin123"));

			Account account1 = new Account("VIN001", 5000.00);
			Account account2 = new Account(LocalDate.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDate.now(), 8000.00);

			clientRepository.save(melba);
			clientRepository.save(client2);
			clientRepository.save(admin);

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
			account3.addTransactions(transaction4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			Loan loanMortgage = new Loan("Mortgage", 500000.00, List.of(12,24,36,48,60));
			Loan loanPersonal = new Loan("Personal", 100000.00, List.of(6,12,24));
			Loan loanAutomotive = new Loan("Automotive", 300000.00, List.of(6,12,24,36));

			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotive);

			ClientLoan loanMelba1 = new ClientLoan(400000.00,60);
			ClientLoan loanMelba2 = new ClientLoan(50000.00,12);
			ClientLoan loanPepe1 = new ClientLoan(100000.00,24);

			loanMortgage.addClientLoan(loanMelba1);
			loanPersonal.addClientLoan(loanMelba2);
			loanPersonal.addClientLoan(loanPepe1);

			melba.addClientLoan(loanMelba1);
			melba.addClientLoan(loanMelba2);
			client2.addClientLoan(loanPepe1);

			clientLoanRepository.save(loanMelba1);
			clientLoanRepository.save(loanMelba2);
			clientLoanRepository.save(loanPepe1);

			Card cardGoldMelba = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.DEBIT, CardColor.GOLD, "4579-5589-1134-5079", 899, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card cardTitaniumMelba = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "4789-5100-5234-7083", 736, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card cardSilverPepe = new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER, "3719-0034-1904-4436", 830, LocalDateTime.now(), LocalDateTime.now().plusYears(5));

			melba.addCards(cardGoldMelba);
			melba.addCards(cardTitaniumMelba);
			client2.addCards(cardSilverPepe);

			cardRepository.save(cardGoldMelba);
			cardRepository.save(cardTitaniumMelba);
			cardRepository.save(cardSilverPepe);

		};
	}*/

}

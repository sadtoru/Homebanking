package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class CardController {
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private CardRepository cardRepository;
        @RequestMapping(value = "/cards", method = RequestMethod.POST)
        public ResponseEntity<Object> newCard(
                @RequestParam CardType type, @RequestParam CardColor color, Authentication auth){
            Client client =  clientRepository.findByEmail(auth.getName());
            int cvv = (int)Math.floor(Math.random()*999-111)+111;
            String cardNumber = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999 + 1));

            do{
                for (int i = 0; i < 3; i++) {
                    String random = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999 + 1));
                    cardNumber = cardNumber.concat("-").concat(random);
                }
            }while (cardRepository.findByNumber(cardNumber) != null);

            boolean hasDebit = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).count() <= 3;
            boolean hasCredit = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).count() <= 3;
            boolean hasColorCredit = client.getCards().stream().anyMatch(card -> card.getColor() == color &&  card.getType().equals(CardType.CREDIT));
            boolean hasColorDebit = client.getCards().stream().anyMatch(card -> card.getColor() == color && card.getType().equals(CardType.DEBIT));

            if (type.equals(CardType.CREDIT)){
                if (hasCredit) {
                    if (hasColorCredit) {
                        return new ResponseEntity<>("You already have a " + color + " credit card.", HttpStatus.FORBIDDEN);
                    } else {
                        Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv);
                        client.addCards(card);
                        cardRepository.save(card);
                        return new ResponseEntity<>("A new credit card has been created.", HttpStatus.CREATED);
                    }
                }else{
                    return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
                }

            } else if (type.equals(CardType.DEBIT)) {
                if (hasDebit) {
                    if (hasColorDebit) {
                        return new ResponseEntity<>("You already have a " + color + " credit card.", HttpStatus.FORBIDDEN);
                    } else {
                        Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv);
                        client.addCards(card);
                        cardRepository.save(card);
                        return new ResponseEntity<>("A new debit card has been created.", HttpStatus.CREATED);
                    }
                }else{
                    return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
                }

            }else {
                return new ResponseEntity<>("You have reached the cards limit.", HttpStatus.FORBIDDEN);
            }

        }
}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;
    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
        public ResponseEntity<Object> applyForNewCard(
                @RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication auth) {
        Client client = clientService.findByEmail(auth.getName());
        int cvv = (int) Math.floor(Math.random() * 999 - 111) + 111;
        String cardNumber = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999 + 1));

        do {
            for (int i = 0; i < 3; i++) {
                String random = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999 + 1));
                cardNumber = cardNumber.concat("-").concat(random);
            }
        } while (cardService.findByNumber(cardNumber) != null);

        List<Card> cardListClient = cardService.findByClientCardAndTypeAndColors(client, cardType, cardColor);

        if (cardListClient.size() <= 3) {
            boolean hasCardColor = cardListClient.stream().anyMatch(card -> card.getColor().equals(cardColor));
            if (hasCardColor) {
                return new ResponseEntity<>("You already have a " + cardColor + " " + cardType + " card", HttpStatus.FORBIDDEN);
            }
            Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, cardNumber, cvv);
            client.addCards(card);
            cardService.save(card);
            return new ResponseEntity<>("A new " + cardType + " card has been created.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You have reached the cards limit.", HttpStatus.FORBIDDEN);
        }
}

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.GET)
    public List<CardDTO> getCards(Authentication auth){
        return clientService.findByEmail(auth.getName()).getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

}

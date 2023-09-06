package com.mindhub.homebanking.services.Implements;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }
    @Override
    public List<Card> findByClientCardAndTypeAndColors(Client client, CardType type, CardColor color) {
        return cardRepository.findByClientCardAndTypeAndColor(client, type, color);
    }
}

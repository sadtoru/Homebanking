package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface CardService {
    Card findByNumber(String number);
    Card save(Card card);

    List<Card> findByClientCardAndTypeAndColors(Client client, CardType type, CardColor color);
}

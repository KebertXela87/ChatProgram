package edu.jhu.teamundecided.clueless.deck;

import edu.jhu.teamundecided.clueless.player.Player;

import java.io.IOException;
import java.util.*;

public class DeckController
{
    // Decks
    private Deck _suspectDeck;
    private Deck _roomDeck;
    private Deck _weaponDeck;
    private ArrayList<Deck> _decks;
    private Deck _FullDeck;

    // Card Lists
    private ArrayList<String> _Suspects;
    private ArrayList<String> _Rooms;
    private ArrayList<String> _Weapons;
    private ArrayList<Card> _CaseFile;

    public DeckController()
    {
        // create the card name lists - used to create the cards
        _Suspects = new ArrayList<>(Arrays.asList("Mr._Green", "Col._Mustard", "Mrs._Peacock", "Prof._Plum",
                "Ms._Scarlett", "Mrs._White"));
        _Rooms = new ArrayList<>(Arrays.asList("Ballroom", "Kitchen", "Dining_Room", "Lounge", "Hall", "Study",
                "Library", "Conservatory", "Billiard_Room"));
        _Weapons = new ArrayList<>(Arrays.asList("Candle_Stick", "Dagger", "Lead_Pipe", "Revolver", "Rope", "Wrench"));

        setupDecks();
        shuffleAllDecks();
        selectCaseFile();
        combineDecks();
    }

    private void setupDecks()
    {
        _suspectDeck = new Deck(_Suspects, Card.CardType.Suspect);
        _roomDeck = new Deck(_Rooms, Card.CardType.Room);
        _weaponDeck = new Deck(_Weapons, Card.CardType.Weapon);

        _decks = new ArrayList<>();
        _decks.add(_suspectDeck);
        _decks.add(_roomDeck);
        _decks.add(_weaponDeck);
    }

    private void combineDecks()
    {
        ArrayList<Card> tempList = new ArrayList<>();

        for (Deck deck : _decks)
        {
            tempList.addAll(deck.getCards());
        }

        _FullDeck = new Deck(tempList);

        // shuffle full deck
        _FullDeck.shuffleCards();
    }

    private void selectCaseFile()
    {
        _CaseFile = new ArrayList<>();

        for (Deck deck : _decks)
        {
            Card temp = deck.getCards().get(0);
            _CaseFile.add(temp);
            deck.removeCard(temp);
        }
    }

    private void shuffleAllDecks()
    {
        _decks.forEach(Deck::shuffleCards);
    }

    public boolean dealCards(ArrayList<Player> players) throws IOException
    {
        // This method will deal all the cards to all the players
        if (players == null)
        {
            System.out.println("Need players...");
            return false;
        }

        int playerId = 0;
        for (Card card : _FullDeck.getCards())
        {
            try
            {
                players.get(playerId++).getPlayerHand().addCard(card);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return false;
            }

            // loop playerId
            if (playerId == players.size())
            {
                playerId = 0;
            }
        }

//        for (Player player : players)
//        {
//            player.sendHandToClient();
//        }

        // Deal successful
        return true;
    }

    // Method to check an accusation
    public boolean checkAccusation(Suggestion suggestion)
    {
        for (Card card : _CaseFile)
        {
            if(!card.getCardName().equals(suggestion.getCard(card.getType()).getCardName()))
            {
                return false;
            }
        }

        return true;
    }

    public Deck getSuggestionDeck()
    {
        Deck suggestionDeck = new Deck();

        suggestionDeck.addCardsFromDeck(new Deck(_Suspects, Card.CardType.Suspect));
        suggestionDeck.addCardsFromDeck(new Deck(_Rooms, Card.CardType.Room));
        suggestionDeck.addCardsFromDeck(new Deck(_Weapons, Card.CardType.Weapon));

        return suggestionDeck;
    }
}

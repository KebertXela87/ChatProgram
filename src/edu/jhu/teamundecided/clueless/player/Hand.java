package edu.jhu.teamundecided.clueless.player;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.Suggestion;

import java.util.ArrayList;

public class Hand
{
    private ArrayList<Card> _cards;

    public Hand()
    {
        _cards = new ArrayList<>();
    }

    public void addCard(Card card)
    {
        if(card != null)
        {
            if (!_cards.contains(card))
            {
                _cards.add(card);
            }
        }
    }

    public ArrayList<Card> getCards()
    {
        return _cards;
    }

    public void setCards(ArrayList<Card> cards)
    {
        this._cards = cards;
    }


    public void displayCards()
    {

        System.out.println("You have the following cards:");
        for (Card card : _cards)
        {
            System.out.println(card.toString());
        }

        System.out.println();
    }

    @Override
    public String toString()
    {
        StringBuilder hand = new StringBuilder();
        for(Card card : _cards)
        {
            hand.append(":" + card.getCardName());
        }

        return hand.toString();
    }


   public ArrayList<Card> getMatchingCards(Suggestion suggestion)
   {

       ArrayList<Card> matchingCards = new ArrayList<>();

       for (Card suggestedCard : suggestion.getSuggestedCards())
       {
           for (Card playerCard : _cards)
           {
               if (suggestedCard.isSameAs(playerCard))
               {
                   matchingCards.add(playerCard);
               }
           }
       }

       return matchingCards;
   }







}

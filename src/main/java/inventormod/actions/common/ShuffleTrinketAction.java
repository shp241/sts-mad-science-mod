package inventormod.actions.common;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

import inventormod.cards.AbstractTrinket;
import inventormod.cards.FlamableTrinket;
import inventormod.cards.FoamTrinket;
import inventormod.cards.RecycledTrinket;
import inventormod.cards.ShinyTrinket;
import inventormod.cards.SpikyTrinket;
import inventormod.relics.PolishingWheel;

public class ShuffleTrinketAction extends AbstractGameAction {
    private static final float startingDuration = 0.5f;
    private int amount;
    private boolean random;
    private boolean cardOffset;

    public ShuffleTrinketAction(int amount, boolean random, boolean cardOffset) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = startingDuration;
        this.amount = amount;
        this.random = random;
        this.cardOffset = cardOffset;
    }

    @Override
    public void update() {
        if (this.duration == startingDuration) {
            amount = Math.min(amount, allTrinkets.size());
            if (random) {
                // random without replacement
                ArrayList<AbstractCard> cards = new ArrayList<>();
                while(cards.size() < amount) {
                    AbstractCard card = allTrinketsToUse().getRandomCard(false);
                    if (!cards.contains(card)) {
                        cards.add(card);
                    }
                }
                shuffleTrinkets(cards);
                isDone = true;
                return;
            } else {
                // Show a screen to select a number of cards
                String message = amount == 1 ? AbstractTrinket.EXTENDED_DESCRIPTION[0] : AbstractTrinket.EXTENDED_DESCRIPTION[1] + amount + AbstractTrinket.EXTENDED_DESCRIPTION[2];
                AbstractDungeon.gridSelectScreen.open(allTrinketsToUse(), amount, message, false, false, true, false);
                this.tickDuration();
                return;
            }
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            shuffleTrinkets(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.tickDuration();
    }

    private void shuffleTrinkets(ArrayList<AbstractCard> cards) {
        // see MakeTempCardInDrawPileAction
        boolean randomSpot = true;
        for (AbstractCard c : cards) {
            c.unhover();
            if (cards.size() < 6) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c, (float)Settings.WIDTH / 2.0f, (float)Settings.HEIGHT / 2.0f, randomSpot, cardOffset));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c, randomSpot));
            }
        }
    }

    private static CardGroup allTrinkets;
    private static CardGroup allUpgradedTrinkets;
    {
        allTrinkets = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        allTrinkets.addToTop(new FoamTrinket());
        allTrinkets.addToTop(new FlamableTrinket());
        allTrinkets.addToTop(new RecycledTrinket());
        allTrinkets.addToTop(new SpikyTrinket());
        allTrinkets.addToTop(new ShinyTrinket());
        // Note: no TastyTrinket
        allTrinkets.sortAlphabetically(false);
        // Upgraded
        allUpgradedTrinkets = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : allTrinkets.group) {
            AbstractCard copy = c.makeCopy();
            copy.upgrade();
            allUpgradedTrinkets.addToTop(copy);
        }
    }

    private static CardGroup allTrinketsToUse() {
        if (AbstractDungeon.player.hasRelic(PolishingWheel.ID)) {
            return allUpgradedTrinkets;
        } else {
            return allTrinkets;
        }
    }
}

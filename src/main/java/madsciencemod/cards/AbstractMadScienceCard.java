package madsciencemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomCard;
import madsciencemod.MadScienceMod;
import madsciencemod.patches.CardColorEnum;
import madsciencemod.powers.FuelPower;

public abstract class AbstractMadScienceCard extends CustomCard {
    public int fuelCost = 0;
    public boolean costsFuelThisTurn = false;
    public int hackNotToPayCost; // see UseCardPatch

    public AbstractMadScienceCard(String id, String name, int cost, String description, CardType type, CardRarity rarity, CardTarget target) {
        super(id, name, MadScienceMod.cardImage(id), cost, description, type, CardColorEnum.MAD_SCIENCE, rarity, target);
    }

    public AbstractMadScienceCard(String id, String name, int cost, String description, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, MadScienceMod.cardImage(id), cost, description, type, color, rarity, target);
    }

    public AbstractMadScienceCard(String id, String name, String image, int cost, String description, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, image, cost, description, type, color, rarity, target);
    }

    public int fuelCostForTurn() {
        return fuelCost + (costsFuelThisTurn ? costForTurn : 0);
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (costsFuelThisTurn) {
            // Replace energy check with fuel check
            if (AbstractDungeon.actionManager.turnHasEnded) {
                this.cantUseMessage = TEXT[9];
                return false;
            }
            if (AbstractDungeon.player.hasPower("Entangled") && this.type == CardType.ATTACK) {
                this.cantUseMessage = TEXT[10];
                return false;
            }
            if (this.freeToPlayOnce) {
                return true;
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r.canPlay(this)) continue;
                return false;
            }
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.canPlay(this)) continue;
                return false;
            }
            this.cantUseMessage = TEXT[11];
            return FuelPower.currentAmount() >= this.fuelCostForTurn();
        }
        // enough energy?
        if (!super.hasEnoughEnergy()) return false;
        if (!freeToPlayOnce && FuelPower.currentAmount() < fuelCost) {
            this.cantUseMessage = FuelPower.NOT_ENOUGH_FUEL_MESSAGE;
            return false;
        }
        return true;
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.costsFuelThisTurn = false;
    }

    // Note: actually spending fuel is done in UseCardPatch
}

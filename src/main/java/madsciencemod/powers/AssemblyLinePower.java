package madsciencemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import madsciencemod.actions.common.ShuffleTrinketAction;

public class AssemblyLinePower extends AbstractMadSciencePower {
    public static final String POWER_ID = "MadScienceMod:AssemblyLine";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AssemblyLinePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, owner, amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ShuffleTrinketAction(this.amount, false, false));
    }
}

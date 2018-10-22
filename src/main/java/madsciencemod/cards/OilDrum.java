package madsciencemod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import madsciencemod.actions.common.GainFuelAction;

public class OilDrum extends AbstractMadScienceCard {
    public static final String ID = "MadScienceMod:OilDrum";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int FUEL_AMT = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_BLOCK_AMT = 3;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;

    public OilDrum() {
        super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, CardTarget.SELF);
        this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = FUEL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new GainFuelAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new OilDrum();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}

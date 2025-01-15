/*
 * Copyright (c) 2020, Zoinkwiz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.questhelper.helpers.miniquests.cracktheclueii;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.Requirement;
import com.questhelper.rewards.ItemReward;
import com.questhelper.steps.DetailedQuestStep;
import com.questhelper.steps.DigStep;
import com.questhelper.steps.EmoteStep;
import com.questhelper.steps.QuestStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

public class CrackTheClueII extends BasicQuestHelper
{
    //Items Required
    ItemRequirement spade, pieDish, rawHerring, goblinMail, plainPizza, woodenShield, cheese;

    QuestStep week1Dig, week2Dig, week3Emotes, week4Dig, finalEmotes;

    @Override
    public Map<Integer, QuestStep> loadSteps()
    {
        initializeRequirements();
        setupSteps();
        Map<Integer, QuestStep> steps = new HashMap<>();

        steps.put(0, week1Dig);
        steps.put(1, week2Dig);
        steps.put(2, week3Emotes);
        steps.put(3, week4Dig);
        steps.put(4, finalEmotes);

        return steps;
    }

    @Override
    protected void setupRequirements()
    {
        spade = new ItemRequirement("Spade", ItemID.SPADE).isNotConsumed();
        pieDish = new ItemRequirement("Pie dish", ItemID.PIE_DISH).isNotConsumed();
        rawHerring = new ItemRequirement("Raw herring", ItemID.RAW_HERRING).isNotConsumed();
        goblinMail = new ItemRequirement("Goblin mail", ItemID.GOBLIN_MAIL).isNotConsumed();
        plainPizza = new ItemRequirement("Plain pizza", ItemID.PLAIN_PIZZA).isNotConsumed();
        woodenShield = new ItemRequirement("Wooden shield", ItemID.WOODEN_SHIELD).isNotConsumed();
        cheese = new ItemRequirement("Cheese", ItemID.CHEESE).isNotConsumed();
    }

    public void setupSteps()
    {
        week1Dig = new DigStep(this, new WorldPoint(2978, 3194, 0), 
            "Dig south-east of Rimmington and north-west of the chapel.", spade, pieDish);

        week2Dig = new DigStep(this, new WorldPoint(2991, 3295, 0),
            "Dig by the entrance to the Air Altar south of Falador.", spade, rawHerring);

        week3Emotes = new EmoteStep(this, "Perform the shrug and cheer emotes east of the Black Knights' Fortress.",
            new WorldPoint(3035, 3518, 0), Arrays.asList("SHRUG", "CHEER"));

        week4Dig = new DigStep(this, new WorldPoint(3235, 3631, 0),
            "Dig outside the Chaos Temple in the Wilderness.", spade, goblinMail);

        finalEmotes = new EmoteStep(this, "Perform the bow, yes and clap emotes between the trees south of Varrock, " +
            "east of the stone circle. Have nothing equipped and only a plain pizza, wooden shield and cheese in your inventory.",
            new WorldPoint(3246, 3362, 0), Arrays.asList("BOW", "YES", "CLAP"), plainPizza, woodenShield, cheese);
    }

    @Override
    public List<ItemRequirement> getItemRequirements()
    {
        return Arrays.asList(spade, pieDish, rawHerring, goblinMail, plainPizza, woodenShield, cheese);
    }

    @Override
    public List<ItemReward> getItemRewards()
    {
        return Arrays.asList(
            new ItemReward("Ornate gloves", ItemID.ORNATE_GLOVES, 1),
            new ItemReward("Ornate boots", ItemID.ORNATE_BOOTS, 1),
            new ItemReward("Ornate legs", ItemID.ORNATE_LEGS, 1),
            new ItemReward("Ornate top", ItemID.ORNATE_TOP, 1),
            new ItemReward("Ornate cape", ItemID.ORNATE_CAPE, 1),
            new ItemReward("Ornate helm", ItemID.ORNATE_HELM, 1)
        );
    }

    @Override
    public List<PanelDetails> getPanels()
    {
        List<PanelDetails> allSteps = new ArrayList<>();
        allSteps.add(new PanelDetails("Week 1", Collections.singletonList(week1Dig), spade, pieDish));
        allSteps.add(new PanelDetails("Week 2", Collections.singletonList(week2Dig), spade, rawHerring));
        allSteps.add(new PanelDetails("Week 3", Collections.singletonList(week3Emotes)));
        allSteps.add(new PanelDetails("Week 4", Collections.singletonList(week4Dig), spade, goblinMail));
        allSteps.add(new PanelDetails("Final Clue", Collections.singletonList(finalEmotes), 
            plainPizza, woodenShield, cheese));
        return allSteps;
    }
}

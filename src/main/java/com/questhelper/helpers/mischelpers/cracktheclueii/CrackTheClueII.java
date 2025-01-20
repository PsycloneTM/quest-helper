/*
 * Copyright (c) 2024, Zoinkwiz <https://github.com/Zoinkwiz>
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
package com.questhelper.helpers.mischelpers.cracktheclueii;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.rewards.ItemReward;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DigStep;
import com.questhelper.steps.EmoteStep;
import com.questhelper.steps.QuestStep;
import com.questhelper.steps.emote.QuestEmote;
import com.questhelper.requirements.Requirement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;
import com.questhelper.requirements.runelite.RuneliteRequirement;
import com.questhelper.requirements.widget.WidgetTextRequirement;
import net.runelite.api.widgets.ComponentID;
import static com.questhelper.requirements.util.LogicHelper.nor;
import com.questhelper.requirements.zone.Zone;
import com.questhelper.requirements.zone.ZoneRequirement;

public class CrackTheClueII extends BasicQuestHelper
{
	ItemRequirement spade, pieDish, rawHerring, goblinMail, plainPizza, woodenShield, cheese;

	QuestStep week1Dig, week2Dig, week3EmoteShrug, week3EmoteCheer, week4Dig, finalEmoteBow, finalEmoteYes, finalEmoteClap;

	ConditionalStep week1Steps, week2Steps, week3Steps, week4Steps, finalSteps;

	Requirement completedWeek1, completedWeek2, completedWeek3, completedWeek4;

	Zone week1Zone, week2Zone, week3ShrugZone, week3CheerZone, week4Zone, finalZone;

	ZoneRequirement inWeek1Zone, inWeek2Zone, inWeek3ShrugZone, inWeek3CheerZone, inWeek4Zone, inFinalZone;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		setupRequirements();
		setupZones();
		setupConditions();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();

		week1Steps = new ConditionalStep(this, week1Dig);
		week1Steps.setLockingCondition(completedWeek1);

		week2Steps = new ConditionalStep(this, week2Dig);
		week2Steps.setLockingCondition(completedWeek2);

		week3Steps = new ConditionalStep(this, week3EmoteShrug);
		week3Steps.addStep(new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "shrug emote"), week3EmoteCheer);
		week3Steps.setLockingCondition(completedWeek3);

		week4Steps = new ConditionalStep(this, week4Dig);
		week4Steps.setLockingCondition(completedWeek4);

		finalSteps = new ConditionalStep(this, finalEmoteBow);
		finalSteps.addStep(new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "bow emote"), finalEmoteYes);
		finalSteps.addStep(new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "yes emote"), finalEmoteClap);

		ConditionalStep allSteps = new ConditionalStep(this, week1Steps);
		allSteps.addStep(nor(completedWeek1), week1Steps);
		allSteps.addStep(nor(completedWeek2), week2Steps);
		allSteps.addStep(nor(completedWeek3), week3Steps);
		allSteps.addStep(nor(completedWeek4), week4Steps);
		allSteps.addStep(nor(completedWeek4), finalSteps);
		allSteps.setCheckAllChildStepsOnListenerCall(true);

		steps.put(0, allSteps);
		steps.put(1, allSteps);
		steps.put(2, allSteps);
		steps.put(3, allSteps);
		steps.put(4, allSteps);
		steps.put(5, allSteps);
		steps.put(6, allSteps);
		steps.put(7, allSteps);

		return steps;
	}

	public void setupConditions()
	{
		completedWeek1 = new RuneliteRequirement(
			getConfigManager(), "cracktheclue2.week1",
			new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "completed Week 1")
		);

		completedWeek2 = new RuneliteRequirement(
			getConfigManager(), "cracktheclue2.week2",
			new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "completed Week 2")
		);

		completedWeek3 = new RuneliteRequirement(
			getConfigManager(), "cracktheclue2.week3",
			new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "completed Week 3")
		);

		completedWeek4 = new RuneliteRequirement(
			getConfigManager(), "cracktheclue2.week4",
			new WidgetTextRequirement(ComponentID.DIARY_TEXT, true, "completed Week 4")
		);

		inWeek1Zone = new ZoneRequirement(week1Zone);
		inWeek2Zone = new ZoneRequirement(week2Zone);
		inWeek3ShrugZone = new ZoneRequirement(week3ShrugZone);
		inWeek3CheerZone = new ZoneRequirement(week3CheerZone);
		inWeek4Zone = new ZoneRequirement(week4Zone);
		inFinalZone = new ZoneRequirement(finalZone);
	}

	@Override
	public void setupRequirements()
	{
		spade = new ItemRequirement("Spade", ItemID.SPADE).isNotConsumed();
		pieDish = new ItemRequirement("Pie dish", ItemID.PIE_DISH).isNotConsumed();
		rawHerring = new ItemRequirement("Raw herring", ItemID.RAW_HERRING).isNotConsumed();
		goblinMail = new ItemRequirement("Goblin mail", ItemID.GOBLIN_MAIL).isNotConsumed();
		plainPizza = new ItemRequirement("Plain pizza", ItemID.PLAIN_PIZZA).isNotConsumed();
		woodenShield = new ItemRequirement("Wooden shield", ItemID.WOODEN_SHIELD).isNotConsumed();
		cheese = new ItemRequirement("Cheese", ItemID.CHEESE).isNotConsumed();
	}

	public void setupZones()
	{
		week1Zone = new Zone(new WorldPoint(2977, 3193, 0), new WorldPoint(2979, 3195, 0));
		week2Zone = new Zone(new WorldPoint(2990, 3294, 0), new WorldPoint(2992, 3296, 0));
		week3ShrugZone = new Zone(new WorldPoint(3034, 3517, 0), new WorldPoint(3036, 3519, 0));
		week3CheerZone = new Zone(new WorldPoint(3034, 3517, 0), new WorldPoint(3036, 3519, 0));
		week4Zone = new Zone(new WorldPoint(3234, 3630, 0), new WorldPoint(3236, 3632, 0));
		finalZone = new Zone(new WorldPoint(3245, 3361, 0), new WorldPoint(3247, 3363, 0));
	}

	public void setupSteps()
	{
		week1Dig = new DigStep(this, new WorldPoint(2978, 3194, 0),
			"Dig south-east of Rimmington and north-west of the chapel.", spade, pieDish);
		week1Dig.addIcon(ItemID.SPADE);

		week2Dig = new DigStep(this, new WorldPoint(2991, 3295, 0),
			"Dig by the entrance to the Air Altar south of Falador.", spade, rawHerring);
		week2Dig.addIcon(ItemID.SPADE);

		week3EmoteShrug = new EmoteStep(this, QuestEmote.SHRUG, new WorldPoint(3035, 3518, 0),
			"Perform the shrug emote east of the Black Knights' Fortress.");
		week3EmoteCheer = new EmoteStep(this, QuestEmote.CHEER, new WorldPoint(3035, 3518, 0),
			"Perform the cheer emote east of the Black Knights' Fortress.");

		week4Dig = new DigStep(this, new WorldPoint(3235, 3631, 0),
			"Dig outside the Chaos Temple in the Wilderness.", spade, goblinMail);

		finalEmoteBow = new EmoteStep(this, QuestEmote.BOW, new WorldPoint(3246, 3362, 0),
			"Perform the bow emote between the trees south of Varrock, east of the stone circle. Have nothing equipped and only a plain pizza, wooden shield and cheese in your inventory.",
			plainPizza, woodenShield, cheese);
		finalEmoteYes = new EmoteStep(this, QuestEmote.YES, new WorldPoint(3246, 3362, 0),
			"Perform the yes emote.", plainPizza, woodenShield, cheese);
		finalEmoteClap = new EmoteStep(this, QuestEmote.CLAP, new WorldPoint(3246, 3362, 0),
			"Perform the clap emote.", plainPizza, woodenShield, cheese);
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
	public List<String> getNotes()
	{
		return Collections.singletonList("If the helper is out of sync, open up the Quest Journal to re-sync it.");
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();

		PanelDetails week1Panel = new PanelDetails("Week 1", Collections.singletonList(week1Dig),
			Arrays.asList(spade, pieDish));
		week1Panel.setLockingStep(week1Steps);
		allSteps.add(week1Panel);

		PanelDetails week2Panel = new PanelDetails("Week 2", Collections.singletonList(week2Dig),
			Arrays.asList(spade, rawHerring));
		week2Panel.setLockingStep(week2Steps);
		allSteps.add(week2Panel);

		PanelDetails week3Panel = new PanelDetails("Week 3", Arrays.asList(week3EmoteShrug, week3EmoteCheer));
		week3Panel.setLockingStep(week3Steps);
		allSteps.add(week3Panel);

		PanelDetails week4Panel = new PanelDetails("Week 4", Collections.singletonList(week4Dig),
			Arrays.asList(spade, goblinMail));
		week4Panel.setLockingStep(week4Steps);
		allSteps.add(week4Panel);

		PanelDetails finalPanel = new PanelDetails("Final Clue",
			Arrays.asList(finalEmoteBow, finalEmoteYes, finalEmoteClap),
			Arrays.asList(plainPizza, woodenShield, cheese));
		finalPanel.setLockingStep(finalSteps);
		allSteps.add(finalPanel);

		return allSteps;
	}
}

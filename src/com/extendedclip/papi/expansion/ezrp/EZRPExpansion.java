/*
 *
 * EzRanksPro-Expansion
 * Copyright (C) 2020 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.extendedclip.papi.expansion.ezrp;

import me.clip.ezrankspro.EZRanksPro;
import me.clip.ezrankspro.multipliers.CostHandler;
import me.clip.ezrankspro.rankdata.Rankup;
import me.clip.ezrankspro.util.EcoUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class EZRPExpansion extends PlaceholderExpansion {

	@Override
	public boolean canRegister() {
		return Bukkit.getPluginManager().getPlugin("EZRanksPro") != null;
	}
	
	@Override
	public boolean register() {
		return Bukkit.getPluginManager().getPlugin("EZRanksPro").isEnabled() && super.register();
	}

	@Override
	public String getAuthor() {
		return "clip";
	}

	@Override
	public String getIdentifier() {
		return "ezrankspro";
	}

	@Override
	public String getVersion() {
		return "1.2.0";
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		String rank;
		Rankup r;

		if (identifier.startsWith("rank_cost_formatted_")) {
			rank = identifier.replace("rank_cost_formatted_", "");

			r = Rankup.getRankup(rank);

			return r != null ? EcoUtil.fixMoney(r.getCost()) : null;
		}

		if (identifier.startsWith("rank_cost_")) {
			rank = identifier.replace("rank_cost_", "");

			r = Rankup.getRankup(rank);

			return r != null ? String.valueOf(r.getCost()) : null;
		}

		if (identifier.startsWith("rank_prefix_")) {
			rank = identifier.replace("rank_prefix_", "");

			r = Rankup.getRankup(rank);

			return r != null ? r.getPrefix() : null;
		}

		if (identifier.startsWith("rankup_rank_prefix_")) {

			rank = identifier.replace("rankup_rank_prefix_", "");

			r = Rankup.getRankup(rank);

			if (r == null)
				return null;

			String rRank = r.getRankup();

			Rankup to = Rankup.getRankup(rRank);

			if (to != null)
				return to.getPrefix();

			if (Rankup.isLastRank(rRank))
				return Rankup.getLastRank().getPrefix();

			return null;
		}

		if (identifier.startsWith("rankup_rank_")) {
			rank = identifier.replace("rankup_rank_", "");

			r = Rankup.getRankup(rank);

			return r != null ? r.getRankup() : null;
		}

		if (!player.isOnline())
			return null;

		Player p = player.getPlayer();

		if (p == null)
			return null;

		r = Rankup.getRankup(p);

		double cost = 0.0;
		double balance = EZRanksPro.getInstance().getEconomy().getBalance(p);

		switch (identifier) {

			case "player":
				return p.getName();
			case "displayname":
				return p.getDisplayName();
			case "world":
				return p.getWorld().getName();
			case "rank":
			case "rankfrom":
			case "currentrank":
				if (r != null && r.getRank() != null)
					return r.getRank();

				if (Rankup.isLastRank(p) && Rankup.getLastRank() != null && Rankup.getLastRank().getRank() != null)
					return Rankup.getLastRank().getRank();

				String group = EZRanksPro.getInstance().getVaultPerms().getPrimaryGroup(p);
				return group != null ? group : "unknown";
			case "nextrank":
			case "rankto":
			case "rankup":
				return r != null && r.getRankup() != null ? r.getRankup() : "none";
			case "cost":
			case "rankup_cost":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return String.valueOf((long) cost);
			case "cost_formatted":
			case "rankup_cost_formatted":
				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return EcoUtil.fixMoney(cost);
			case "difference":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}
				return String.valueOf((long) EcoUtil.getDifference(balance, cost));
			case "difference_formatted":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return EcoUtil.fixMoney(EcoUtil.getDifference(balance, cost));
			case "progress":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return EcoUtil.getProgress(balance, cost);
			case "progressbar":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return EcoUtil.getProgressBar(EcoUtil.getProgressInt(balance, cost));
			case "progressexact":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				return EcoUtil.getProgressExact(balance, cost);
			case "balance":
				return String.valueOf((long) balance);
			case "balance_formatted":
				return EcoUtil.fixMoney(balance);
			case "rankprefix":
			case "rank_prefix":
				if (r != null && r.getPrefix() != null)
					return r.getPrefix();

				if (Rankup.isLastRank(p) && Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null)
					return Rankup.getLastRank().getPrefix();

				return "";
			case "lastrank":
			case "last_rank":
				return Rankup.getLastRank() != null && Rankup.getLastRank().getRank() != null ?
						Rankup.getLastRank().getRank() : "";
			case "lastrank_prefix":
			case "lastrankprefix":
				return Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null ?
						Rankup.getLastRank().getPrefix() : "";
			case "rankupprefix":
			case "rankup_prefix":
			case "nextrank_prefix":
				if (r == null)
					return "";

				if (Rankup.getRankup(r.getRankup()) == null)
					return Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null ?
							Rankup.getLastRank().getPrefix() : "";

				return Rankup.getRankup(r.getRankup()).getPrefix() != null ?
						Rankup.getRankup(r.getRankup()).getPrefix() : "";
		}

		return null;

	}


	
	
}

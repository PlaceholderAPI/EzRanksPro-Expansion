package com.extendedclip.papi.expansion.ezrp;

import me.clip.ezrankspro.EZRanksPro;
import me.clip.ezrankspro.multipliers.CostHandler;
import me.clip.ezrankspro.rankdata.Rankup;
import me.clip.ezrankspro.util.EcoUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class EZRPExpansion extends PlaceholderExpansion {

	@Override
	public boolean canRegister() {
		return Bukkit.getPluginManager().getPlugin("EZRanksPro") != null;
	}
	
	@Override
	public boolean register() {
		
		if (!canRegister()) {
			return false;
		}
		
		return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
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
	public String getPlugin() {
		return "EZRanksPro";
	}

	@Override
	public String getVersion() {
		return "1.1.0";
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) {

		if (p == null) {
			return "";
		}
		
		 if (identifier.startsWith("rank_cost_formatted_")) {
				String rank = identifier.replace("rank_cost_formatted_", "");
				
				Rankup r = Rankup.getRankup(rank);
				
				if (r == null) {
					return null;
				}
				
				return EcoUtil.fixMoney(r.getCost());
		} else if (identifier.startsWith("rank_cost_")) {
			String rank = identifier.replace("rank_cost_", "");
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				return null;
			}
			
			return r.getCost()+"";
		} else if (identifier.startsWith("rank_prefix_")) {
			String rank = identifier.replace("rank_prefix_", "");
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				return null;
			}
			
			return r.getPrefix();
		} else if (identifier.startsWith("rankup_rank_")) {
			String rank = identifier.replace("rankup_rank_", "");
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				return null;
			}
			
			return r.getRankup();
		} else if (identifier.startsWith("rankup_rank_prefix_")) {
			String rank = identifier.replace("rankup_rank_prefix_", "");
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				return null;
			}
			
			String rRank = r.getRankup();
			
			Rankup to = Rankup.getRankup(rRank);

			if (to != null) {
				return r.getPrefix();
			}
			
			if (Rankup.isLastRank(rRank)) {
				return Rankup.getLastRank().getPrefix();
			}
			return null;
		}
		
		Rankup r = Rankup.getRankup(p);
		
		String replacement = null;
		
		double cost = 0.0;
		
		switch (identifier) {

		case "player":
			replacement = p.getName();
			break;
		case "displayname":
			replacement = p.getDisplayName();
			break;
		case "world":
			replacement = p.getWorld().getName();
			break;
		case "rank":
		case "rankfrom":
		case "currentrank":
			replacement = r != null && r.getRank() != null ? r.getRank()
					: Rankup.isLastRank(p) && Rankup.getLastRank() != null
							&& Rankup.getLastRank().getRank() != null ? Rankup
							.getLastRank().getRank() : EZRanksPro.getInstance().getPerms()
							.getPrimaryGroup(p) != null ? EZRanksPro.getInstance().getPerms()
							.getPrimaryGroup(p) : "unknown";
			break;
		case "nextrank":
		case "rankto":
		case "rankup":
			replacement = r != null && r.getRankup() != null ? r
					.getRankup() : "none";
			break;
		case "cost":
		case "rankup_cost":
			

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			long send = (long) cost;

			replacement = String.valueOf(send);
			break;
		case "cost_formatted":
		case "rankup_cost_formatted":
			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = EcoUtil.fixMoney(cost);
			break;
		case "difference":

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = String.valueOf(EcoUtil.getDifference(EZRanksPro.getInstance().getEconomy().getBalance(p), cost));
			break;
		case "difference_formatted":

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = EcoUtil.fixMoney(EcoUtil.getDifference(EZRanksPro.getInstance().getEconomy().getBalance(p), cost));
			break;
		case "progress":

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = EcoUtil.getProgress(EZRanksPro.getInstance().getEconomy().getBalance(p), cost);
			break;
		case "progressbar":

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = EcoUtil.getProgressBar(EcoUtil.getProgressInt(EZRanksPro.getInstance().getEconomy().getBalance(p), cost));
			break;
		case "progressexact":

			if (r != null) {

				cost = r.getCost();

				cost = CostHandler.getMultiplier(p, cost);

				cost = CostHandler.getDiscount(p, cost);
			}

			replacement = EcoUtil.getProgressExact(EZRanksPro.getInstance().getEconomy().getBalance(p), cost);
			break;
		case "balance":
			replacement = String.valueOf(EZRanksPro.getInstance().getEconomy().getBalance(p));
			break;
		case "balance_formatted":
			replacement = EcoUtil.fixMoney(EZRanksPro.getInstance().getEconomy().getBalance(p));
			break;
		case "rankprefix":
		case "rank_prefix":
			replacement = r != null && r.getPrefix() != null ? r
					.getPrefix() : Rankup.isLastRank(p)
					&& Rankup.getLastRank() != null
					&& Rankup.getLastRank().getPrefix() != null ? Rankup
					.getLastRank().getPrefix() : "";
			break;
		case "lastrank":
		case "last_rank":
			replacement = Rankup.getLastRank() != null
					&& Rankup.getLastRank().getRank() != null ? Rankup
					.getLastRank().getRank() : "";
			break;
		case "lastrank_prefix":
		case "lastrankprefix":
			replacement = Rankup.getLastRank() != null
					&& Rankup.getLastRank().getPrefix() != null ? Rankup
					.getLastRank().getPrefix() : "";
			break;
		case "rankupprefix":
		case "rankup_prefix":
		case "nextrank_prefix":
			if (r == null) {
				replacement = "";
				break;
			}
			if (Rankup.getRankup(r.getRankup()) == null) {
				replacement = Rankup.getLastRank() != null
						&& Rankup.getLastRank().getPrefix() != null ? Rankup
						.getLastRank().getPrefix() : "";
				break;
			}

			replacement = Rankup.getRankup(r.getRankup()).getPrefix() != null ? Rankup
					.getRankup(r.getRankup()).getPrefix() : "";
			break;
		}
		
		return replacement;
	
	}
	
	
	
	
}

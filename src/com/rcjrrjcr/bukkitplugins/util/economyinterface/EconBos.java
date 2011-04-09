package com.rcjrrjcr.bukkitplugins.util.economyinterface;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import cosine.boseconomy.BOSEconomy;

public class EconBos implements IEconHandler {

	private BOSEconomy plugin;
//	private RcjrPlugin origin;
	
	public EconBos(Plugin plugin) throws Exception {
		if(!(plugin instanceof BOSEconomy)) throw new Exception("BuyAbilities: Wrong economy plugin passed to BOSEconomy interface constructor!");
		this.plugin = (BOSEconomy) plugin;
//		this.origin = origin;
	}

	@Override
	public double getBalance(Player player) {
		return getBalance(player.getName());
	}

	@Override
	public boolean deduct(Player player, Integer cost) {
		return deduct(player.getName(),cost);
	}

	@Override
	public void add(Player player, Integer cost) {
		add(player.getName(),cost);
	}

	@Override
	public double getBalance(String playerName) {
		return plugin.getPlayerMoney(playerName);
	}

	@Override
	public boolean deduct(String playerName, Integer cost) {
		if(getBalance(playerName) >= cost)
		{
			plugin.addPlayerMoney(playerName, -cost, false);
			return true;
		}

		return false;
	}

	@Override
	public void add(String playerName, Integer cost) {
		plugin.addPlayerMoney(playerName, cost, false);
	}
	

}

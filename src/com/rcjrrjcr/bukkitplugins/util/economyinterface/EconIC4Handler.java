package com.rcjrrjcr.bukkitplugins.util.economyinterface;

import org.bukkit.entity.Player;

import com.nijiko.coelho.iConomy.iConomy;
import com.rcjrrjcr.bukkitplugins.util.RcjrPlugin;

public class EconIC4Handler implements IEconHandler {
	
	//private BuyAbilities origin;
	public EconIC4Handler(RcjrPlugin origin) {
	//	this.origin = origin;
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
	public double getBalance(String playerName) {
		if(iConomy.getBank().hasAccount(playerName))
		{
			return iConomy.getBank().getAccount(playerName).getBalance();
		}
		return -1.0d;
	}

	@Override
	public boolean deduct(String playerName, Integer cost) {
		if(getBalance(playerName) >= cost)
		{
			iConomy.getBank().getAccount(playerName).subtract(cost);
			return true;
		}
		
		return false;
	}

	@Override
	public void add(Player player, Integer cost) {
		add(player.getName(),cost);
	}

	@Override
	public void add(String playerName, Integer cost) {
			iConomy.getBank().getAccount(playerName).add(cost);
	}

}

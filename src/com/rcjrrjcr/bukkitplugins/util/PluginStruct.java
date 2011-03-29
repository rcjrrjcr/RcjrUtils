package com.rcjrrjcr.bukkitplugins.util;

public class PluginStruct {
	private boolean perm;
	private boolean econ;
	public PluginStruct(boolean perm, boolean econ)
	{
		this.setPerm(perm);
		this.setEcon(econ);
	}
	public boolean getStatus()
	{
		return isPermActive()&&isEconActive();
	}
	public boolean isPermActive() {
		return perm;
	}
	public void setPerm(boolean perm) {
		this.perm = perm;
	}
	public boolean isEconActive() {
		return econ;
	}
	public void setEcon(boolean econ) {
		this.econ = econ;
	}
}

/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */

package me.fromgate.reactions.externals;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.util.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

@UtilityClass
public class RaEconomics {
    public boolean isEconomyFound() {
        return RaVault.isEconomyConnected();
    }

    public boolean hasMoney(String account, double amount, String worldName) {
        if (RaVault.isEconomyConnected()) return RaVault.hasMoney(account, worldName, amount);
        return false;
    }


    public String creditAccount(String target, String source, String amountStr, String worldName) {
        if (target.isEmpty()) return "";
        if (!NumberUtils.isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RaVault.isEconomyConnected()) {
            if (RaVault.creditAccount(target, source, amount, worldName))
                return RaVault.format(amount, worldName);
        }
        return "";
    }

    public String debitAccount(String accountFrom, String accountTo, String amountStr, String worldName) {
        if (accountFrom.isEmpty()) return "";
        if (!NumberUtils.isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RaVault.isEconomyConnected()) {
            if (RaVault.debitAccount(accountFrom, accountTo, amount, worldName))
                return RaVault.format(amount, worldName);
        }
        return "";
    }

    public Map<String, String> getBalances(Player p) {
        if (RaVault.isEconomyConnected()) return RaVault.getAllBalances(p.getName());
        return Collections.emptyMap();
    }

    public String format(double amount, String worldName) {
        if (RaVault.isEconomyConnected())
            return RaVault.format(amount, worldName.isEmpty() ? Bukkit.getWorlds().get(0).getName() : worldName);
        return Double.toString(amount);
    }

}

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

package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.SignActivator;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SignStorage extends Storage {

    private final boolean leftClick;
    private final Location location;
    private final String[] signLines;

    public SignStorage(Player player, String[] signLines, Location loc, boolean leftClick) {
        super(player);
        this.signLines = signLines;
        this.location = loc;
        this.leftClick = leftClick;
    }

    @Override
    public Class<? extends Activator> getType() {
        return SignActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        for (int i = 0; i < signLines.length; i++)
            tempVars.put("sign_line" + (i + 1), signLines[i]);
        tempVars.put("sign_loc", LocationUtils.locationToString(location));
        tempVars.put("click", leftClick ? "left" : "right");
        return tempVars;
    }
}

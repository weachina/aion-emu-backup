/*
 * This file is part of aion-emu.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.commons.scripting.scriptmanager;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Root element for script descriptors
 *
 * @author SoulKeeper
 */
@XmlRootElement(name = "scriptlist")
public class ScriptList {

    /**
     * List of Script descriptors
     */
    @XmlElement(name = "scriptinfo", type = ScriptInfo.class)
    private Set<ScriptInfo> scriptInfos;

    /**
     * Returns list of script descriptors
     *
     * @return list of script descriptors
     */
    @XmlTransient
    public Set<ScriptInfo> getScriptInfos() {
        return scriptInfos;
    }

    /**
     * Sets list of script descriptors
     *
     * @param scriptInfos lisft of script descriptors
     */
    public void setScriptInfos(Set<ScriptInfo> scriptInfos) {
        this.scriptInfos = scriptInfos;
	}
}

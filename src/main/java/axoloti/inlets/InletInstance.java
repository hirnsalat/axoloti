/**
 * Copyright (C) 2013, 2014, 2015 Johannes Taelman
 *
 * This file is part of Axoloti.
 *
 * Axoloti is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Axoloti is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Axoloti. If not, see <http://www.gnu.org/licenses/>.
 */
package axoloti.inlets;

import axoloti.atom.AtomDefinition;
import axoloti.atom.AtomDefinitionController;
import axoloti.atom.AtomInstance;
import axoloti.datatypes.DataType;
import axoloti.mvc.AbstractModel;
import axoloti.object.IAxoObjectInstance;
import java.beans.PropertyChangeEvent;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persist;

/**
 *
 * @author Johannes Taelman
 */
@Root(name = "dest")
public class InletInstance<T extends Inlet> extends AbstractModel implements AtomInstance<T> {

    @Attribute(name = "inlet", required = false)
    protected String inletname;
    @Deprecated
    @Attribute(required = false)
    protected String name;
    @Attribute(name = "obj", required = false)
    protected String objname;

    private final AtomDefinitionController controller;

    protected IAxoObjectInstance axoObj;

    public String getInletname() {
        return inletname;
    }

    public IAxoObjectInstance getObjectInstance() {
        return axoObj;
    }

    @Persist
    public void Persist() {
        objname = axoObj.getInstanceName();
    }

    @Override
    public T getModel() {
        return (T) getController().getModel();
    }

    public InletInstance() {
        axoObj = null;
        controller = null;
    }

    public InletInstance(String objname, String inletname) {
        axoObj = null;
        controller = null;
        this.objname = objname;
        this.inletname = inletname;
    }

    public InletInstance(AtomDefinitionController inletController, final IAxoObjectInstance axoObj) {
        this.controller = inletController;
        this.axoObj = axoObj;
        RefreshName();
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{
            AtomDefinition.ATOM_NAME,
            AtomDefinition.ATOM_DESCRIPTION
        };
    }

    public DataType getDataType() {
        return getModel().getDatatype();
    }

    public String GetCName() {
        return getModel().GetCName();
    }

    public String GetLabel() {
        return getModel().getName();
    }

    public void RefreshName() {
        if (axoObj != null) {
            name = axoObj.getInstanceName() + " " + getModel().getName();
            objname = axoObj.getInstanceName();
            name = null;
        }
    }

    public String getObjname() {
        if (axoObj != null) {
            return axoObj.getInstanceName();
        } else {
            return this.objname;
        }
    }

    public boolean isConnected() {
        if (axoObj == null) {
            return false;
        }
        return false;
        //FIXME: return (axoObj.getPatchModel().GetNet(this) != null);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(AtomDefinition.ATOM_NAME)) {
            setName((String) evt.getNewValue());
        } else if (evt.getPropertyName().equals(AtomDefinition.ATOM_DESCRIPTION)) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    @Override
    public AtomDefinitionController getController() {
        return controller;
    }

    public String getName() {
        return inletname;
    }

    public void setName(String inletname) {
        String preVal = this.inletname;
        this.inletname = inletname;
        firePropertyChange(AtomDefinition.ATOM_NAME, preVal, inletname);
    }

    public String getDescription() {
        return getModel().getDescription();
    }
}

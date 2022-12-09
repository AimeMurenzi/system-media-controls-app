/**
 * @Author: Aimé
 * @Date:   2022-04-08 20:19:07
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-08-26 15:47:34
 */

package be.freeaime.util;

import java.io.Serializable;

public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean editable;
    private String value;

    public Setting() {
    }

    public Setting(String value) {
        this.value = value;
        this.editable = true;
    }

    public Setting(boolean editable, String value) {
        this.editable = editable;
        this.value = value;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

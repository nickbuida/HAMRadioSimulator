package edu.augustana;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

//import static com.sun.javafx.css.StyleClassSet.getStyleClass;

public class KnobControl extends Control {
    private DoubleProperty value = new SimpleDoubleProperty(0);
    public final KnobControlSkin skin = new KnobControlSkin(this);

    public KnobControl() {
        getStyleClass().add("knob-control");
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(double value){

        this.value.set(value);
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return skin;
    }
}

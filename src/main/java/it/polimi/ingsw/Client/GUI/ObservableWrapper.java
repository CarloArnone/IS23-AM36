package it.polimi.ingsw.Client.GUI;

import javafx.beans.property.SimpleObjectProperty;

public class ObservableWrapper<T> extends SimpleObjectProperty<T> {
    public ObservableWrapper(T object) {
        super(object);
    }

    @Override
    public T get() {
        return super.get();
    }

    @Override
    public void set(T t) {
        super.set(t);
    }
}

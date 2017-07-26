package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmObject;

public class Enemy extends RealmObject{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package de.thbingen.movs.lukas.a4gewinntspezial;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 13.04.2017
 *          <p>
 *          Die Klasse Migration ist dafür verantwortlich, dass beim Laden der Realmdaten aus der
 *          Datenbank die Objekte verändert werden, die nicht auf dem aktuellsten Stand der Klassen-
 *          definition sind.
 */
class Migration implements RealmMigration {

    /**
     * Die Methode migriert die alten Objekte in der Datenbank auf den neuesten Stand. Praktisch
     * werden hier die Schamata und Tabellen angepasst, die aufgrund einer Veränderung an der
     * Klassenstruktur veraltet sind.
     *
     * @param realm Der Datenbankzugriff
     * @param oldVersion Der aktuelle Stand der Datenbank
     * @param newVersion Der neue Stand der Datenbank
     */
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // TODO: Wenn Veränderungen an der Datenstruktur vorgenommen werden
    }

}

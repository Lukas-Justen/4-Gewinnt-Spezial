package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse TextWatcherAdapter implementiert lediglich die Methoden beforeTextChanged &
 *          afterTextChanged des Interfaces TextWatcher leer, damit der Code zum überwachen der
 *          Eingabe eines Textfeldes küzer wird.
 */
public abstract class TextWatcherAdapter implements TextWatcher {

    // Muss implenentiert werden zur Überwachung der Eingabe ins Textfeld
    public abstract void onTextChanged(CharSequence s, int start, int before, int count);

    // Werden nicht benötigt daher leer implentiert
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }

}

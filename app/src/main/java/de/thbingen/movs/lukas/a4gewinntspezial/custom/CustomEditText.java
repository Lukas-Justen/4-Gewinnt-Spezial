package de.thbingen.movs.lukas.a4gewinntspezial.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Sobald man in diesen EditText einen Text eingibt färbt sich die Linie auf die man den
 *          Text schreibt. Dadurch sieht der EditText nach einer Eingabe und Auswahl der passenden
 *          Tauschfarben wie ein TextView aus. Der CustomEditText ermöglicht es beim Anlegen im XML
 *          über das Attribut "exchangeColor" die Farbe festzulegen, die die Linie annehmen soll,
 *          wenn ein Text eingegeben wurde. Diese Farbe ist im Idealfall gleich der
 *          Hintergrundfarbe. Übeer das Attribut "standardColor" kann ausgewählt werden, welche
 *          Farbe die Linie bei leerer Eingabe annehmen soll.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    // Die Farben durch die die Linie ersetzt werden sollen
    private int exchangeColor;
    private int standardColor;

    /**
     * 1. Custom Ctor legt nur ein CustomEditText ohne spezielle Attribute an
     *
     * @param context Der Context des CustomViews
     */
    public CustomEditText(Context context) {
        super(context);
    }

    /**
     * 2. Custom Ctor legt einen neuen CustomEditText mit den Attributen aus dem XML und der
     * exchangeColor an
     *
     * @param context Der Context des CustomViews
     * @param attrs   Die übergebenen Attribute
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditText,
                0, 0);

        try {
            exchangeColor = a.getColor(R.styleable.CustomEditText_exchangeColor, getResources().getColor(R.color.windowBackgroundColor));
            standardColor = a.getColor(R.styleable.CustomEditText_standardColor, getResources().getColor(R.color.colorPrimaryDark));
        } finally {
            a.recycle();
        }

        getBackground().mutate().setColorFilter(standardColor, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * 3. Custom Ctor legt einen neuen CustomEditText mit den Attributen aus dem XML und der
     * exchangeColor an. Erlaubt zusätzlich ein StyleAttribut.
     *
     * @param context      Der Context des CustomViews
     * @param attrs        Die übergebenen Attribute
     * @param defStyleAttr das StyleAttribut
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditText,
                0, 0);

        try {
            exchangeColor = a.getColor(R.styleable.CustomEditText_exchangeColor, getResources().getColor(R.color.windowBackgroundColor));
            standardColor = a.getColor(R.styleable.CustomEditText_standardColor, getResources().getColor(R.color.colorPrimaryDark));
        } finally {
            a.recycle();
        }

        getBackground().mutate().setColorFilter(standardColor, PorterDuff.Mode.SRC_ATOP);
    }


    /**
     * Wird automatisch aufgerufen sobald sich der Text in dem CustomEditText ändert. Wenn das der
     * Fall ist wird die Linie des EditTexts entsprechend angepasst.
     *
     * @param text Der Text nach der Veränderung
     * @param start Start der Veränderung
     * @param lengthBefore Länge vor der Veränderung
     * @param lengthAfter Länge nach der Veränderung
     */
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (text.length() > 0) {
            getBackground().mutate().setColorFilter(exchangeColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            getBackground().mutate().setColorFilter(standardColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

}


package Expr;

public class Token {
    // Para el NOMBRE del token. No modifique estos valores.
    public static final int FIN = 0;
    public static final int ERROR = 1;
    public static final int PA = 2; // "("
    public static final int PC = 3; // ")"
    public static final int MAS = 4;
    public static final int MENOS = 5;
    public static final int POR = 6;
    public static final int DIV = 7;
    public static final int COMA = 8; // ","
    public static final int NUM = 9;
    public static final int IDFUNC = 10;

    // Campos de la class: <nom, atr>
    private int nom;
    private float atr;

    public Token() {
        this(FIN);
    }

    public Token(int nombre) {
        this(nombre, 0);
    }

    public Token(int nombre, float atributo) {
        nom = nombre;
        atr = atributo;
    }

    public void set(int nombre, float atributo) {
        nom = nombre;
        atr = atributo;
    }

    public void set(Token t) {
        set(t.nom, t.atr);
    }

    public void setNom(int nom) {
        this.nom = nom;
    }

    public void setAtr(float atr) {
        this.atr = atr;
    }

    public int getNom() {
        return nom;
    }

    public float getAtr() {
        return atr;
    }

    /**
     * @return Devuelve el lexema que caracteriza a éste token
     */
    public String getLexem() {
        try {
            return LEXEM[nom];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param cc dado el char cc, no forma un token
     * @return devuelve el nombre del token (solo para tokens menosímbols
     */
    public static int getNomToken(char cc) {
        for (int i = 0; i < LEXEM.length; i++) {
            if (LEXEM[i].length() == 1 && LEXEM[i].charAt(0) == cc)
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String atrS = (nom == NUM || nom == IDFUNC ? niceFloat(atr) : "_");
        return "<" + getNomStr(nom) + "," + atrS + ">";
    }

    private String getNomStr(int i) {
        try {
            return NOMtokenSTR[i];
        } catch (Exception e) {
            return DESCONOCIDO;
        }
    }

    /**
     * Quita el punto decimal (si es posible)
     * 
     * @param r
     * @return
     */
    private String niceFloat(float r) {
        int e = (int) r;
        if (e == r)
            return "" + e;

        return "" + r;
    }

    private static final String DESCONOCIDO = "??";

    private static final String NOMtokenSTR[] = {
            "FIN", "ERROR", "PA", "PC", "MAS", "MENOS", "POR", "DIV", "COMA",
            "NUM", "IDFUNC"
    };

    private static final String LEXEM[] = {
            "", "", "(", ")", "+", "-", "*", "/", ",", "", ""
    };
}

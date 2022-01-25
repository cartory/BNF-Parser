package Expr;

public class Parser {
    private PError error;
    private Cinta cinta;
    private Analex analex;

    public Parser() {
        error = new PError();
        cinta = new Cinta();
        analex = new Analex(cinta, error);
    }

    public boolean hayError() {
        return error.hayError();
    }

    public String getErrorMsj() {
        return error.getErrorMsj();
    }

    public float evaluar(String expresion) {
        error.init();
        cinta.init(expresion);
        analex.init();
        if (analex.preNom() == Token.FIN)
            return 0; // La expresion está vacía.

        return expresion(); // Llamar al símbolo inicial.
    }

    /**
     * Expr -> Term E1
     * 
     * @return float
     */
    private float expresion() {
        float t = termino();
        float s = e1(t);

        return s;
    }

    /**
     * E1 -> expresion | λ
     * 
     * @return float
     */
    private int[] CF_E1 = {
            Token.NUM, Token.IDFUNC, Token.MENOS, Token.MAS, Token.POR, Token.DIV,
    };

    boolean CF_E1_contains(int token) {
        for (int cf_token : CF_E1) {
            if (cf_token == token) {
                return true;
            }
        }
        return false;
    }

    private float e1(float a) {
        float s = a;
        if (CF_E1_contains(analex.Preanalisis().getNom())) {
            float e = expresion();
            s = s + e;
        }

        return s;
    }

    /**
     * termino -> factor T1
     * 
     * @return float
     */
    private float termino() {
        float a = factor();
        float p = t1(a);
        return p;
    }

    /**
     * T1 -> * termino | / termino | λ
     * 
     * @return float
     */
    private float t1(float a) {
        float b;
        if (analex.Preanalisis().getNom() == Token.POR) {
            match(Token.POR);
            b = termino();
        } else if (analex.Preanalisis().getNom() == Token.DIV) {
            match(Token.DIV);
            b = 1 / termino();
        } else {
            b = 1;
        }

        return a * b;
    }

    /**
     * factor -> ID | NUM | IDFunc(Expr, Expr) | - termino | + termino
     * 
     * @return
     */

    private float getFunc(String funct, float x, float b) {
        if (funct.equals("sen")) {
            return (float) Math.sin(Math.PI / 180 * x);
        }

        if (funct.equals("cos")) {
            return (float) Math.cos(Math.PI / 180 * x);
        }

        if (funct.equals("sqrt")) {
            return (float) Math.sqrt(x);
        }

        return (float) (Math.log(x) / Math.log(b));
    }

    private float factor() {
        float f;
        if (analex.Preanalisis().getNom() == Token.NUM) {
            f = analex.Preanalisis().getAtr();
            match(Token.NUM);
        } else if (analex.Preanalisis().getNom() == Token.IDFUNC) {
            String function = analex.lexema();
            match(Token.IDFUNC);
            match(Token.PA);
            float param1 = expresion();

            if (function.equals("logb")) {
                match(Token.COMA);
                float param2 = expresion();
                f = getFunc(function, param1, param2);
            } else {
                f = getFunc(function, param1, 0);
            }

            match(Token.PC);
        } else if (analex.Preanalisis().getNom() == Token.MENOS) {
            match(Token.MENOS);
            f = -termino();
        } else {
            match(Token.MAS);
            f = termino();
        }

        return f;
    }

    private void match(int nomToken) {
        match(nomToken, "Error de Sintaxis");
    }

    private void match(int nomToken, String msj) {
        if (analex.Preanalisis().getNom() == nomToken)
            analex.avanzar();
        else
            setError(msj);
    }

    private void setError(String msj) {
        error.setError(msj, analex.getPosLexema(), analex.lexema());
    }

    // ----------------- Para resaltar el Error en el Form
    // --------------------------
    public void comunicarError() {
        if (hayError())
            comunicarError(getErrorMsj(), error.getPosLexema(), error.getLexema());
    }

    public void comunicarError(String errorMsj, int pos, String lexema) {
        // Overridable.
    }
}

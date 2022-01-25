package Expr;

public class Analex {
    private PError error;
    private Cinta M;
    private Token R;
    private String ac;
    private int pos; // Posición de inicio del lexema del preanalisis(), calculado en el dt().

    public Analex(Cinta c, PError err) {
        error = err;
        M = c;
        R = new Token();
        init();
    }

    public final void init() {
        M.init();
        avanzar(); // Calcular el primer preanalisis.
    }

    public Token Preanalisis() {
        return R;
    }

    public int preNom() { // Devuelve el campo nombre del Preanalisis()
        return Preanalisis().getNom();
    }

    public String lexema() {
        return ac;
    }

    public int getPosLexema() {
        return pos;
    }

    public void avanzar() {
        dt();
    }

    private void dt() {
        int estado = 0;
        int nom;
        ac = "";

        // Quitar espacios iniciales
        while (isEspacio(M.cc())) {
            M.avanzar();
        }

        pos = M.getPos(); // Posición de inicio del lexem que se formará en ac.

        while (true) {
            char c = M.cc();

            switch (estado) {
                case 0:
                    if (c == Cinta.EOF) {
                        R.setNom(Token.FIN);
                        return;
                    }

                    ac = "" + c; // Empezar a formar el lexem

                    if ((nom = Token.getNomToken(c)) != -1) { // M.cc() es un token monosímbolo
                        R.setNom(nom);
                        M.avanzar();
                        return;
                    }

                    if (Character.isDigit(c)) {
                        M.avanzar();
                        estado = 10;
                    } else if (c == '.') {
                        M.avanzar();
                        estado = 20;
                    } else if (isLetra(c)) {
                        M.avanzar();
                        estado = 30; // CONST o IDFUNC
                    } else {
                        error.setError("Char no permitido: '" + ac + "'", pos, ac);
                        R.setNom(Token.ERROR);
                        M.avanzar();
                        return;
                    }
                    break;

                case 10: // Secuenciar parte entera del NUMR
                    while (Character.isDigit(M.cc())) {
                        ac += M.cc();
                        M.avanzar();
                    }

                    if (M.cc() == '.') {
                        ac += ".";
                        M.avanzar();
                        estado = 20;
                    } else {
                        R.set(Token.NUM, Integer.parseInt(ac));
                        return;
                    }
                    break;

                case 20: // Secuenciar la parte decimal del NUMR
                    while (Character.isDigit(M.cc())) {
                        ac += M.cc();
                        M.avanzar();
                    }

                    if (ac.length() == 1) {
                        error.setError("El '.' no es aceptado como un número", pos, ac);
                        R.setNom(Token.ERROR); // ac='.'
                    } else
                        R.set(Token.NUM, Float.parseFloat(ac));
                    return;

                case 30: // Secuenciar una conbinación de Letras y/o Digitos
                    while (Character.isDigit(M.cc()) || isLetra(M.cc())) {
                        ac += M.cc();
                        M.avanzar();
                    }

                    Token t = isKeyWord(ac);
                    if (t != null)
                        R.set(t); // Se reconoció una CONST o una IDFUNC
                    else {
                        error.setError(ac + ": Función desconocida", pos, ac);
                        R.setNom(Token.ERROR);
                    }

                    return;
            } // End switch
        }
    }

    private boolean isEspacio(char cc) {
        return (cc == Cinta.EOLN || cc == 32 || cc == 9);
    }

    private boolean isLetra(char cc) {
        cc = Character.toUpperCase(cc);
        return ('A' <= cc && cc <= 'Z');
    }

    // ------------------------------------------------------------------------------
    private Token aux;

    private Token isKeyWord(String ac) { // A manera de TPC
        ac = ac.toUpperCase();

        if (aux == null)
            aux = new Token();

        int i;
        String nom;
        for (i = 0; i < CONST.length; i += 2) { // Verificar si ac es una const
            nom = (String) CONST[i];
            if (nom.equals(ac)) {
                aux.set(Token.NUM, (float) CONST[i + 1]);
                return aux;
            }
        }

        for (i = 0; i < FUNC.length; i += 2) { // Verificar si ac es una función
            nom = (String) FUNC[i];
            if (nom.equals(ac)) {
                aux.set(Token.IDFUNC, (int) FUNC[i + 1]); // <IDFUNC, cant_parametros>
                return aux;
            }
        }

        return null;
    }

    private static final Object CONST[] = { // Tabla de Constantes
            "PI", 3.141592f,
            "E", 2.718281f // Número de Euler
    };

    private static final Object FUNC[] = {
            "SEN", 1, // Nombre función, cantidad de parámetros
            "COS", 1,
            "SQRT", 1, // Raíz cuadrada
            "LOGB", 2 // log(base, valor)
    };
}

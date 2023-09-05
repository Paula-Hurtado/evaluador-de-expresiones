/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Departamento de Tecnologías de la Información y Comunicaciones
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Proyecto Evaluador de Expresiones Postfijas
 * Fecha: Febrero 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

/**
 * Esta clase representa una clase que evalúa expresiones en notación polaca o
 * postfija. Por ejemplo: 4 5 +
 */
public class EvaluadorPostfijo {

    /**
     * Permite saber si la expresión en la lista está balanceada
     * o no. Cada elemento de la lista es un elemento. DEBE OBlIGATORIAMENTE
     * USARSE EL ALGORITMO QUE ESTÁ EN EL ENUNCIADO.
     */
    static boolean estaBalanceada(List<String> expresion) {
        Stack<String> delimitadores = new Stack<>();

       for (String elemento : expresion) {
            if (elemento.equals("(") || elemento.equals("[") || elemento.equals("{")) {
                delimitadores.push(elemento);
            } else if (elemento.equals(")") || elemento.equals("]") || elemento.equals("}")) {
                if (delimitadores.isEmpty()) {
                    return false;
                }

                String delimitadorAbierto = delimitadores.pop();
                if ((elemento.equals(")") && !delimitadorAbierto.equals("(")) ||
                    (elemento.equals("]") && !delimitadorAbierto.equals("[")) ||
                    (elemento.equals("}") && !delimitadorAbierto.equals("{"))) {
                    return false;
                }
            }
        }

        return delimitadores.isEmpty();
    }

    /**
     * Transforma la expresión, cambiando los símbolos de agrupación
     * de corchetes ([]) y llaves ({}) por paréntesis ()
     */
    static void reemplazarDelimitadores(List<String> expresion) {
        for (int i = 0; i < expresion.size(); i++) {
            String elemento = expresion.get(i);
            if (elemento.equals("[") || elemento.equals("]")) {
                expresion.set(i, "(");
            } else if (elemento.equals("{") || elemento.equals("}")) {
                expresion.set(i, "(");
            }
        }
    }

    /**
     * Realiza la conversión de la notación infija a postfija
     * @return la expresión convertida a postfija
     * OJO: Debe usarse el algoritmo que está en el enunciado OBLIGATORIAMENTE
     */
    static List<String> convertirAPostfijo(List<String> expresion) {
        Stack<String> pila = new Stack<>();
        List<String> salida = new ArrayList<>();
        Map<String, Integer> precedencia = new HashMap<>();
        precedencia.put("+", 1);
        precedencia.put("-", 1);
        precedencia.put("*", 2);
        precedencia.put("/", 2);

        for (String elemento : expresion) {
            if (elemento.matches("[0-9]+")) {
                salida.add(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.add(pila.pop());
                }
                pila.pop(); // Remove "("
            } else {
                while (!pila.isEmpty() && !pila.peek().equals("(") &&
                       precedencia.containsKey(pila.peek()) &&
                       precedencia.get(pila.peek()) >= precedencia.get(elemento)) {
                    salida.add(pila.pop());
                }
                pila.push(elemento);
            }
        }

        while (!pila.isEmpty()) {
            salida.add(pila.pop());
        }

        return salida;
    }

    /**
     * Realiza la evaluación de la expresión postfijo utilizando una pila
     * @param expresion una lista de elementos con números u operadores
     * @return el resultado de la evaluación de la expresión.
     */
    static int evaluarPostFija(List<String> expresion) {
        Stack<Integer> pila = new Stack<>();
        for (String elemento : expresion) {
            if (elemento.matches("[0-9]+")) {
                pila.push(Integer.parseInt(elemento));
            } else {
                int operand2 = pila.pop();
                int operand1 = pila.pop();
                int result;
                switch (elemento) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        result = operand1 / operand2;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + elemento);
                }
                pila.push(result);
            }
        }

        return pila.pop();
    }

}

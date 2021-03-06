import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class main {
    static Stack<String> groups = new Stack<>();
    static Map<String, Integer> elementCounter = new HashMap<>();
    static Map<String, Double> massMap = new HashMap<>();
    static double totalMass = 0.00000;

    public static void main(String[] args) throws FileNotFoundException {
        File fileName = new File("src/MassData.txt");
        Scanner massData = new Scanner(fileName);
        while (massData.hasNext()) {
            String symbol = massData.next();
            Double mass = Double.valueOf(massData.next());
            massMap.put(symbol, mass);
        }
        Scanner input = new Scanner(System.in);
        System.out.print("Enter molecule: ");
        String molecule = input.next();
        calculateMass(molecule);
        System.out.println("Molecular weight: "+ totalMass + " g/mol");
    }

    static void calculateMass(String molecule) {
        String prevElement = "";
        for (int i = 0; i < molecule.length(); i++) {
            String c = String.valueOf(molecule.charAt(i));
            char nextChar = '$';
            if (i < molecule.length() - 1) {
                nextChar = molecule.charAt(i + 1);
            }
            if (c.equals("(")) {
                updateMass();
                elementCounter.clear();
                groups.push(c);
            } else if (c.equals(")")) {
                groups.pop();
            } else if (c.chars().allMatch(Character::isDigit)) {
                if (String.valueOf(nextChar).chars().allMatch(Character::isDigit)) {
                    c = c + nextChar;
                    i += 1;
                }
                int multiplier = Integer.valueOf(c);
                String prevChar = String.valueOf(molecule.charAt(i - 1));
                if (prevChar.equals(")")) {
                    for (String element : elementCounter.keySet()) {
                        elementCounter.put(element, elementCounter.get(element) * multiplier);
                    }
                } else {
                    elementCounter.put(prevElement, elementCounter.get(prevElement) * multiplier);
                }
            } else {
                if (Character.isLetter(nextChar) && Character.isLowerCase(nextChar)) {
                    c = c + nextChar;
                    i += 1;
                }
                if (elementCounter.containsKey(c)) {
                    elementCounter.put(c, elementCounter.get(c) + 1);
                } else {
                    elementCounter.put(c, 1);
                }
                prevElement = c;
            }
        }
        updateMass();
    }

    static void updateMass(){
        Double mass = 0.00000;
        for (String element : elementCounter.keySet()) {
            mass += elementCounter.get(element) * massMap.get(element);
        }
        if(!elementCounter.isEmpty()) {
            System.out.println(elementCounter);
        }
        totalMass += mass;
    }
}


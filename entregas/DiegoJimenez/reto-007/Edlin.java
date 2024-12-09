

import java.util.Scanner;

class Edlin {
    public static void main(String[] args) {
        int[] currentLine = { 1 }; 
        String[] content = {
                "Bienvenidos al editor de texto Edlin",
                "Use el menú para realizar acciones",
                "------",
                "[L] Define la línea activa",
                "[E] Edita la línea activa",
                "[I] Intercambia dos líneas",
                "[B] Borra el contenido de la línea activa",
                "[D] Deshace la última acción",
                "[R] Rehace la última acción deshecha",
                "[C] Copia el contenido de la línea activa",
                "[P] Pega el contenido copiado",
                "[G] Guarda el archivo",
                "[A] Abre un archivo",
                "[S] Sale del programa",
                "",
                ""
        };

        String[] previousContent = { "" };
        String[] redoContent = { "" };
        String[] clipboard = { "" };

        FileManager fileManager = new FileManager();

        do {
            displayContent(content, currentLine);
        } while (executeCommand(content, currentLine, previousContent, redoContent, clipboard, fileManager));
    }

    static void displayContent(String[] content, int[] currentLine) {
        clearScreen();
        drawLine();
        for (int i = 0; i < content.length; i++) {
            System.out.println(i + showPointer(i, currentLine[0]) + content[i]);
        }
        drawLine();
    }

    static String showPointer(int line, int current) {
        return line == current ? ":*| " : ": | ";
    }

    static void drawLine() {
        System.out.println("-".repeat(50));
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static boolean executeCommand(String[] content, int[] currentLine, String[] previousContent, String[] redoContent,
            String[] clipboard, FileManager fileManager) {
        System.out.println(
                "Comandos: [L]ínea activa | [E]ditar | [I]ntercambiar | [D]eshacer | [R]ehacer | [C]opiar | [P]egar | [B]orrar | [G]uardar | [A]brir | [S]alir");

        switch (getCommand()) {
            case 'S':
            case 's':
                return false;
            case 'L':
            case 'l':
                updateActiveLine(content, currentLine);
                break;
            case 'E':
            case 'e':
                modifyLine(content, currentLine, previousContent, redoContent);
                break;
            case 'I':
            case 'i':
                swapLines(content);
                break;
            case 'B':
            case 'b':
                removeLine(content, currentLine, previousContent, redoContent);
                break;
            case 'D':
            case 'd':
                undoChange(content, currentLine, previousContent, redoContent);
                break;
            case 'R':
            case 'r':
                redoChange(content, currentLine, previousContent, redoContent);
                break;
            case 'C':
            case 'c':
                copyToClipboard(content, currentLine, clipboard);
                break;
            case 'P':
            case 'p':
                pasteFromClipboard(content, currentLine, clipboard, previousContent, redoContent);
                break;
            case 'G':
            case 'g':
                fileManager.saveToFile(content);
                break;
            case 'A':
            case 'a':
                fileManager.loadFromFile(content);
                break;
        }
        return true;
    }

    static char getCommand() {
        Scanner input = new Scanner(System.in);
        return input.next().charAt(0);
    }

    static void updateActiveLine(String[] content, int[] currentLine) {
        do {
            System.out.print("Nueva línea activa: ");
            currentLine[0] = getInteger();
        } while (!isValidLine(currentLine[0], content.length));
    }

    static void modifyLine(String[] content, int[] currentLine, String[] previousContent, String[] redoContent) {
        System.out.println("Editando > " + content[currentLine[0]]);
        redoContent[0] = "";
        previousContent[0] = content[currentLine[0]];
        content[currentLine[0]] = getString();
    }

    static void removeLine(String[] content, int[] currentLine, String[] previousContent, String[] redoContent) {
        System.out.println("Borrando contenido de la línea " + currentLine[0]);
        redoContent[0] = "";
        previousContent[0] = content[currentLine[0]];
        content[currentLine[0]] = "";
    }

    static void swapLines(String[] content) {
        int firstLine, secondLine;
        do {
            System.out.print("Primera línea: ");
            firstLine = getInteger();
        } while (!isValidLine(firstLine, content.length));

        do {
            System.out.print("Segunda línea: ");
            secondLine = getInteger();
        } while (!isValidLine(secondLine, content.length));

        String temp = content[firstLine];
        content[firstLine] = content[secondLine];
        content[secondLine] = temp;
    }

    static void undoChange(String[] content, int[] currentLine, String[] previousContent, String[] redoContent) {
        if (previousContent[0].isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }
        System.out.println("Deshaciendo en línea " + currentLine[0]);
        redoContent[0] = content[currentLine[0]];
        content[currentLine[0]] = previousContent[0];
        previousContent[0] = "";
    }

    static void redoChange(String[] content, int[] currentLine, String[] previousContent, String[] redoContent) {
        if (redoContent[0].isEmpty()) {
            System.out.println("No hay acciones para rehacer.");
            return;
        }
        System.out.println("Rehaciendo en línea " + currentLine[0]);
        previousContent[0] = content[currentLine[0]];
        content[currentLine[0]] = redoContent[0];
        redoContent[0] = "";
    }

    static void copyToClipboard(String[] content, int[] currentLine, String[] clipboard) {
        clipboard[0] = content[currentLine[0]];
        System.out.println("Contenido copiado: " + clipboard[0]);
    }

    static void pasteFromClipboard(String[] content, int[] currentLine, String[] clipboard, String[] previousContent,
            String[] redoContent) {
        if (clipboard[0].isEmpty()) {
            System.out.println("Portapapeles vacío.");
            return;
        }
        redoContent[0] = "";
        previousContent[0] = content[currentLine[0]];
        content[currentLine[0]] = clipboard[0];
        System.out.println("Contenido pegado: " + clipboard[0]);
    }

    static boolean isValidLine(int line, int limit) {
        return line >= 0 && line < limit;
    }

    static String getString() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    static int getInteger() {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }
}
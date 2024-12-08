import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FileManager {
    private static final String FILE_NAME = "archivo_edlin.txt";

    public void saveToFile(String[] content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : content) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Archivo guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    public void loadFromFile(String[] content) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < content.length) {
                content[index++] = line;
            }
            System.out.println("Archivo cargado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
}

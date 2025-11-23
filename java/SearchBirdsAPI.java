import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBirdsAPI {

    public static void main(String[] args) {
        try {
            // 1. CONFIGURACIÓN: IP de tu servidor (¡Asegúrate de que es correcta!)
            String ip = "127.0.0.1"; 
            String port = "9191";
            
            // Puedes cambiar "?type=birds" por "?type=sightings" si quieres ver avistamientos
            String url = "http://" + ip + ":" + port + "/api.php?type=birds";

            System.out.println("Conectando a: " + url);

            // 2. PETICIÓN HTTP (Estándar de Java)
            String jsonResponse = sendGETPetition(url);

            // 3. PROCESAR DATOS (Sin librerías externas)
            if (jsonResponse.startsWith("[")) {
                printBirdList(jsonResponse);
            } else {
                System.out.println("Respuesta no reconocida o error: " + jsonResponse);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // --- MÉTODO HTTP ESTÁNDAR ---
    private static String sendGETPetition(String urlString) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    // --- PARSER MANUAL (El truco para no usar .jar) ---
    private static void printBirdList(String jsonArray) {
        // Limpiamos los corchetes del array [ ... ]
        String content = jsonArray.trim();
        if (content.startsWith("[")) content = content.substring(1);
        if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

        // Separamos por objetos. El truco es partir el string cada vez que vemos "},{"
        // (Esto es básico, fallaría si el texto contiene esa cadena, pero para este proyecto sirve)
        String[] objects = content.split("\\},\\{");

        System.out.println("\n LISTA DE AVES");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-20s | %-25s | %-15s |\n", "ID", "NOMBRE COMUN", "NOMBRE CIENTIFICO", "FOTO");
        System.out.println("-------------------------------------------------------------------------");

        for (String obj : objects) {
            // Limpiamos llaves extra si quedaron
            obj = obj.replace("{", "").replace("}", "");

            // Extraemos los valores usando un método auxiliar
            String id = extractValue(obj, "bird_id");
            String name = extractValue(obj, "common_name");
            String sci = extractValue(obj, "scientific_name");
            String img = extractValue(obj, "img_url");
            
            // Convertimos Unicode (ej: \u00e1) a caracteres reales si hace falta
            name = decodeUnicode(name);

            // Imprimimos bonito
            String hasPhoto = (img != null && !img.isEmpty()) ? "SI" : "NO";
            
            System.out.printf("| %-4s | %-20s | %-25s | %-15s |\n", 
                id, 
                truncate(name, 20), 
                truncate(sci, 25), 
                hasPhoto
            );
        }
        System.out.println("-------------------------------------------------------------------------\n");
    }

    // Busca un valor dentro del string estilo: "clave":"valor"
    private static String extractValue(String json, String key) {
        // Regex: Busca "key":"(lo que sea)" o "key":(numero)
        Pattern p = Pattern.compile("\"" + key + "\":\"?(.*?)\"?(,|$)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1).replace("\"", ""); // Limpiar comillas extra
        }
        return "N/A";
    }

    // Utilidad para cortar textos largos
    private static String truncate(String str, int width) {
        if (str.length() > width) {
            return str.substring(0, width - 3) + "...";
        }
        return str;
    }
    
    // Utilidad simple para acentos básicos en JSON
    private static String decodeUnicode(String val) {
        return val.replace("\\u00e1", "á").replace("\\u00e9", "é")
                  .replace("\\u00ed", "í").replace("\\u00f3", "ó")
                  .replace("\\u00fa", "ú").replace("\\u00f1", "ñ")
                  .replace("\\u00c1", "Á").replace("\\u00c9", "É")
                  .replace("\\u00cd", "Í").replace("\\u00d3", "Ó")
                  .replace("\\u00da", "Ú").replace("\\u00d1", "Ñ");
    }
}
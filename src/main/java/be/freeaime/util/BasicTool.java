/**
 * @Author: Aimé
 * @Date:   2022-04-08 20:20:47
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 00:48:09
 */

package be.freeaime.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.Normalizer;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicTool {
    private ObjectMapper objectMapper;

    private BasicTool() {
        objectMapper = new ObjectMapper();
    }

    private static class Holder {
        private static final BasicTool INSTANCE = new BasicTool();
    }

    public static BasicTool getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 
     * @param object
     * @return json string representation or null if it fail
     * 
     */
    public static String getJsonString(Object object) {
        try {
            return getInstance().objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode readJson(String jsonString) {
        try {
            return getInstance().objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static String defaultPersistedFolder = "drop-blocks-2048";

    public static String generateLocation(String fileName) {
        Path defaultPersistedFolderPath = Paths.get(System.getProperty("user.dir"), defaultPersistedFolder, fileName);
        File file = new File(defaultPersistedFolderPath.toString());
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return defaultPersistedFolderPath.toString();
    }

    public static long generateID() {
        final long MIN_VALUE = 1000000000000000000l;
        long random = (long) (Math.random() * (Long.MAX_VALUE - MIN_VALUE) + MIN_VALUE);
        return random;
    }

    /**
     *
     * @return String of 11 random you tube like characters using base 36 without
     *         capital letters
     */
    public static String generateYouTubeStyleNameID() {
        // <editor-fold defaultstate="collapsed" desc="base36-11characters">
        // windows doesn't care for upper and lower case
        // also i dont want to add unicode symbols
        // so that narrows the names down
        // stored in db sqlite uses utf-8 which uses 1-4 bytes per character in ascii
        // if you go more than ascii it adds more bytes
        // i want to get 11 character id so every id = 4 * 11 = 44 bytes
        // possible ids = 36 pow(11)= 1,3162×10¹⁷
        // </editor-fold>
        return generateYouTubeStyleNameID(11);

    }

    public static String generateYouTubeStyleNameID(int length) {

        String base = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder finalString2 = new StringBuilder();
        int min = 0, max = 36;
        for (int i = 0; i < length; i++) {
            int random = (int) ((Math.random() * (max - min)) + min);
            finalString2.append(base.charAt(random));
        }
        return finalString2.toString();
    }


    public static void saveToTextFile(String dataString, String pathToFile) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToFile))) {
            bufferedWriter.write(dataString);
        }
    }

    public static <T extends Serializable> void saveObjectToFile(T serializable, String pathToFile)
            throws IOException, FileNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
            objectOutputStream.writeObject(serializable);
        }
    }

    public static Object openObjectFile(String pathToFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(pathToFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateRSAPair(String foldername) throws NoSuchAlgorithmException, IOException {
        Path path = Paths.get(System.getProperty("user.home"), foldername);
        Files.createDirectories(path);
        String publicKeyPathString = Paths.get(path.toString(), String.format("%s.public.key", foldername)).toString();
        String privateKeyPathString = Paths.get(path.toString(), String.format("%s.private.key", foldername))
                .toString();

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        writeToFile(publicKeyPathString, publicKey.getEncoded());
        writeToFile(privateKeyPathString, privateKey.getEncoded());

    }

    public static byte[] readFile(String pathToKeyFile) throws IOException {
        return Files.readAllBytes(Paths.get(pathToKeyFile));
    }

    public static void writeToFile(String pathToFile, byte[] key)
            throws IOException, FileNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathToFile)) {
            fileOutputStream.write(key);
        }
    }

 
    /**
     * Removes all the potentially malicious characters from a string
     * 
     * @param value the raw string
     * @return the sanitized string
     */
    // source: https://gist.github.com/ftroncosom/e593d2d2931ee5c3584cba4234e2522b
    public static String stripXSS(String value) {
        String cleanValue = null;
        if (value != null) {
            cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);

            // Avoid null characters
            cleanValue = cleanValue.replaceAll("\0", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
        }
        return cleanValue;
    }
}
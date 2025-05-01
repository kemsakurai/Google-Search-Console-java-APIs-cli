package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * ResponseWriter
 */
public class ResponseWriter {

    /**
     * Write response to JSON
     *
     * @param response response object
     * @param format   output format
     * @param path     file path for JSON output
     * @throws CmdLineArgmentException if command line arguments are invalid
     * @throws CmdLineIOException if IO error occurs
     */
    public static void writeJson(Object response, Format format, String path) throws CmdLineArgmentException, CmdLineIOException {
        if (format == null) {
            throw new CmdLineArgmentException("Format must be specified");
        }

        String jsonString;
        try {
            if (response == null) {
                jsonString = "{}";
            } else if (response instanceof GenericJson) {
                jsonString = ((GenericJson) response).toPrettyString();
            } else {
                jsonString = response.toString();
            }
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to convert response to JSON", e);
        }

        if (format == Format.JSON) {
            if (path == null || path.trim().isEmpty()) {
                throw new CmdLineArgmentException("For JSON format, filepath is mandatory.");
            }
            writeToFile(path, jsonString);
        } else if (format == Format.CONSOLE) {
            System.out.println(jsonString);
        } else {
            throw new CmdLineArgmentException("Unsupported format: " + format);
        }
    }

    private static void writeToFile(String path, String content) throws CmdLineIOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        
        try {
            if (parentDir != null && !parentDir.exists()) {
                FileUtils.forceMkdir(parentDir);
            }
            FileUtils.write(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }
}

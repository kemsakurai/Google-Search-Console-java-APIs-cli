package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.out;

/**
 * ResponseWriter
 */
public class ResponseWriter {

    /**
     * Write Json according to Option
     *
     * @param response
     * @param format
     * @param filePath
     */
    public static void writeJson(GenericJson response, Format format, String filePath) {
        // Output
        try {
            switch (format) {
                // for format console
                case CONSOLE:
                    out.println(response.toPrettyString());
                    break;
                // for format json
                case JSON:
                    if (StringUtils.isEmpty(filePath)) {
                        throw new CmdLineArgmentException("For JSON format, filepath is mandatory.");
                    }
                    String json = response.toPrettyString();
                    Path path = Paths.get(filePath);
                    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                        writer.write(json);
                        writer.flush();
                    }
                    break;
                default:
                    throw new AssertionError("Illegal argument format = [" + format.name() + "]");
            }
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }
}

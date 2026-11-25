package es.uah.clientePeliculas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Guarda y borra las portadas en la carpeta uploads del cliente. En la base de datos solo se guarda
 * el nombre del fichero.
 */
@Service
public class PortadaService {

    private static final Logger log = LoggerFactory.getLogger(PortadaService.class);

    private static final Map<String, String> EXTENSIONES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif");

    // Solo se borran ficheros con el nombre que genera este servicio: el nombre llega en un campo
    // oculto del formulario y no debe poder apuntar a otro fichero del disco.
    private static final Pattern NOMBRE_GENERADO = Pattern.compile("[0-9a-f-]{36}\\.(jpg|png|webp|gif)");

    private final Path carpeta;

    public PortadaService(@Value("${app.portadas.carpeta:uploads}") String carpeta) throws IOException {
        this.carpeta = Path.of(carpeta).toAbsolutePath().normalize();
        Files.createDirectories(this.carpeta);
    }

    /**
     * Guarda la imagen con un nombre único.
     *
     * @return el nombre del fichero, o null si no se eligió ninguna imagen
     * @throws PortadaNoValidaException si el fichero no es una imagen admitida
     */
    public String guardar(MultipartFile fichero) {
        if (fichero == null || fichero.isEmpty()) {
            return null;
        }
        String extension = EXTENSIONES.get(fichero.getContentType());
        if (extension == null) {
            throw new PortadaNoValidaException("La portada debe ser una imagen JPG, PNG, WebP o GIF.");
        }
        String nombre = UUID.randomUUID() + "." + extension;
        try (InputStream entrada = fichero.getInputStream()) {
            Files.copy(entrada, carpeta.resolve(nombre));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la portada", e);
        }
        return nombre;
    }

    /** Borra una portada; si el nombre no es uno generado aquí, no hace nada. */
    public void eliminar(String nombre) {
        if (nombre == null || !NOMBRE_GENERADO.matcher(nombre).matches()) {
            return;
        }
        try {
            Files.deleteIfExists(carpeta.resolve(nombre));
        } catch (IOException e) {
            // No impide la operación principal: como mucho queda un fichero huérfano.
            log.warn("No se pudo borrar la portada {}", nombre, e);
        }
    }

    public Path getCarpeta() {
        return carpeta;
    }

    /** La imagen elegida no es de un tipo admitido. */
    public static class PortadaNoValidaException extends RuntimeException {
        public PortadaNoValidaException(String mensaje) {
            super(mensaje);
        }
    }
}

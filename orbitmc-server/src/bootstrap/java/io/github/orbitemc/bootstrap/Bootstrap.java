package io.github.orbitemc.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap {
    public static void main(String[] argv) {
        new Bootstrap().run(argv);
    }

    private void run(String[] argv) {
        try {
            String defaultMainClassName = this.readResource("main-class", BufferedReader::readLine);
            String mainClassName = System.getProperty("bundlerMainClass", defaultMainClassName);
            String repoDir = System.getProperty("bundlerRepoDir", "");
            Path outputDir = Paths.get(repoDir, new String[0]);
            Files.createDirectories(outputDir, new FileAttribute[0]);
            ArrayList<URL> extractedUrls = new ArrayList<URL>();
            this.readAndExtractDir("versions", outputDir, extractedUrls);
            this.readAndExtractDir("libraries", outputDir, extractedUrls);
            if (mainClassName == null || mainClassName.isEmpty()) {
                System.out.println("Empty main class specified, exiting");
                System.exit(0);
            }
            ClassLoader maybePlatformClassLoader = this.getClass().getClassLoader().getParent();
            URLClassLoader classLoader = new URLClassLoader(extractedUrls.toArray(new URL[0]), maybePlatformClassLoader);
            System.out.println("Starting " + mainClassName);
            Thread runThread = new Thread(() -> {
                try {
                    Class<?> mainClass = Class.forName(mainClassName, true, classLoader);
                    MethodHandle mainHandle = MethodHandles.lookup().findStatic(mainClass, "main", MethodType.methodType(Void.TYPE, String[].class)).asFixedArity();
                    mainHandle.invoke(argv);
                } catch (Throwable t) {
                    Thrower.INSTANCE.sneakyThrow(t);
                }
            }, "ServerMain");
            runThread.setContextClassLoader(classLoader);
            runThread.start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println("Failed to extract server libraries, exiting");
        }
    }

    private <T> T readResource(String resource, ResourceParser<T> parser) throws Exception {
        String fullPath = "/META-INF/" + resource;
        try (InputStream is = this.getClass().getResourceAsStream(fullPath);) {
            if (is == null) {
                throw new IllegalStateException("Resource " + fullPath + " not found");
            }
            T t = parser.parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
            return t;
        }
    }

    private void readAndExtractDir(String subdir, Path outputDir, List<URL> extractedUrls) throws Exception {
        List<FileEntry> entries = this.readResource(subdir + ".list", reader -> reader.lines().map(FileEntry::parseLine).toList());
        Path subdirPath = outputDir.resolve(subdir);
        for (FileEntry entry : entries) {
            Path outputFile = subdirPath.resolve(entry.path);
            this.checkAndExtractJar(subdir, entry, outputFile);
            extractedUrls.add(outputFile.toUri().toURL());
        }
    }

    private void checkAndExtractJar(String subdir, FileEntry entry, Path outputFile) throws Exception {
        if (!Files.exists(outputFile, new LinkOption[0]) || !Bootstrap.checkIntegrity(outputFile, entry.hash())) {
            System.out.printf("Unpacking %s (%s:%s) to %s%n", entry.path, subdir, entry.id, outputFile);
            this.extractJar(subdir, entry.path, outputFile);
        }
    }

    private void extractJar(String subdir, String jarPath, Path outputFile) throws IOException {
        Files.createDirectories(outputFile.getParent(), new FileAttribute[0]);
        try (InputStream input = this.getClass().getResourceAsStream("/META-INF/" + subdir + "/" + jarPath);) {
            if (input == null) {
                throw new IllegalStateException("Declared library " + jarPath + " not found");
            }
            Files.copy(input, outputFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean checkIntegrity(Path file, String expectedHash) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream output = Files.newInputStream(file, new OpenOption[0]);) {
            output.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), digest));
            String actualHash = Bootstrap.byteToHex(digest.digest());
            if (actualHash.equalsIgnoreCase(expectedHash)) {
                boolean bl = true;
                return bl;
            }
            System.out.printf("Expected file %s to have hash %s, but got %s%n", file, expectedHash, actualHash);
        }
        return false;
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(Character.forDigit(b >> 4 & 0xF, 16));
            result.append(Character.forDigit(b >> 0 & 0xF, 16));
        }
        return result.toString();
    }

    @FunctionalInterface
    private static interface ResourceParser<T> {
        public T parse(BufferedReader var1) throws Exception;
    }

    private record FileEntry(String hash, String id, String path) {
        public static FileEntry parseLine(String line) {
            String[] fields = line.split(" ");
            if (fields.length != 3) {
                throw new IllegalStateException("Malformed library entry: " + line);
            }
            return new FileEntry(fields[0], fields[2], fields[2]);
        }
    }

    private static class Thrower<T extends Throwable> {
        private static final Thrower<RuntimeException> INSTANCE = new Thrower();

        private Thrower() {
        }

        public void sneakyThrow(Throwable exception) throws T {
            throw (T) exception;
        }
    }
}

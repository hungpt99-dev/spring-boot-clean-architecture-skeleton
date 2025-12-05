package com.yourorg.yourapp.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight CLI to scaffold common building blocks (similar to NestJS CLI).
 *
 * Examples:
 *   ./gradlew :tooling:runCli --args="generate usecase Order --package=com.acme.shop --author=you --tag=cqrs"
 *   ./gradlew :tooling:runCli --args="generate controller Order --package=com.acme.shop --force"
 *   ./gradlew :tooling:runCli --args="generate dto Order --package=com.acme.shop --lang=en"
 *   ./gradlew :tooling:runCli --args="generate test Order --package=com.acme.shop"
 *   ./gradlew :tooling:runCli --args="generate migration add_orders"
 *   ./gradlew :tooling:runCli --args="rename project cool-app --app-name=coolapp --config-prefix=coolapp --env-prefix=COOLAPP --group=com.acme"
 *   ./gradlew :tooling:runCli --args="list"
 *   ./gradlew :tooling:runCli --args="destroy <path>"
 *
 * You can also set APP_BASE_PACKAGE env var instead of --package flag or use .codegen/config.properties.
 */
public final class CodegenCli {

    private static final String DEFAULT_APP_NAME = "app";
    private static final String DEFAULT_CONFIG_PREFIX = "app";
    private static final String DEFAULT_ENV_PREFIX = "APP";
    private static final String DEFAULT_GROUP = "com.example";
    private static final Path MANIFEST = Paths.get(".codegen/manifest.txt");
    private static final Path DEFAULT_CONFIG_PATH = Paths.get(".codegen/config.properties");

    public static void main(String[] args) throws Exception {
        new CodegenCli().run(args);
    }

    private void run(String[] args) throws IOException {
        ParsedArgs parsed = parse(args);
        if (parsed.positional.isEmpty()) {
            printHelp();
            return;
        }
        Map<String, String> config = loadConfig(parsed.options);
        String command = parsed.positional.get(0);
        String target = parsed.positional.size() >= 2 ? parsed.positional.get(1) : null;
        String name = parsed.positional.size() >= 3 ? parsed.positional.get(2) : null;
        String basePackage = resolveBasePackage(parsed.options, config);

        switch (command) {
            case "generate" -> generate(target, name, basePackage, parsed.options, config);
            case "rename" -> rename(target, name, parsed.options, config);
            case "list" -> listGenerated();
            case "destroy" -> destroy(target);
            default -> {
                System.out.println("Unknown command: " + command);
                printHelp();
            }
        }
    }

    private ParsedArgs parse(String[] args) {
        List<String> positional = new ArrayList<>();
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                String trimmed = arg.substring(2);
                if (trimmed.contains("=")) {
                    String[] kv = trimmed.split("=", 2);
                    options.put(kv[0], kv.length > 1 ? kv[1] : "true");
                } else if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    options.put(trimmed, args[i + 1]);
                    i++;
                } else {
                    options.put(trimmed, "true");
                }
            } else {
                positional.add(arg);
            }
        }
        return new ParsedArgs(positional, options);
    }

    private String resolveBasePackage(Map<String, String> opts, Map<String, String> config) {
        String fromFlag = opts.get("package");
        if (fromFlag != null && !fromFlag.isBlank()) {
            return fromFlag;
        }
        String fromEnv = System.getenv("APP_BASE_PACKAGE");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        String fromConfig = config.get("basePackage");
        if (fromConfig != null && !fromConfig.isBlank()) {
            return fromConfig;
        }
        System.out.println("Base package is required. Provide --package, APP_BASE_PACKAGE env, or set basePackage in .codegen/config.properties.");
        return null;
    }

    private void generate(String target, String name, String basePackage, Map<String, String> opts, Map<String, String> config) throws IOException {
        if (target == null) {
            System.out.println("Target is required for generate.");
            printHelp();
            return;
        }
        if (name == null || name.isBlank()) {
            System.out.println("Name is required for generate.");
            printHelp();
            return;
        }
        if (basePackage == null || basePackage.isBlank()) {
            return;
        }
        boolean dryRun = Boolean.parseBoolean(opts.getOrDefault("dry-run", "false"));
        boolean force = Boolean.parseBoolean(opts.getOrDefault("force", "false"));
        String author = opts.getOrDefault("author", "");
        String tag = opts.getOrDefault("tag", "");
        String lang = opts.getOrDefault("lang", "en");
        switch (target) {
            case "usecase" -> scaffoldUseCase(name, basePackage, force, dryRun, author, tag);
            case "controller" -> scaffoldController(name, basePackage, force, dryRun, author, tag);
            case "dto" -> scaffoldDto(name, basePackage, force, dryRun, author, tag);
            case "adapter" -> scaffoldAdapter(name, basePackage, force, dryRun, author, tag);
            case "port" -> scaffoldPort(name, basePackage, force, dryRun, author, tag);
            case "test" -> scaffoldTest(name, basePackage, force, dryRun, author, tag);
            case "migration" -> scaffoldMigration(name, force, dryRun);
            case "config" -> scaffoldConfig(name, basePackage, force, dryRun, author, tag);
            case "message" -> scaffoldMessage(lang, force, dryRun);
            default -> {
                System.out.println("Unknown generate target: " + target);
                printHelp();
            }
        }
    }

    private void scaffoldUseCase(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".usecase.interactor";
        Path path = Paths.get("usecase/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("usecase/interactor/" + name + "Interactor.java");

        String content = """
                package %s;

                import %s.usecase.annotation.UseCaseComponent;
                import %s.usecase.annotation.UseCaseTransactional;

                @UseCaseComponent
                @UseCaseTransactional
                public class %sInteractor {

                    public void execute() {
                        // TODO: implement business logic
                    }
                }
                """.formatted(pkg, basePackage, basePackage, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldController(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".adapter.web";
        Path path = Paths.get("adapter/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("adapter/web/" + name + "Controller.java");

        String route = "/api/v1/" + toKebab(rawName);
        String content = """
                package %s;

                import %s.adapter.annotation.ApiController;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.RequestMapping;

                @ApiController
                @RequestMapping("%s")
                public class %sController {

                    @GetMapping
                    public String sample() {
                        return "ok";
                    }
                }
                """.formatted(pkg, basePackage, route, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldDto(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".adapter.web.dto";
        Path path = Paths.get("adapter/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("adapter/web/dto/" + name + "Request.java");

        String content = """
                package %s;

                public record %sRequest(
                        String sampleField
                ) {}
                """.formatted(pkg, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldAdapter(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".adapter.persistence";
        Path path = Paths.get("adapter/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("adapter/persistence/" + name + "Adapter.java");

        String content = """
                package %s;

                import %s.adapter.annotation.AdapterComponent;

                @AdapterComponent
                public class %sAdapter {

                    // TODO: implement persistence logic
                }
                """.formatted(pkg, basePackage, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldPort(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".usecase.port";
        Path path = Paths.get("usecase/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("usecase/port/" + name + "Port.java");

        String content = """
                package %s;

                public interface %sPort {
                    void execute();
                }
                """.formatted(pkg, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldTest(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".usecase.interactor";
        Path path = Paths.get("usecase/src/test/java")
            .resolve(toPath(basePackage))
            .resolve("usecase/interactor/" + name + "InteractorTest.java");

        String content = """
                package %s;

                import org.junit.jupiter.api.Test;

                class %sInteractorTest {

                    @Test
                    void sample() {
                        // TODO: implement test
                    }
                }
                """.formatted(pkg, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldMigration(String rawName, boolean force, boolean dryRun) throws IOException {
        String ts = String.valueOf(System.currentTimeMillis());
        String kebab = toKebab(rawName);
        Path path = Paths.get("application/src/main/resources/db/migration/V" + ts + "__" + kebab + ".sql");
        String content = "-- migration: " + kebab + "\n\n-- TODO: add SQL here\n";
        writeFile(path, content, force, dryRun, "", "");
    }

    private void scaffoldConfig(String rawName, String basePackage, boolean force, boolean dryRun, String author, String tag) throws IOException {
        String name = capitalize(rawName);
        String pkg = basePackage + ".config";
        Path path = Paths.get("application/src/main/java")
            .resolve(toPath(basePackage))
            .resolve("config/" + name + "Config.java");

        String content = """
                package %s;

                import org.springframework.context.annotation.Configuration;

                @Configuration
                public class %sConfig {
                }
                """.formatted(pkg, name);
        writeFile(path, content, force, dryRun, author, tag);
    }

    private void scaffoldMessage(String lang, boolean force, boolean dryRun) throws IOException {
        String suffix = lang == null || lang.isBlank() ? "en" : lang;
        Path path = Paths.get("application/src/main/resources/messages_" + suffix + ".properties");
        String content = "# messages for " + suffix + "\nsample.key=Hello\n";
        writeFile(path, content, force, dryRun, "", "");
    }

    private void rename(String target, String name, Map<String, String> opts, Map<String, String> config) throws IOException {
        if (!"project".equals(target)) {
            System.out.println("Unknown rename target: " + target);
            printHelp();
            return;
        }
        if (name == null || name.isBlank()) {
            System.out.println("Name is required for rename.");
            printHelp();
            return;
        }
        String newProjectName = name;
        String newAppName = firstNonBlank(
            opts.get("app-name"),
            config.get("appName"),
            sanitizeAppName(name),
            DEFAULT_APP_NAME
        );
        String newConfigPrefix = firstNonBlank(
            opts.get("config-prefix"),
            config.get("configPrefix"),
            toKebab(name),
            DEFAULT_CONFIG_PREFIX
        );
        String newEnvPrefix = firstNonBlank(
            opts.get("env-prefix"),
            config.get("envPrefix"),
            toEnvPrefix(newConfigPrefix),
            DEFAULT_ENV_PREFIX
        );
        String newGroup = firstNonBlank(
            opts.get("group"),
            config.get("group"),
            DEFAULT_GROUP
        );
        renameProjectFiles(newProjectName, newAppName, newConfigPrefix, newEnvPrefix, newGroup);
        System.out.printf("Renamed project to '%s', app-name='%s', config-prefix='%s', env-prefix='%s', group='%s'%n",
                newProjectName, newAppName, newConfigPrefix, newEnvPrefix, newGroup);
    }

    private void renameProjectFiles(String projectName, String appName, String configPrefix, String envPrefix, String group) throws IOException {
        updateSettingsProjectName(projectName);
        updateRootGroup(group);
        updateApplicationYaml(appName, configPrefix, envPrefix);
    }

    private void updateSettingsProjectName(String newName) throws IOException {
        Path settings = Paths.get("settings.gradle.kts");
        if (!Files.exists(settings)) {
            System.out.println("Skip: settings.gradle.kts not found");
            return;
        }
        String content = Files.readString(settings, StandardCharsets.UTF_8);
        String updated = content.replaceAll("(?m)^rootProject\\.name\\s*=\\s*\"[^\"]*\"",
                "rootProject.name = \"" + newName + "\"");
        writeString(settings, updated);
    }

    private void updateRootGroup(String newGroup) throws IOException {
        Path rootBuild = Paths.get("build.gradle.kts");
        if (!Files.exists(rootBuild)) {
            System.out.println("Skip: build.gradle.kts not found");
            return;
        }
        String content = Files.readString(rootBuild, StandardCharsets.UTF_8);
        String updated = content.replaceAll("(?m)^group\\s*=\\s*\"[^\"]*\"",
                "group = \"" + newGroup + "\"");
        writeString(rootBuild, updated);
    }

    private void updateApplicationYaml(String appName, String configPrefix, String envPrefix) throws IOException {
        Path yaml = Paths.get("application/src/main/resources/application.yml");
        if (!Files.exists(yaml)) {
            System.out.println("Skip: application.yml not found");
            return;
        }
        String content = Files.readString(yaml, StandardCharsets.UTF_8);
        content = content.replaceFirst("(?m)^\\s*name:\\s*.+$", "    name: " + appName);
        String currentPrefix = extractRootConfigPrefix(content);
        if (currentPrefix == null || currentPrefix.isBlank()) {
            currentPrefix = DEFAULT_CONFIG_PREFIX;
        }
        content = content.replaceFirst("(?m)^" + Pattern.quote(currentPrefix) + ":", configPrefix + ":");
        content = content.replace(DEFAULT_ENV_PREFIX + "_", envPrefix + "_");
        content = content.replaceAll(DEFAULT_ENV_PREFIX, envPrefix);
        writeString(yaml, content);
    }

    private void listGenerated() throws IOException {
        if (!Files.exists(MANIFEST)) {
            System.out.println("No generated files tracked.");
            return;
        }
        List<String> lines = Files.readAllLines(MANIFEST);
        if (lines.isEmpty()) {
            System.out.println("Manifest is empty.");
            return;
        }
        lines.forEach(System.out::println);
    }

    private void destroy(String pathStr) throws IOException {
        if (pathStr == null || pathStr.isBlank()) {
            System.out.println("Path is required for destroy.");
            printHelp();
            return;
        }
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            System.out.println("Not found: " + path);
            return;
        }
        Files.delete(path);
        removeFromManifest(path);
        System.out.println("Deleted: " + path);
    }

    private void writeFile(Path path, String content, boolean force, boolean dryRun, String author, String tag) throws IOException {
        ensureParent(path);
        if (!force && Files.exists(path)) {
            System.out.println("Skip (exists): " + path);
            return;
        }
        String header = buildHeader(author, tag);
        if (dryRun) {
            System.out.println("[dry-run] Would write: " + path);
            System.out.println(header + content);
            return;
        }
        Files.writeString(path, header + content, StandardCharsets.UTF_8);
        addToManifest(path);
        System.out.println("Created: " + path);
    }

    private String buildHeader(String author, String tag) {
        StringBuilder sb = new StringBuilder();
        if ((author != null && !author.isBlank()) || (tag != null && !tag.isBlank())) {
            sb.append("/*");
            if (author != null && !author.isBlank()) {
                sb.append(" author: ").append(author).append(";");
            }
            if (tag != null && !tag.isBlank()) {
                sb.append(" tag: ").append(tag).append(";");
            }
            sb.append(" */\n");
        }
        return sb.toString();
    }

    private void addToManifest(Path path) throws IOException {
        ensureParent(MANIFEST);
        Files.writeString(MANIFEST, path.toString() + System.lineSeparator(), StandardCharsets.UTF_8,
                Files.exists(MANIFEST) ? java.nio.file.StandardOpenOption.APPEND : java.nio.file.StandardOpenOption.CREATE);
    }

    private void removeFromManifest(Path path) throws IOException {
        if (!Files.exists(MANIFEST)) {
            return;
        }
        List<String> lines = Files.readAllLines(MANIFEST);
        List<String> updated = lines.stream().filter(p -> !p.equals(path.toString())).toList();
        Files.write(MANIFEST, updated, StandardCharsets.UTF_8);
    }

    private void ensureParent(Path path) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
    }

    private void writeString(Path path, String content) throws IOException {
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    private String capitalize(String in) {
        if (in == null || in.isBlank()) {
            return "";
        }
        return Character.toUpperCase(in.charAt(0)) + in.substring(1);
    }

    private String toKebab(String in) {
        if (in == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().replace('_', '-');
    }

    private String toEnvPrefix(String in) {
        if (in == null || in.isBlank()) {
            return DEFAULT_ENV_PREFIX;
        }
        return in.replace('-', '_')
            .replace('.', '_')
            .replace('/', '_')
            .toUpperCase();
    }

    private String sanitizeAppName(String in) {
        if (in == null || in.isBlank()) {
            return DEFAULT_APP_NAME;
        }
        return in.replaceAll("[^A-Za-z0-9_-]", "-");
    }

    private Map<String, String> loadConfig(Map<String, String> opts) throws IOException {
        Path path = opts.containsKey("config") ? Paths.get(opts.get("config")) : DEFAULT_CONFIG_PATH;
        Map<String, String> cfg = new HashMap<>();
        if (!Files.exists(path)) {
            return cfg;
        }
        Properties props = new Properties();
        try (var in = Files.newInputStream(path)) {
            props.load(in);
        }
        props.forEach((k, v) -> cfg.put(k.toString(), v.toString()));
        return cfg;
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }

    private String extractRootConfigPrefix(String content) {
        Matcher m = Pattern.compile("(?m)^([A-Za-z0-9_-]+):\\s*$").matcher(content);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private Path toPath(String pkg) {
        return Paths.get(pkg.replace('.', '/'));
    }

    private void printHelp() {
        System.out.println("""
                Usage:
                  ./gradlew :tooling:runCli --args="generate usecase <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate controller <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate dto <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate adapter <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate port <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate test <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate migration <name>"
                  ./gradlew :tooling:runCli --args="generate config <Name> [--package=com.acme.app]"
                  ./gradlew :tooling:runCli --args="generate message [--lang=en]"
                  ./gradlew :tooling:runCli --args="rename project <NewName> [--app-name=foo] [--config-prefix=foo] [--env-prefix=FOO] [--group=com.acme]"
                  ./gradlew :tooling:runCli --args="list"
                  ./gradlew :tooling:runCli --args="destroy <path>"

                Options:
                  --package=com.acme.app   Base package (else APP_BASE_PACKAGE or .codegen/config.properties basePackage)
                  --config=path            Config file (default: .codegen/config.properties)
                  --force                  Overwrite if exists
                  --dry-run                Print without writing
                  --author=you             Add author meta comment
                  --tag=cqrs               Add tag meta comment
                  --lang=en                Language code (for messages)

                Commands:
                  generate usecase <Name>     Create a new interactor stub (@UseCaseComponent + @UseCaseTransactional)
                  generate controller <Name>  Create a new @ApiController stub with a GET endpoint
                  generate dto <Name>         Create a Request DTO stub
                  generate adapter <Name>     Create a persistence adapter stub
                  generate port <Name>        Create a port interface stub
                  generate test <Name>        Create a JUnit test stub for interactor
                  generate migration <name>   Create a Flyway-like SQL stub
                  generate config <Name>      Create a Spring @Configuration stub
                  generate message            Create a messages_<lang>.properties stub
                  rename project <NewName>    Update project name, spring.application.name, config/env prefixes, group
                  list                        Show tracked generated files
                  destroy <path>              Delete a tracked file
                """);
    }

    private record ParsedArgs(List<String> positional, Map<String, String> options) {}
}


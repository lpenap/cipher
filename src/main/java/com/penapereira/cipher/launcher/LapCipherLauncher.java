package com.penapereira.cipher.launcher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.penapereira.cipher.service.ApplicationService;
import com.penapereira.cipher.service.ConfigurationService;
import com.penapereira.cipher.service.impl.ApplicationServiceImpl;
import com.penapereira.cipher.service.impl.ConfigurationServiceImpl;

public class LapCipherLauncher {

    private final Logger logger = LogManager.getLogger();

    ApplicationService appService;
    ConfigurationService configService;

    public static void main(String[] args) {
        LapCipherLauncher launcher = new LapCipherLauncher();
        launcher.init(args);
        launcher.start(args);
    }

    public void init(String[] args) {
        configService = ConfigurationServiceImpl.instance();
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option configDir =
                Option.builder().argName("d").longOpt("config-dir").desc("configuration directory").hasArg().build();
        options.addOption(configDir);

        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("config-dir")) {
                configService.setConfigurationDirectory(line.getOptionValue("config-dir"));
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }

        appService = ApplicationServiceImpl.instance();
    }

    public void start(String[] args) {
        logger.info("Loading Application...");
        appService.start();
    }

    public void printUsage() {
        logger.info("USAGE:");
        logger.info("  java -jar cipher.jar []");
        logger.info("PARAMETERS:");
        logger.info("  prop : properties file to use.");
    }
}
